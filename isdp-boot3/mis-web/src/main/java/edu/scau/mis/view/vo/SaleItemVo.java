package edu.scau.mis.view.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleItemVo {
    private String itemSn;
    private String productName;
    private BigDecimal price;
    private int quantity;
}
