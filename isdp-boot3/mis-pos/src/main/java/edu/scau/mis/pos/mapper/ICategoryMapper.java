package edu.scau.mis.pos.mapper;

import edu.scau.mis.pos.domain.Category;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Mapper
@Primary
public interface ICategoryMapper {

    /**
     * 根据ID查询分类
     * @param categoryId 分类ID
     * @return 分类对象
     */
    Category selectCategoryById(Long categoryId);

    /**
     * 根据父级ID查询分类列表
     * @param parentId 父级ID
     * @return 分类列表
     */
    List<Category> selectCategoryByParentId(Long parentId);

    /**
     * 查询所有分类
     * @return 分类列表
     */
    List<Category> selectAllCategoryList();

    List<Category> selectCategoryList(Category category);

    /**
     * 新增分类
     * @param category 分类对象
     * @return 影响记录数
     */
    int insertCategory(Category category);

    /**
     * 修改分类
     * @param category 分类对象
     * @return 影响记录数
     */
    int updateCategory(Category category);

    /**
     * 删除分类
     * @param categoryId 分类ID
     * @return 影响记录数
     */
    int deleteCategoryById(Long categoryId);

    /**
     * 批量删除分类
     * @param categoryIds 分类ID数组
     * @return 影响记录数
     */
    int deleteCategoryByIds(Long[] categoryIds);
}