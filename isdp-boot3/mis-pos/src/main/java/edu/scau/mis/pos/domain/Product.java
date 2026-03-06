package edu.scau.mis.pos.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
public class Product implements Serializable {
    private Long productId;
    private String productSn;
    private String productName;
    private String productDescription;
    private BigDecimal price;
    private Long productCategoryId;
    private Category category;
    private String imageUrl;
    private String detailUrl;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;

    // 自定义构造函数，用于测试数据
    public Product(Long productId, String productSn, String productName, String productDescription, BigDecimal price, Long productCategoryId, Category category, String imageUrl, String detailUrl) {
        this.productId = productId;
        this.productSn = productSn;
        this.productName = productName;
        this.productDescription = productDescription;
        this.price = price;
        this.productCategoryId = productCategoryId;
        this.category = category;
        this.imageUrl = imageUrl;
        this.detailUrl = detailUrl;
    }
}