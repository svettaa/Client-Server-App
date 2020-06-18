package com.lukichova.olenyn.app;

import com.lukichova.olenyn.app.http.Controller;
import com.lukichova.olenyn.app.views.JsonView;
import com.lukichova.olenyn.app.views.View;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

import static com.lukichova.olenyn.app.resoures.Resoures.HTTP_SERVER_PORT;

public class HttpServerReflection {



    final static View VIEW = new JsonView();

    public static void main(String[] args) {
        try {
            Controller.setView(VIEW);

            HttpServer server = HttpServer.create();

            server.bind(new InetSocketAddress(HTTP_SERVER_PORT), 0);

            Controller controller = new Controller();
            server.createContext("/", controller);

            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
