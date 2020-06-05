package com.lukichova.olenyn.app.DB;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class Group {
    private Integer id;
    private String name;
    private String description;
}
