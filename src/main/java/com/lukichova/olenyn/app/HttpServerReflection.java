package com.lukichova.olenyn.app;

import com.lukichova.olenyn.app.http.Controller;
import com.lukichova.olenyn.app.http.MyAuthenticator;
import com.lukichova.olenyn.app.views.JsonView;
import com.lukichova.olenyn.app.views.View;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpServerReflection {

    final static int HTTP_SERVER_PORT = 8888;
    private static HttpServer server;

    final static View VIEW = new JsonView();

   public void stop() {
        this.server.stop(1);
  }
    public static void main(String[] args) {
        try {
            Controller.setView(VIEW);

             server = HttpServer.create();

            server.bind(new InetSocketAddress(HTTP_SERVER_PORT), 0);

            Controller exampleMultiactionController = new Controller();

            server.createContext("/", exampleMultiactionController).setAuthenticator(new MyAuthenticator());

            server.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
