package edu.scau.mis.pos.service;

import edu.scau.mis.pos.domain.Category;
import java.util.List;

public interface ICategoryService {

    Category getCategoryById(Long categoryId);

    List<Category> getCategoryByParentId(Long parentId);

    List<Category> getAllCategories();

    List<Category> getCategories(Category category);

    int addCategory(Category category);

    int updateCategory(Category category);

    int deleteCategory(Long categoryId);

    int deleteCategoryByIds(Long[] categoryIds);
}