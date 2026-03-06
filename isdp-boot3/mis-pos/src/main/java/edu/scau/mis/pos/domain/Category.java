package edu.scau.mis.pos.domain;

import edu.scau.mis.common.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseEntity implements Serializable{
    private Long categoryId;
    private Long parentId;
    private String categoryName;


}
