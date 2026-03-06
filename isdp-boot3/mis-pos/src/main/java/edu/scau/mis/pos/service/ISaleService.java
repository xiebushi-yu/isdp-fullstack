package edu.scau.mis.pos.service;

import edu.scau.mis.pos.domain.Product;
import edu.scau.mis.pos.domain.Sale;
import edu.scau.mis.pos.domain.SaleItem;

import java.math.BigDecimal;
import java.util.List;

public interface ISaleService {
    /**
     * 开始新的销售
     */
    Sale makeNewSale();

    /**
     * 实例化SaleItem
     * @param product 商品对象
     * @param quantity 数量
     * @return 订单明细列表
     */
    Sale makeLineItem(Product product, int quantity);

    /**
     * 结束订单商品录入
     * @return 总金额
     */
    Sale endSale();

    /**
     * 订单支付
     * @param cashTendered 付款金额
     * @return 找零
     */
    BigDecimal makePayment(BigDecimal cashTendered);

    /**
     * 删除订单明细
     * @param itemSn 商品编码
     * @return 更新后的销售对象
     */
    Sale removeLineItem(String itemSn);

    /**
     * 更新订单明细数量
     * @param itemSn 商品编码
     * @param newQuantity 新数量
     * @return 更新后的销售对象
     */
    Sale updateLineItemQuantity(String itemSn, int newQuantity);
}


