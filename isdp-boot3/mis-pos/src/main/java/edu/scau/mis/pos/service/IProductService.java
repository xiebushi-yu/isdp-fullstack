package edu.scau.mis.pos.service;

import edu.scau.mis.pos.domain.Product;
import java.util.List;

public interface IProductService {
    Product getProductById(Long productId);

    Product getProductBySn(String productSn);

    List<Product> getAllProducts();

    List<Product> getProducts(Product product);

    int addProduct(Product product);

    int updateProduct(Product product);

    int deleteProduct(Long productId);

    int deleteProductByIds(Long[] productIds);
}