package com.lukichova.olenyn.app.DB;

import lombok.ToString;

@ToString
public class Goods {
    private String name;

    private double price;
    public Goods(){};
    public Goods(String name, double price) {
        this.name = name;
        this.price = price;
    }
    public Goods(Goods g) {
        this.name = g.name;
        this.price = g.price;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
