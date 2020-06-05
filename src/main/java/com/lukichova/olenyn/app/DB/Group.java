package com.lukichova.olenyn.app.DB;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class Group {
    private Integer id;
    private String name;
    private String description;

    Group(){}

    public Group(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
