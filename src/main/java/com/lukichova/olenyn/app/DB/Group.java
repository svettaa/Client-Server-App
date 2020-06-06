package com.lukichova.olenyn.app.DB;

import lombok.Data;

@Data
public class Group {
    private Integer id;
    private String name;
    private String description;

    Group(){}

    public Group(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Group(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
