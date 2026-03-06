package edu.scau.mis.web.controller;

import edu.scau.mis.common.web.domain.ApiResult;
import edu.scau.mis.pos.domain.Product;
import edu.scau.mis.pos.domain.Sale;
import edu.scau.mis.pos.service.IProductService;
import edu.scau.mis.pos.service.ISaleService;
import edu.scau.mis.view.vo.SaleItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/sale")
public class SaleController {
    @Autowired
    private ISaleService saleService;

    @Autowired
    private IProductService productService;

    /**
     * 开始一次新的销售
     */
    @GetMapping("/makeNewSale")
    public ApiResult<Sale> makeNewSale(){
        return ApiResult.success(saleService.makeNewSale());
    }
    /**
     * 输入商品
     * @param itemSn 产品编号
     * @param quantity 数量
     * 代码有部分业务逻辑，不符合controller内聚设计要求。
     * 这样写的原因是因为代码来自OOAD案例的Register，
     * Register是门面控制器，本身是domian类，允许写少量业务逻辑。
     */
    @PostMapping("/enterItem")
    public ApiResult<Sale> enterItem(@RequestParam("itemSn") String itemSn,
                                     @RequestParam("quantity") int quantity) {

        System.out.println("=== enterItem 调试 ===");
        System.out.println("itemSn: " + itemSn);
        System.out.println("quantity: " + quantity);

        Product product = productService.getProductBySn(itemSn);
        System.out.println("查询到的product: " + product);

        if(product != null) {
            System.out.println("商品存在，执行makeLineItem");
            Sale sale = saleService.makeLineItem(product, quantity);
            System.out.println("返回的sale: " + sale);
            return ApiResult.success(sale);
        } else {
            System.out.println("商品不存在，返回错误");
            return ApiResult.error(404, "商品不存在: " + itemSn);  // 明确设置404
        }
    }
    /**
     * 结束销售
     */
    @GetMapping("/endSale")
    public ApiResult<Sale> endSale(){
        Sale sale = saleService.endSale();
        return ApiResult.success(sale);
    }
    /**
     * 确认支付
     * @param cashTendered 支付金额
     */
    @PutMapping("/makePayment")
    public ApiResult<BigDecimal> makePayment(@RequestParam("cashTendered") BigDecimal cashTendered){
        return ApiResult.success(saleService.makePayment(cashTendered));
    }

    /**
     * 删除订单明细
     * @param itemSn 商品编码
     */
    @DeleteMapping("/removeItem")
    public ApiResult<Sale> removeItem(@RequestParam("itemSn") String itemSn) {
        Sale sale = saleService.removeLineItem(itemSn);
        return ApiResult.success(sale);
    }

    /**
     * 更新订单明细数量
     * @param itemSn 商品编码
     * @param quantity 新数量
     */
    @PutMapping("/updateQuantity")
    public ApiResult<Sale> updateQuantity(@RequestParam("itemSn") String itemSn,
                                          @RequestParam("quantity") int quantity) {

        // 记录日志便于调试
        System.out.println("更新商品数量 - 商品编码: " + itemSn + ", 新数量: " + quantity);

        Sale sale = saleService.updateLineItemQuantity(itemSn, quantity);
        return ApiResult.success(sale);
    }
}
