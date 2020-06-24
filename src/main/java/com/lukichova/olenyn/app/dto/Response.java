package com.lukichova.olenyn.app.dto;

import com.sun.net.httpserver.HttpExchange;
import lombok.Data;

@Data
public class Response {
    String template = "";
    String token = "";
    String data;
    Integer statusCode;
    HttpExchange httpExchange;
}
