package com.lukichova.olenyn.app.DB;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@ToString
@Data
public class Goods {
    private Integer id;
    private String name;
    private BigDecimal price;
    private Integer left_amount;
    private String producer;
    private String description;
    private Integer group_id;
}
