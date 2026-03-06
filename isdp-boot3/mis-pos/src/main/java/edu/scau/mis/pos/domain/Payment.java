package edu.scau.mis.pos.domain;

import edu.scau.mis.common.domain.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Data
public class Payment extends BaseEntity implements Serializable {
    private Long paymentId;
    private BigDecimal amount;
    private Date payTime;
}
