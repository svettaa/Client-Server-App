package com.lukichova.olenyn.app.views;

import com.lukichova.olenyn.app.dto.Response;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class JsonView implements View {
    @Override
    public void view(Response response) {
        String responseBody = "{\"error\": \"response encoding error\"}";
        Integer statusCode = 500;

        responseBody = response.getData();
        statusCode = response.getStatusCode();
        HttpExchange httpExchange = response.getHttpExchange();

        Headers responseHeaders = httpExchange.getResponseHeaders();
        responseHeaders.set("Content-Type", "application/json");

        try {
            if (responseBody != null) {

                httpExchange.sendResponseHeaders(statusCode, responseBody.length());
                OutputStream outputStream = httpExchange.getResponseBody();
                outputStream.write(responseBody.getBytes());
                System.out.println(responseBody);
                outputStream.close();
            } else {

                httpExchange.sendResponseHeaders(statusCode, 0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

