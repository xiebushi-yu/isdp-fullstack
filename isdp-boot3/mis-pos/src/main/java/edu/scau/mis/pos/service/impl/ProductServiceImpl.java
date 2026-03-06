package edu.scau.mis.pos.service.impl;

import com.aliyun.oss.ServiceException;
import edu.scau.mis.common.web.domain.HttpCode;
import edu.scau.mis.pos.domain.Product;
import edu.scau.mis.pos.mapper.IProductMapper;
import edu.scau.mis.pos.service.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ProductServiceImpl implements IProductService {
    @Autowired
    private IProductMapper productMapper;
    @Override
    public Product getProductById(Long productId) {
        Product product = productMapper.selectProductById(productId);
        log.debug("查询商品成功：{}", product);
        return product;
    }

    @Override
    public Product getProductBySn(String productSn) {
        return productMapper.selectProductBySn(productSn);
    }

    @Override
    public List<Product> getAllProducts() {
        return productMapper.selectAllProductList();
    }

    @Override
    public List<Product> getProducts(Product product) {
        return productMapper.selectProductList(product);
    }

    @Override
    public int addProduct(Product product) {
        Product p = productMapper.selectProductBySn(product.getProductSn());
        if (p != null) {
            throw new ServiceException(String.valueOf(HttpCode.PRODUCT_SN_ALREADY_EXIST));
        }
        product.setCreateTime(new Date());
        return productMapper.insertProduct(product);
    }

    @Override
    public int updateProduct(Product product) {
        product.setUpdateTime(new Date());
        return productMapper.updateProduct(product);
    }

    @Override
    public int deleteProduct(Long productId) {
        // 实际项目建议采用逻辑删除，这里为了演示直接物理删除
        return productMapper.deleteProductById(productId);
    }

    @Override
    public int deleteProductByIds(Long[] productIds) {
        return productMapper.deleteProductByIds(productIds);
    }
}
