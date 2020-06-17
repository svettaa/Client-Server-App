package com.lukichova.olenyn.app.DB;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class User {
    private Integer id;
    private String login;
    private String password;
    private String role;



    public User(){}

    public User(Integer id,String login,String password, String role) {
        this.id=id;
        this.login = login;
        this.password = password;
        this.role = role;

    }

}
