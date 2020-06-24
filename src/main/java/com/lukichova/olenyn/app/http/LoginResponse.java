package com.lukichova.olenyn.app.http;

import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class LoginResponse {


    private String login;
    private String role;

    public LoginResponse( String login, String role) {


        this.login = login;

        this.role = role;

    }
}