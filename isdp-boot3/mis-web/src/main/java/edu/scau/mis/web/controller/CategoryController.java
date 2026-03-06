package edu.scau.mis.web.controller;

import edu.scau.mis.pos.domain.Category;
import edu.scau.mis.pos.service.ICategoryService;
import edu.scau.mis.common.web.domain.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @GetMapping("/{categoryId}")
    public ApiResult<Category> getById(@PathVariable("categoryId") Long categoryId) {
        Category category = categoryService.getCategoryById(categoryId);
        return category == null ? ApiResult.noContent() : ApiResult.success(category);
    }

    @GetMapping("/listAll")
    public ApiResult<List<Category>> listAll() {
        List<Category> categories = categoryService.getAllCategories();
        return categories.isEmpty() ? ApiResult.noContent() : ApiResult.success(categories);
    }

    @GetMapping("/listByParams")
    public ApiResult<List<Category>> listByParams(Category category) {
        List<Category> categories = categoryService.getCategories(category);
        return categories.isEmpty() ? ApiResult.noContent() : ApiResult.success(categories);
    }

    @PostMapping("/add")
    public ApiResult<String> add(@RequestBody Category category) {
        int result = categoryService.addCategory(category);
        if (result > 0) {
            return ApiResult.success("分类添加成功");
        } else {
            return ApiResult.error("分类添加失败");
        }
    }

    @PutMapping("/update")
    public ApiResult<String> update(@RequestBody Category category) {
        int result = categoryService.updateCategory(category);
        if (result > 0) {
            return ApiResult.success("分类更新成功");
        } else {
            return ApiResult.error("分类更新失败");
        }
    }

    @DeleteMapping("/{categoryId}")
    public ApiResult<String> delete(@PathVariable("categoryId") Long categoryId) {
        int result = categoryService.deleteCategory(categoryId);
        if (result > 0) {
            return ApiResult.success("分类删除成功");
        } else {
            return ApiResult.error("分类删除失败");
        }
    }
}