package edu.scau.mis.pos.mapper;

import edu.scau.mis.IsdpBootApplication;
import edu.scau.mis.pos.domain.Category;
import org.junit.jupiter.api.*;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
@ContextConfiguration(classes = IsdpBootApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ICategoryMapperTest {

    @Autowired
    private ICategoryMapper categoryMapper;

    private static Long testCategoryId;

    @Test
    @Order(1)
    void insertCategory() {
        Category category = new Category();
        category.setCategoryName("测试分类");
        category.setParentId(0L);

        int result = categoryMapper.insertCategory(category);
        testCategoryId = category.getCategoryId();

        assertEquals(1, result);
        assertNotNull(testCategoryId);
    }

    @Test
    @Order(2)
    void selectCategoryById() {
        Category category = categoryMapper.selectCategoryById(1L);
        assertNotNull(category);
        assertEquals("文具", category.getCategoryName());
    }

    @Test
    @Order(3)
    void selectCategoryByParentId() {
        List<Category> categories = categoryMapper.selectCategoryByParentId(0L);
        assertTrue(categories.size() >= 3); // 至少有3个顶级分类
    }

    @Test
    @Order(4)
    void selectAllCategoryList() {
        List<Category> categories = categoryMapper.selectAllCategoryList();
        assertTrue(categories.size() >= 3);
    }

    @Test
    @Order(5)
    void updateCategory() {
        Category category = new Category();
        category.setCategoryId(testCategoryId);
        category.setCategoryName("更新后的分类名称");

        int result = categoryMapper.updateCategory(category);
        assertEquals(1, result);

        Category updated = categoryMapper.selectCategoryById(testCategoryId);
        assertEquals("更新后的分类名称", updated.getCategoryName());
    }

    @Test
    @Order(6)
    void deleteCategoryById() {
        int result = categoryMapper.deleteCategoryById(testCategoryId);
        assertEquals(1, result);

        Category deleted = categoryMapper.selectCategoryById(testCategoryId);
        assertNull(deleted);
    }

    @Test
    @Order(7)
    void deleteCategoryByIds() {
        // 插入两个测试分类用于批量删除
        Category category1 = new Category();
        category1.setCategoryName("测试分类1");
        category1.setParentId(0L);
        categoryMapper.insertCategory(category1);

        Category category2 = new Category();
        category2.setCategoryName("测试分类2");
        category2.setParentId(0L);
        categoryMapper.insertCategory(category2);

        // 记录当前分类数量
        int beforeCount = categoryMapper.selectAllCategoryList().size();

        // 执行批量删除
        categoryMapper.deleteCategoryByIds(new Long[]{category1.getCategoryId(), category2.getCategoryId()});

        // 验证删除后数量正确
        int afterCount = categoryMapper.selectAllCategoryList().size();
        assertEquals(beforeCount - 2, afterCount);
    }
}