package com.lukichova.olenyn.app.http;

import com.lukichova.olenyn.app.DB.User;
import com.lukichova.olenyn.app.DB.UserDao;
import com.lukichova.olenyn.app.service.JwtService;
import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;


public class MyAuthenticator extends Authenticator {
    private static final HttpPrincipal ANONYMOUS_USER = new HttpPrincipal("anonymous", "anonymous");
    @Override
    public Result authenticate(final HttpExchange httpExchange) {
        final String token = httpExchange.getRequestHeaders().getFirst("Authorization");
        UserDao userDao = new UserDao();
        if (token != null) {
            try {
                final String username = JwtService.getUsernameFromToken(token);
                final User user = userDao.getByLogin(username);
                if (user != null) {
                    return new Success(new HttpPrincipal(username, user.getRole()));
                } else {
                    return new Retry(401);
                }

            } catch (Exception e) {
                return new Failure(403);
            }
        }

        return new Success(ANONYMOUS_USER);
    }
}
