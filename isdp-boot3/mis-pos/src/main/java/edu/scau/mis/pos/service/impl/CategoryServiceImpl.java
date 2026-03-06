package edu.scau.mis.pos.service.impl;

import edu.scau.mis.pos.domain.Category;
import edu.scau.mis.pos.mapper.ICategoryMapper;
import edu.scau.mis.pos.service.ICategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private ICategoryMapper categoryMapper;

    @Override
    public Category getCategoryById(Long categoryId) {
        Category category = categoryMapper.selectCategoryById(categoryId);
        log.debug("查询分类成功：{}", category);
        return category;
    }

    @Override
    public List<Category> getCategoryByParentId(Long parentId) {
        return categoryMapper.selectCategoryByParentId(parentId);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryMapper.selectAllCategoryList();
    }

    @Override
    public List<Category> getCategories(Category category) {
        // 由于CategoryMapper没有条件查询方法，这里直接返回所有分类
        // 如果需要条件查询，需要在ICategoryMapper中添加对应方法
        return categoryMapper.selectAllCategoryList();
    }

    @Override
    public int addCategory(Category category) {
        // 分类通常不需要检查唯一编码，如果需要可以添加相应逻辑
        category.setCreateTime(new Date());
        return categoryMapper.insertCategory(category);
    }

    @Override
    public int updateCategory(Category category) {
        category.setUpdateTime(new Date());
        return categoryMapper.updateCategory(category);
    }

    @Override
    public int deleteCategory(Long categoryId) {
        // 实际项目建议采用逻辑删除，这里为了演示直接物理删除
        return categoryMapper.deleteCategoryById(categoryId);
    }

    @Override
    public int deleteCategoryByIds(Long[] categoryIds) {
        return categoryMapper.deleteCategoryByIds(categoryIds);
    }
}