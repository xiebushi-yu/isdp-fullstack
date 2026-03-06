package edu.scau.mis.pos.mapper;

import edu.scau.mis.IsdpBootApplication;
import edu.scau.mis.pos.domain.Product;
import org.junit.jupiter.api.*;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false) // 必需设为false，即测试会更新数据库
@ContextConfiguration(classes = IsdpBootApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // 添加执行顺序控制
class IProductMapperTest {

    @Autowired
    private IProductMapper productMapper;

    private static Long testProductId; // 用于保存测试商品的ID

    @Test
    @Order(1) // 第一个执行：插入测试数据
    void insertProduct() {
        Product product = new Product();
        product.setProductSn("1011");
        product.setProductName("牙膏");
        product.setProductDescription("黑人牙膏越刷越白");
        product.setPrice(new BigDecimal("10.00"));
        product.setProductCategoryId(2L);
        product.setImageUrl("https://www.baidu.com");
        product.setDetailUrl("https://www.baidu.com");
        productMapper.insertProduct(product);

        // 保存插入的商品ID，供后续测试使用
        testProductId = product.getProductId();

        // 验证插入成功
        Product inserted = productMapper.selectProductBySn("1011");
        assertEquals("牙膏", inserted.getProductName());
    }

    @Test
    @Order(2) // 第二个执行：查询测试
    void selectProductById() {
        Product product = productMapper.selectProductById(1L);
        assertEquals("钢笔", product.getProductName());
    }

    @Test
    @Order(3)
    void selectProductBySn() {
        Product product = productMapper.selectProductBySn("1001");
        assertEquals("钢笔", product.getProductName());
    }

    @Test
    @Order(4)
    void selectProductList() {
        Product product = new Product();
        product.setProductName("笔");
        List<Product> products = productMapper.selectProductList(product);
        assertTrue(products.size() >= 1);
    }

    @Test
    @Order(5)
    void selectAllProductList() {
        List<Product> products = productMapper.selectAllProductList();
        assertTrue(products.size() > 0);
    }

    @Test
    @Order(6) // 第六个执行：更新测试（使用前面插入的商品）
    void updateProduct() {
        Product product = new Product();
        product.setProductId(testProductId); // 使用保存的测试商品ID
        product.setProductName("牙刷");
        productMapper.updateProduct(product);

        // 验证更新成功
        Product updated = productMapper.selectProductBySn("1011");
        assertEquals("牙刷", updated.getProductName());
    }

    @Test
    @Order(7) // 第七个执行：删除测试（使用前面插入的商品）
    void deleteProductById() {
        // 删除测试商品
        productMapper.deleteProductById(testProductId);

        // 验证删除成功
        assertNull(productMapper.selectProductBySn("1011"));
    }

    @Test
    @Order(8) // 最后执行：批量删除测试
    void deleteProductByIds() {
        // 插入两个测试商品用于批量删除
        Product product1 = new Product();
        product1.setProductSn("1012");
        product1.setProductName("沐浴露");
        product1.setProductDescription("洗去疲惫");
        product1.setProductCategoryId(2L);
        product1.setPrice(new BigDecimal("10.00"));
        productMapper.insertProduct(product1);

        Product product2 = new Product();
        product2.setProductSn("1013");
        product2.setProductName("洗发水");
        product2.setProductDescription("清爽无刺激");
        product2.setProductCategoryId(2L);
        product2.setPrice(new BigDecimal("20.00"));
        productMapper.insertProduct(product2);

        // 记录当前商品数量
        int beforeCount = productMapper.selectAllProductList().size();

        // 执行批量删除
        productMapper.deleteProductByIds(new Long[]{product1.getProductId(), product2.getProductId()});

        // 验证删除后数量正确
        int afterCount = productMapper.selectAllProductList().size();
        assertEquals(beforeCount - 2, afterCount);
    }
}