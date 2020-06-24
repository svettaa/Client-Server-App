package com.lukichova.olenyn.app;

import com.lukichova.olenyn.app.DB.User;
import com.lukichova.olenyn.app.DB.UserDao;
import com.lukichova.olenyn.app.Exceptions.wrongDataBaseConnection;
import com.lukichova.olenyn.app.Exceptions.wrongNotUniqueValue;
import com.lukichova.olenyn.app.http.Controller;
import com.lukichova.olenyn.app.service.GoodsService;
import com.lukichova.olenyn.app.views.JsonView;
import com.lukichova.olenyn.app.views.View;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

import static com.lukichova.olenyn.app.resoures.Resoures.HTTP_SERVER_PORT;

public class HttpServerReflection {

    private static HttpServer server;

    final static View VIEW = new JsonView();

    public void stop() {
        this.server.stop(1);
    }

    public static void main(String[] args) {
        try {
            Controller.setView(VIEW);

            server = HttpServer.create();
//            User user = new User("sonya","best","rrr");
//       //     User userTest = new User("sveta","luk","ff");
//            UserDao userDao = new UserDao();
//            userDao.create(user);
            server.bind(new InetSocketAddress(HTTP_SERVER_PORT), 0);
//            GoodsService h = new GoodsService();
//            System.out.println(h.gettotalPrice());




            Controller controller = new Controller();
            server.createContext("/", controller);

            server.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
