package com.lukichova.olenyn.app.dto;

import com.sun.net.httpserver.HttpExchange;
import lombok.Data;

@Data
public class Response {
    String template = "";
    Object data;
    Integer statusCode;
    HttpExchange httpExchange;
}
