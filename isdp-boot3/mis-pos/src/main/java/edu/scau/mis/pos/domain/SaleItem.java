package edu.scau.mis.pos.domain;

import edu.scau.mis.common.domain.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SaleItem extends BaseEntity implements Serializable {
    private Long saleItemId;
    private Product product;
    private int quantity;
    private Long saleId;
    private Long productId;
    private BigDecimal price;
    private String status;
    private String delFlag;
}
