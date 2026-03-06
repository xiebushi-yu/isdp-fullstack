package edu.scau.mis.view;

import edu.scau.mis.pos.domain.Product;
import edu.scau.mis.pos.domain.Sale;
import edu.scau.mis.pos.domain.SaleItem;
import edu.scau.mis.pos.mapper.ProductCatalog;
import edu.scau.mis.pos.service.ISaleService;
import edu.scau.mis.view.vo.SaleItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * 记账本
 * GRASP:控制器
 * 作为领域层的门面控制器，协调跨领域业务逻辑
 */
@Component
public class Register {
    @Autowired
    private ISaleService saleService;

    @Autowired
    private ProductCatalog productCatalog;

    private Sale currentSale;

    /**
     * 开始一次新的销售
     */
    public void makeNewSale() {
        currentSale = saleService.makeNewSale();
    }

    /**
     * 输入商品
     * @param itemSn 商品序列号
     * @param quantity 数量
     * @return 销售项视图对象列表
     */
    public Sale enterItem(String itemSn, int quantity) {
        Product product = productCatalog.selectProductBySn(itemSn);
        if (product != null) {
            Sale sale = saleService.makeLineItem(product, quantity);
            return sale;
        }
        return null;
    }

    /**
     * 结束销售
     * @return 总金额
     */
    public Sale endSale() {
        return saleService.endSale();
    }

    /**
     * 确认支付
     * @param cashTendered 支付金额
     * @return 找零金额
     */
    public BigDecimal makePayment(BigDecimal cashTendered) {
        return saleService.makePayment(cashTendered);
    }

    /**
     * 获取销售项
     * @return 销售项视图对象列表
     */
    public List<SaleItemVo> getSaleItems() {
        if (currentSale == null || currentSale.getSaleItems() == null) {
            return List.of();
        }
        return currentSale.getSaleItems().stream()
                .map(saleItem -> new SaleItemVo(
                        saleItem.getProduct().getProductSn(),
                        saleItem.getProduct().getProductName(),
                        saleItem.getProduct().getPrice(),
                        saleItem.getQuantity()))
                .toList();
    }
}