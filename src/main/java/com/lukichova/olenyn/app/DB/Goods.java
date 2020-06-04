package com.lukichova.olenyn.app.DB;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class Goods {
    private String name;

    private double price;
    public Goods(){};
    public Goods(String name, double price) {
        this.name = name;
        this.price = price;

    }
}
