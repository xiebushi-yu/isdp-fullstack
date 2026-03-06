package edu.scau.mis.pos.service.impl;  // 保持相同的包名

import edu.scau.mis.pos.service.IProductService;
import lombok.extern.slf4j.Slf4j;
import edu.scau.mis.pos.domain.Product;  // 如果需要的话
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class ProductServiceImplTest {

    @Autowired
    private IProductService productService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        // 在每个测试方法执行前准备测试数据
        testProduct = new Product();
        testProduct.setProductName("测试商品");
        testProduct.setProductSn("TEST001");
        testProduct.setProductDescription("这是一个测试商品");
        testProduct.setProductCategoryId(1L); // 文具分类
    }

    @AfterEach
    void tearDown() {
        // 在每个测试方法执行后清理测试数据
        try {
            // 尝试删除测试商品
            Product existingProduct = productService.getProductBySn("TEST001");
            if (existingProduct != null) {
                productService.deleteProduct(existingProduct.getProductId());
            }

            Product existingProduct2 = productService.getProductBySn("TEST002");
            if (existingProduct2 != null) {
                productService.deleteProduct(existingProduct2.getProductId());
            }
        } catch (Exception e) {
            log.warn("清理测试数据失败: {}", e.getMessage());
        }
    }

    @Test
    void getProductById() {
        Product product = productService.getProductById(1L);
        log.info("根据ID查询商品结果: {}", product);
        // 可以添加更多断言
        assertNotNull(product, "商品不应为null");
    }

    @Test
    void getProductBySn() {
        Product product = productService.getProductBySn("1002");
        log.info("根据SN查询商品结果: {}", product);
        assertEquals(3L, product.getProductId(), "测试不通过");
    }

    @Test
    void getAllProducts() {
        List<Product> products = productService.getAllProducts();
        log.info("查询所有商品结果，共{}个商品", products.size());
        assertNotNull(products, "商品列表不应为null");
        assertFalse(products.isEmpty(), "商品列表不应为空");

        // 验证第一个商品的基本信息
        if (!products.isEmpty()) {
            Product firstProduct = products.get(0);
            assertNotNull(firstProduct.getProductName(), "商品名称不应为null");
            assertNotNull(firstProduct.getProductSn(), "商品SN不应为null");
        }
    }

    @Test
    void getProducts() {
        // 测试分页查询或条件查询
        Product queryProduct = new Product();
        queryProduct.setProductName("钢笔"); // 假设按名称模糊查询

        List<Product> products = productService.getProducts(queryProduct);
        log.info("条件查询商品结果，共{}个商品", products.size());
        assertNotNull(products, "商品列表不应为null");

        // 验证查询结果都包含关键词（如果实现的是模糊查询）
        for (Product product : products) {
            assertTrue(product.getProductName().contains("钢笔"),
                    "商品名称应包含查询关键词");
        }
    }

    @Test
    void addProduct() {
        // 执行添加操作
        int result = productService.addProduct(testProduct);
        log.info("添加商品结果: {}", result > 0 ? "成功" : "失败");
        assertTrue(result > 0, "添加商品应该成功");

        // 验证商品是否真的添加到数据库
        Product savedProduct = productService.getProductBySn("TEST001");
        assertNotNull(savedProduct, "新添加的商品应该能查询到");
        assertEquals("测试商品", savedProduct.getProductName(), "商品名称应该一致");
        assertEquals(1L, savedProduct.getProductCategoryId(), "商品分类ID应该一致");
        assertNotNull(savedProduct.getProductId(), "商品ID应该被自动生成");
    }


    @Test
    void updateProduct() {
        // 先添加一个商品
        productService.addProduct(testProduct);
        Product savedProduct = productService.getProductBySn("TEST001");

        // 准备更新数据
        savedProduct.setProductName("更新后的商品");
        savedProduct.setProductDescription("这是更新后的描述");

        // 执行更新
        int result = productService.updateProduct(savedProduct);
        log.info("更新商品结果: {}", result > 0 ? "成功" : "失败");
        assertTrue(result > 0, "更新商品应该成功");

        // 验证更新是否生效
        Product updatedProduct = productService.getProductBySn("TEST001");
        assertNotNull(updatedProduct, "更新后的商品应该能查询到");
        assertEquals("更新后的商品", updatedProduct.getProductName(), "商品名称应该更新");
        assertEquals("这是更新后的描述", updatedProduct.getProductDescription(), "商品描述应该更新");
    }

    @Test
    void deleteProduct() {
        // 先添加一个测试商品
        productService.addProduct(testProduct);
        Product savedProduct = productService.getProductBySn("TEST001");
        assertNotNull(savedProduct, "测试商品应该存在");

        // 执行删除
        int result = productService.deleteProduct(savedProduct.getProductId());
        log.info("删除商品结果: {}", result > 0 ? "成功" : "失败");
        assertTrue(result > 0, "删除商品应该成功");

        // 验证商品是否真的被删除
        Product deletedProduct = productService.getProductBySn("TEST001");
        assertNull(deletedProduct, "删除后的商品应该查询不到");
    }
}