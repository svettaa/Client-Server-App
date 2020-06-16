package com.lukichova.olenyn.app.http;

import com.lukichova.olenyn.app.DB.Goods;
import com.lukichova.olenyn.app.Exceptions.MissedJsonFieldEsxception;
import com.lukichova.olenyn.app.Exceptions.WrongJsoneException;
import com.lukichova.olenyn.app.JSON.ReadJSON;
import com.lukichova.olenyn.app.JSON.WriteJSON;
import com.lukichova.olenyn.app.dto.Response;
import com.lukichova.olenyn.app.views.View;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Controller implements HttpHandler {
    private static View view;
    private final ReadJSON readJSON = new ReadJSON();
    private final WriteJSON writeJSON = new WriteJSON();
    private final

    public static void setView(View newView) {
        view = newView;
    }

    public void getGoods(HttpExchange httpExchange, Map result) {

        Map<String, Object> params = (Map<String, Object>) (result.get("requestParameters"));
        String categoryStr = (String) params.get("categoryId");
        if (categoryStr != null) {
            Response response = new Response();

            response.setTemplate("list");
            response.setStatusCode(200);
            response.setData("{\"message\": \"You tried to get goods\"," +
                    "\"category\": " + Integer.parseInt(categoryStr) + "}");
            response.setHttpExchange(httpExchange);

            view.view(response);
        } else {
            Response response = new Response();

            response.setStatusCode(200);
            response.setData("{\"message\": \"You tried to get goods\"}");
            response.setHttpExchange(httpExchange);

            view.view(response);
        }
    }

    public void getGoodsById(HttpExchange httpExchange, Map result) {

        String[] parts = (String[]) result.get("requestUriPathParts");
        int id = Integer.parseInt(parts[3]);

        Response response = new Response();
        response.setStatusCode(200);

        response.setData("{\"message\": \"You tried to get goods by ID\", " +
                "\"id\": " + id + "}");
        response.setHttpExchange(httpExchange);

        view.view(response);
    }

    public void deleteGoods(HttpExchange httpExchange, Map result) {

        String[] parts = (String[]) result.get("requestUriPathParts");
        int id = Integer.parseInt(parts[3]);

        //if don't exist in the table - 404
        Response response = new Response();
        response.setStatusCode(204);

        response.setData("{\"message\": \"You deleted goods by ID\", " +
                "\"id\": " + id + "}");
        response.setHttpExchange(httpExchange);

        view.view(response);
    }

    public void putGoods(HttpExchange httpExchange, Map result) {
        Response response = new Response();
        response.setHttpExchange(httpExchange);
        try {
            String body = getBodyString(httpExchange);
            Goods goods = readJSON.selectGoods(body);

            // add

            // get by name
            // take id

            response.setStatusCode(204);
            response.setData(writeJSON.createCreatedIdReply(id));
        } catch (IOException e) {
            response.setStatusCode(40);
            response.setData(writeJSON.createErrorReply("No body"));
        } catch (MissedJsonFieldEsxception missedJsonFieldEsxception) {
            response.setStatusCode(204);
            response.setData(writeJSON.createErrorReply("Field missing"));
        } catch (WrongJsoneException e) {
            response.setStatusCode(204);
            response.setData(writeJSON.createErrorReply("WrongJson"));
        }
        view.view(response);
    }


    private String getBodyString(HttpExchange httpExchange) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(httpExchange.getRequestBody(), "UTF-8");

        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        return bufferedReader.readLine();
    }

    public void postGoods(HttpExchange httpExchange, Map result) {
        try {
            String postRequest = getBodyString(httpExchange);

            Map<String, Object> postRequestParameters = HttpUtil.parseQuery(postRequest);
            result.put("postRequestParameters", postRequestParameters);



        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void uknownEndpoint(HttpExchange httpExchange, Map result) {
        Response response = new Response();

        response.setStatusCode(404);
        response.setData("{\"message\": \"Unknown endpoint\"}");
        response.setHttpExchange(httpExchange);

        view.view(response);
    }

   /* public void handler(HttpExchange httpExchange, Map result) {
        URI requestUri = httpExchange.getRequestURI();

        String query = requestUri.getRawQuery();
        result.put("query", query);

        Map<String, Object> getRequestParameters = HttpUtil.parseQuery(query);
        result.put("getRequestParameters", getRequestParameters);

        String requestMethod = httpExchange.getRequestMethod();
        String requestMethodLowercased = requestMethod.toLowerCase();

        if (requestMethodLowercased.equals("post")) {
            try {
                InputStreamReader inputStreamReader = new InputStreamReader(httpExchange.getRequestBody(), "UTF-8");

                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String postRequest = bufferedReader.readLine();

                Map<String, Object> postRequestParameters = HttpUtil.parseQuery(postRequest);
                result.put("postRequestParameters", postRequestParameters);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Response response = new Response();

        response.setTemplate("list");
        response.setStatusCode(200);
        response.setData(result);
        response.setHttpExchange(httpExchange);

        view.view(response);
    }*/

    @Override
    public void handle(HttpExchange httpExchange) {
        try {
            String methodToCallName = null;

            Map<String, Object> result = new HashMap<>();

            URI requestUri = httpExchange.getRequestURI();
            result.put("requestUri", requestUri);

            String requestUriPath = requestUri.getPath();
            result.put("requestUriPath", requestUriPath);

            String[] requestUriPathParts = requestUriPath.split("/");
            result.put("requestUriPathParts", requestUriPathParts);

            String method1 = httpExchange.getRequestMethod();
            String method = method1.toLowerCase();
            result.put("method", method);

            String query = requestUri.getRawQuery();
            result.put("query", query);

            int start = requestUri.toString().indexOf('?');
            String paramsStr;
            if (start == -1) {
                paramsStr = null;
            } else {
                paramsStr = requestUri.toString().substring(start + 1);
            }

            Map<String, Object> requestParameters = HttpUtil.parseQuery(paramsStr);
            result.put("requestParameters", requestParameters);

            //
            if (method.equals("get")) {
                if (requestUriPath.equals("/api/goods")) {
                    methodToCallName = "getGoods";
                } else if (Pattern.matches("/api/goods/\\d+", requestUriPath)) {
                    methodToCallName = "getGoodsById";
                }
            } else if (method.equals("delete")){
                if (Pattern.matches("/api/goods/\\d+", requestUriPath)) {
                    methodToCallName = "deleteGoods";
                }
            } else if (method.equals("put")){
                if (Pattern.matches("/api/goods", requestUriPath)) {
                    methodToCallName = "putGoods";
                }
            } /*else if(method.equals("post")){
                if (requestUriPath.equals("/api/goods")) {
                    methodToCallName = "postGoods";
            }*/

            if (methodToCallName == null) {
                uknownEndpoint(httpExchange, result);
            }

            Method controllerHandlerMethod = Controller.class.getDeclaredMethod(methodToCallName, HttpExchange.class, Map.class);
            controllerHandlerMethod.invoke(this, httpExchange, result);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }


}
