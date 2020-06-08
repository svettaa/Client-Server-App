package com.lukichova.olenyn.app.DB;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Goods {
    private Integer id;
    private String name;
    private BigDecimal price;
    private Integer left_amount;
    private String producer;
    private String description;
    private Integer group_id;

    public Goods(){}

    public Goods(String name, BigDecimal price, Integer left_amount, String producer, String description, Integer group_id) {
        this.name = name;
        this.price = price;
        this.left_amount = left_amount;
        this.producer = producer;
        this.description = description;
        this.group_id = group_id;
    }

    public Goods(Integer id, String name, BigDecimal price, Integer left_amount, String producer, String description, Integer group_id) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.left_amount = left_amount;
        this.producer = producer;
        this.description = description;
        this.group_id = group_id;
    }
}
