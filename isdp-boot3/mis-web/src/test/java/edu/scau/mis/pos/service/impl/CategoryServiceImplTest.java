package edu.scau.mis.pos.service.impl;

import edu.scau.mis.pos.domain.Category;
import edu.scau.mis.pos.service.ICategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class CategoryServiceImplTest {

    @Autowired
    private ICategoryService categoryService;

    private Category testCategory;

    @BeforeEach
    void setUp() {
        // 在每个测试方法执行前准备测试数据
        testCategory = new Category();
        testCategory.setCategoryName("测试分类");
        testCategory.setParentId(0L);
    }

    @AfterEach
    void tearDown() {
        // 在每个测试方法执行后清理测试数据
        try {
            // 查找并删除测试分类
            List<Category> categories = categoryService.getCategories(testCategory);
            for (Category category : categories) {
                if ("测试分类".equals(category.getCategoryName()) ||
                        "更新后的分类".equals(category.getCategoryName())) {
                    categoryService.deleteCategory(category.getCategoryId());
                }
            }
        } catch (Exception e) {
            log.warn("清理测试数据失败: {}", e.getMessage());
        }
    }

    @Test
    void getCategoryById() {
        // 测试根据ID查询存在的分类
        Category category = categoryService.getCategoryById(1L);
        log.info("根据ID查询分类结果: {}", category);

        assertNotNull(category, "分类不应为null");
        assertEquals(1L, category.getCategoryId(), "分类ID应该为1");
        assertEquals("文具", category.getCategoryName(), "分类名称应该为文具");
        assertEquals(0L, category.getParentId(), "父分类ID应该为0");
    }


    @Test
    void getAllCategories() {
        // 测试查询所有分类
        List<Category> categories = categoryService.getAllCategories();
        log.info("查询所有分类结果，共{}个分类", categories.size());

        assertNotNull(categories, "分类列表不应为null");
        assertFalse(categories.isEmpty(), "分类列表不应为空");
        assertTrue(categories.size() >= 3, "至少应该有3个初始分类");

        // 验证分类的基本信息
        for (Category category : categories) {
            assertNotNull(category.getCategoryId(), "分类ID不应为null");
            assertNotNull(category.getCategoryName(), "分类名称不应为null");
            assertNotNull(category.getParentId(), "父分类ID不应为null");
        }
    }


    @Test
    void getCategories() {
        // 测试按父分类ID查询
        Category queryCondition = new Category();
        queryCondition.setParentId(0L); // 查询根分类

        List<Category> categories = categoryService.getCategories(queryCondition);
        log.info("按父分类ID查询结果，共{}个分类", categories.size());

        assertNotNull(categories, "分类列表不应为null");
        assertFalse(categories.isEmpty(), "应该找到父分类ID为0的分类");

        // 验证所有分类的父ID都是0
        for (Category category : categories) {
            assertEquals(0L, category.getParentId(),
                    "分类[" + category.getCategoryName() + "]的父分类ID应该为0");
        }
    }



    @Test
    void addCategory() {
        // 测试添加分类
        int result = categoryService.addCategory(testCategory);
        log.info("添加分类结果: {}", result > 0 ? "成功" : "失败");

        assertTrue(result > 0, "添加分类应该成功");

        // 验证分类是否真的添加到缓存
        Category queryCondition = new Category();
        queryCondition.setCategoryName("测试分类");
        List<Category> categories = categoryService.getCategories(queryCondition);

        assertFalse(categories.isEmpty(), "新添加的分类应该能查询到");
        Category savedCategory = categories.get(0);
        assertEquals("测试分类", savedCategory.getCategoryName(), "分类名称应该一致");
        assertEquals(0L, savedCategory.getParentId(), "父分类ID应该一致");
        assertNotNull(savedCategory.getCategoryId(), "分类ID应该被自动生成");
    }

    @Test
    void updateCategory() {
        // 先添加一个分类
        categoryService.addCategory(testCategory);

        // 获取添加的分类
        Category queryCondition = new Category();
        queryCondition.setCategoryName("测试分类");
        Category savedCategory = categoryService.getCategories(queryCondition).get(0);

        // 准备更新数据
        savedCategory.setCategoryName("更新后的分类");
        savedCategory.setParentId(1L);

        // 执行更新
        int result = categoryService.updateCategory(savedCategory);
        log.info("更新分类结果: {}", result > 0 ? "成功" : "失败");

        assertTrue(result > 0, "更新分类应该成功");

        // 验证更新是否生效
        Category updatedCategory = categoryService.getCategoryById(savedCategory.getCategoryId());
        assertNotNull(updatedCategory, "更新后的分类应该能查询到");
        assertEquals("更新后的分类", updatedCategory.getCategoryName(), "分类名称应该更新");
        assertEquals(1L, updatedCategory.getParentId(), "父分类ID应该更新");
    }


    @Test
    void deleteCategory() {
        // 先添加一个测试分类
        categoryService.addCategory(testCategory);

        // 获取添加的分类
        Category queryCondition = new Category();
        queryCondition.setCategoryName("测试分类");
        Category savedCategory = categoryService.getCategories(queryCondition).get(0);
        assertNotNull(savedCategory, "测试分类应该存在");

        // 执行删除
        int result = categoryService.deleteCategory(savedCategory.getCategoryId());
        log.info("删除分类结果: {}", result > 0 ? "成功" : "失败");

        assertTrue(result > 0, "删除分类应该成功");

        // 验证分类是否真的被删除
        Category deletedCategory = categoryService.getCategoryById(savedCategory.getCategoryId());
        assertNull(deletedCategory, "删除后的分类应该查询不到");
    }


}