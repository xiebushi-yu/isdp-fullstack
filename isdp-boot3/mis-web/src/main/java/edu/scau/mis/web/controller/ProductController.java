package edu.scau.mis.web.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.scau.mis.common.web.domain.ApiResult;
import edu.scau.mis.pos.domain.Product;
import edu.scau.mis.pos.service.IProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/product")
@Tag(name = "商品管理")
public class ProductController {
    @Autowired
    private IProductService productService;
    @Operation(summary = "根据ID查询商品")
    @GetMapping("/{productId}")
    @ApiResponse(responseCode = "200", description = "查询商品成功", content = @Content(schema = @Schema(implementation = Product.class)))
    public ApiResult<Product> getById(
            @Parameter(description = "商品ID", in = ParameterIn.PATH, required = true)
            @PathVariable("productId") Long productId){
        Product product = productService.getProductById(productId);
        return product == null ? ApiResult.noContent() : ApiResult.success(product);
    }
    @Operation(summary = "根据编号查询商品")
    @GetMapping("/getBySn/{productSn}")
    public ApiResult<Product> getBySn(@PathVariable("productSn") String productSn){
        Product product = productService.getProductBySn(productSn);
        return product == null ? ApiResult.noContent() : ApiResult.success(product);
    }

    @Operation(summary = "查询所有商品")
    @GetMapping("/listAll")
    public ApiResult<List<Product>> listAll(){
        List<Product> products = productService.getAllProducts();
        return products.isEmpty() ? ApiResult.noContent() : ApiResult.success(products);
    }

    @Operation(summary = "根据参数查询商品")
    @GetMapping("/listByParams")
    public ApiResult<List<Product>> listByParams(Product product){
        List<Product> products = productService.getProducts(product);
        return products.isEmpty() ? ApiResult.noContent() : ApiResult.success(products);
    }

    @Operation(summary = "分页查询商品")
    @GetMapping("/page")
    public ApiResult listByPage(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize, Product product){
        PageHelper.startPage(pageNum,pageSize);
        List<Product> productList = productService.getProducts(product);
        PageInfo<List> pageInfo = new PageInfo(productList);
        // 以下写法也可以
        // PageInfo<Product> pageInfo = new PageInfo<>(productList);
        return ApiResult.success(pageInfo);
    }

    @Operation(summary = "新增商品")
    @PostMapping("/add")
    public ApiResult<String> add(@RequestBody Product  product){
        int rows = productService.addProduct(product);
        return rows > 0 ? ApiResult.success("添加成功") : ApiResult.fail("添加失败");
    }

    @Operation(summary = "修改商品")
    @PutMapping("/update")
    public ApiResult<String> update(@RequestBody Product product){
        int rows = productService.updateProduct(product);
        return rows > 0 ? ApiResult.success("修改成功") : ApiResult.fail("修改失败");
    }
    @Operation(summary = "删除商品")
    @DeleteMapping("/delete/{productId}")
    public ApiResult<String> delete(@PathVariable("productId") Long productId){
        int rows = productService.deleteProduct(productId);
        return rows > 0 ? ApiResult.success("删除成功") : ApiResult.fail("删除失败");
    }

    @Operation(summary = "批量删除商品")
    @DeleteMapping("/deleteByIds/{productIds}")
    public ApiResult<String> deleteByIds(@PathVariable Long[] productIds){
        int rows = productService.deleteProductByIds(productIds);
        return productIds.length == rows? ApiResult.success("批量删除成功") : ApiResult.fail("批量删除失败");
    }


}
