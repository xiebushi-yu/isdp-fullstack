package edu.scau.mis.pos.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.scau.mis.common.domain.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class Sale extends BaseEntity implements Serializable {
    private Long saleId;
    private String saleNo;
    private BigDecimal total;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date saleTime;
    private String status;
    private List<SaleItem> saleItems = new ArrayList<>();
    private Payment payment;
    private Long paymentId;
    private String delFlag;
    private Integer totalQuantity;
}
