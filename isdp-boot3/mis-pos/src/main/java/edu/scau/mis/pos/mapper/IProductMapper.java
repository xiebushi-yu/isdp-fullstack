package edu.scau.mis.pos.mapper;

import edu.scau.mis.pos.domain.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Mapper
@Primary
public interface IProductMapper {

    /**
     * 根据ID查询产品
     * @param productId 产品ID
     * @return 产品对象
     */
    Product selectProductById(Long productId);

    /**
     * 根据产品编号查询产品
     * @param productSn 产品编号
     * @return 产品对象
     */
    Product selectProductBySn(String productSn);

    /**
     * 查询产品列表
     * @param product 查询参数
     * @return 产品列表
     */
    List<Product> selectProductList(Product product);

    /**
     * 查询所有产品
     * @return 产品列表
     */
    List<Product> selectAllProductList();

    /**
     * 新增产品
     * @param product 产品对象
     * @return 影响记录数
     */
    int insertProduct(Product product);

    /**
     * 修改产品
     * @param product 产品对象
     * @return 影响记录数
     */
    int updateProduct(Product product);

    /**
     * 删除产品
     * @param productId 产品ID
     * @return 影响记录数
     */
    int deleteProductById(Long productId);

    int deleteProductByIds(Long[] productIds);
}