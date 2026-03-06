package edu.scau.mis.pos.service.impl;

import cn.hutool.core.util.IdUtil;
import edu.scau.mis.pos.domain.Payment;
import edu.scau.mis.pos.domain.Product;
import edu.scau.mis.pos.domain.Sale;
import edu.scau.mis.pos.domain.SaleItem;
import edu.scau.mis.pos.enums.SaleStatusEnum;
import edu.scau.mis.pos.mapper.ISaleItemMapper;
import edu.scau.mis.pos.mapper.ISaleMapper;
import edu.scau.mis.pos.service.ISaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class SaleServiceImpl implements ISaleService {

    private Sale currentSale;

    @Autowired
    private ISaleMapper saleMapper;

    @Autowired
    private ISaleItemMapper saleItemMapper;

    /**
     * 开始新的销售
     */
    @Override
    public Sale makeNewSale() {
        currentSale = new Sale();
        Date saleDate = new Date();
        currentSale.setSaleTime(saleDate);
        String saleNo = "so-"+ IdUtil.getSnowflakeNextId();
        currentSale.setSaleNo(saleNo);
        currentSale.setStatus(SaleStatusEnum.RESERVED.getCode());
        currentSale.setTotal(new BigDecimal("0.00"));
        return currentSale;
    }

    /**
     * 实例化SaleItem
     * @param product 商品对象
     * @param quantity 数量
     */
    @Override
    public Sale makeLineItem(Product product, int quantity) {
        // 判断商品是否已录入，未录入则新增。已录入则修改数量。
        if(!isEntered(product.getProductSn(),quantity)){
            SaleItem saleItem = new SaleItem();
            saleItem.setProduct(product);
            saleItem.setQuantity(quantity);
            currentSale.getSaleItems().add(saleItem);
        }
        int totalQty = 0;
        for(SaleItem item : currentSale.getSaleItems()) {
            totalQty += item.getQuantity();
        }
        currentSale.setTotalQuantity(totalQty);
        currentSale.setTotal(this.getTotal()); // 重新计算总金额

        return currentSale;
    }

    /**
     * 判断商品是否已录入
     * 业务逻辑：如果已录入，则修改数量，否则添加saleLineItem
     * @param itemSn
     * @param quantity
     * @return
     */
    private boolean isEntered(String itemSn,int quantity){
        boolean flag = false;
        for(SaleItem si : currentSale.getSaleItems()){
            // 如果已经录入则修改数量
            if(itemSn.equals(si.getProduct().getProductSn())) {
                flag = true;
                int quantityOriginal = si.getQuantity();
                si.setQuantity(quantityOriginal + quantity);
            }
        }
        return flag;
    }

    /**
     * 结束订单商品录入
     * @return 总金额
     */
    @Override
    public Sale endSale() {
        BigDecimal total = this.getTotal();
        currentSale.setTotal(total);
        int totalQty = 0;
        for(SaleItem item : currentSale.getSaleItems()) {
            totalQty += item.getQuantity();
        }
        currentSale.setTotalQuantity(totalQty);
        currentSale.setCreateTime(new Date());
        saleMapper.insertSale(currentSale); // 插入订单
        // 将订单中商品明细持久化
        this.insertSaleItemsOfCurrentSale(currentSale);
        return currentSale;
    }

    /**
     * 计算总金额
     * @return
     */
    private BigDecimal getTotal(){
        BigDecimal total = new BigDecimal(0);
        int totalQty = 0; // 新增：计算总数量

        List<SaleItem> saleItemList = currentSale.getSaleItems();
        for(SaleItem si : saleItemList) {
            total = total.add(getSubTotal(si));
            totalQty += si.getQuantity(); // 新增：累加数量
        }

        currentSale.setTotalQuantity(totalQty); // 设置总数量
        return total;
    }

    /**
     * 计算小计
     * @param saleItem
     * @return
     */
    private BigDecimal getSubTotal(SaleItem saleItem){
        return saleItem.getProduct().getPrice().multiply(new BigDecimal(saleItem.getQuantity()));
    }

    /**
     * 订单支付
     * @param cashTendered 付款金额
     * @return 找零
     */
    @Override
    public BigDecimal makePayment(BigDecimal cashTendered) {
        BigDecimal total = currentSale.getTotal();
        Payment payment = new Payment();
        payment.setAmount(total);
        payment.setPayTime(new Date());
        currentSale.setPayment(payment);
        currentSale.setStatus(SaleStatusEnum.PAID.getCode());
        currentSale.setUpdateTime(new Date());
        BigDecimal change = cashTendered.subtract(total);
        saleMapper.updateSale(currentSale); // 更新订单
        // 更新订单商品明细的状态和更新时间
        this.updateSaleItemsOfCurrentSaleForMakePayment(currentSale);
        return change;
    }

    /**
     * 将Sale中的saleItem存入数据库
     * @param currentSale 当前sale对象
     */
    private void insertSaleItemsOfCurrentSale(Sale currentSale) {
        List<SaleItem> saleItemList = currentSale.getSaleItems();
        for(SaleItem si : saleItemList) {
            si.setStatus(SaleStatusEnum.RESERVED.getCode());
            si.setProductId(si.getProduct().getProductId());
            si.setPrice(si.getProduct().getPrice());
            si.setDelFlag("0");
            si.setCreateTime(new Date());
            // 注意sale插入db自增生成id后才能getSaleId()取到id值
            si.setSaleId(currentSale.getSaleId());
        }
        saleItemMapper.batchInsertSaleItemOfCurrentSale(saleItemList);
    }

    /**
     * 支付后修改商品明细
     * 修改【状态】和【更新时间】
     * @param currentSale 当前sale对象
     *
     * 是否能在最后一步makePayment再持久化sale和saleItem？
     * 取决于挂单业务需求，非技术问题
     * 所谓挂单：存储订单，一定时间后再取出订单支付。
     * 本笔记代码目前后端service支持挂单。但前端界面没有提供入口按钮。
     */
    private void updateSaleItemsOfCurrentSaleForMakePayment(Sale currentSale){
        SaleItem param = new SaleItem();
        param.setSaleId(currentSale.getSaleId());
        List<SaleItem> saleItems = saleItemMapper.selectSaleItemList(param);
        for (SaleItem si : saleItems) {
            si.setStatus(SaleStatusEnum.PAID.getCode());
            si.setUpdateTime(new Date());
            // 建议动态sql写一个批量修改saleItem接口，避免创建多条更新的sql语句。
            // 同学可以自行参考笔记批量新增saleItem代码自行完成。
            saleItemMapper.updateSaleItem(si);
        }
    }

    @Override
    public Sale removeLineItem(String itemSn) {
        // 1. 参数校验
        if (itemSn == null) {
            return currentSale;
        }

        // 2. 检查当前销售是否存在
        if (currentSale == null || currentSale.getSaleItems() == null) {
            return currentSale;
        }

        // 3. 查找并删除商品
        List<SaleItem> saleItems = currentSale.getSaleItems();
        boolean removed = saleItems.removeIf(saleItem ->
                saleItem.getProduct() != null &&
                        itemSn.equals(saleItem.getProduct().getProductSn())
        );

        // 4. 如果删除了商品，重新计算总数和总金额
        if (removed) {
            // 计算总数量
            int totalQty = 0;
            BigDecimal total = new BigDecimal("0.00");

            for (SaleItem si : saleItems) {
                totalQty += si.getQuantity();
                if (si.getProduct() != null && si.getProduct().getPrice() != null) {
                    BigDecimal subTotal = si.getProduct().getPrice()
                            .multiply(new BigDecimal(si.getQuantity()));
                    total = total.add(subTotal);
                }
            }

            // 5. 更新到currentSale
            currentSale.setTotalQuantity(totalQty);
            currentSale.setTotal(total);
        }

        // 6. 返回更新后的销售对象
        return currentSale;
    }

    @Override
    public Sale updateLineItemQuantity(String itemSn, int newQuantity) {
        // 1. 参数校验
        if (itemSn == null || itemSn.trim().isEmpty()) {
            return currentSale;
        }

        if (newQuantity <= 0) {
            // 如果数量<=0，相当于删除该商品
            return removeLineItem(itemSn);
        }

        // 2. 检查当前销售是否存在
        if (currentSale == null || currentSale.getSaleItems() == null) {
            return currentSale;
        }

        // 3. 查找要修改的商品
        List<SaleItem> saleItems = currentSale.getSaleItems();
        boolean found = false;

        for (SaleItem saleItem : saleItems) {
            if (saleItem.getProduct() != null &&
                    itemSn.equals(saleItem.getProduct().getProductSn())) {

                // 4. 更新数量
                saleItem.setQuantity(newQuantity);
                found = true;
                break;
            }
        }

        // 5. 如果找到并修改了商品，重新计算总数和总金额
        if (found) {
            // 计算总数量
            int totalQty = 0;
            BigDecimal total = new BigDecimal("0.00");

            for (SaleItem si : saleItems) {
                totalQty += si.getQuantity();
                if (si.getProduct() != null && si.getProduct().getPrice() != null) {
                    BigDecimal subTotal = si.getProduct().getPrice()
                            .multiply(new BigDecimal(si.getQuantity()));
                    total = total.add(subTotal);
                }
            }

            // 6. 更新到currentSale
            currentSale.setTotalQuantity(totalQty);
            currentSale.setTotal(total);
        }

        // 7. 返回更新后的销售对象
        return currentSale;
    }
}
