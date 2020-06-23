package com.lukichova.olenyn.app.http;

import com.lukichova.olenyn.app.DB.*;
import com.lukichova.olenyn.app.Exceptions.*;
import com.lukichova.olenyn.app.JSON.ReadJSON;
import com.lukichova.olenyn.app.JSON.WriteJSON;
import com.lukichova.olenyn.app.dto.Response;
import com.lukichova.olenyn.app.service.GoodsService;
import com.lukichova.olenyn.app.service.GroupService;
import com.lukichova.olenyn.app.views.View;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;



public class Controller implements HttpHandler {

    private static View view;
    private final ReadJSON readJSON = new ReadJSON();
    private final WriteJSON writeJSON = new WriteJSON();
    private final GoodsService goodsService = new GoodsService();
    private final GroupService groupService = new GroupService();


    public static void setView(View newView) {
        view = newView;
    }


    public void getGroup(HttpExchange httpExchange, Map result) {
        try {
            Response response = new Response();

            List<Group> groupList = groupService.getAll();

            response.setStatusCode(200);
            response.setData(writeJSON.createGroupListReply(groupList));
            response.setHttpExchange(httpExchange);

            view.view(response);

        } catch (wrongDataBaseConnection | WrongServerJsonException e) {
            e.printStackTrace();
        }
    }

    public void getGroupById(HttpExchange httpExchange, Map result) throws noItemWithSuchIdException, wrongDataBaseConnection, WrongServerJsonException {

        String[] parts = (String[]) result.get("requestUriPathParts");

        int id = Integer.parseInt(parts[3]);

        Response response = new Response();

        Group group = groupService.listByCriteria(id);

        response.setStatusCode(200);

        response.setData(writeJSON.createGroupReply(group));
        response.setHttpExchange(httpExchange);

        view.view(response);
    }

    public void deleteGroupById(HttpExchange httpExchange, Map result) throws WrongServerJsonException, wrongDataBaseConnection {

        String[] parts = (String[]) result.get("requestUriPathParts");
        int id = Integer.parseInt(parts[3]);

        Response response = new Response();

        try {
            groupService.delete(id);

            response.setStatusCode(204);
        } catch (noItemWithSuchIdException e) {

            response.setStatusCode(404);
        }

        response.setData(null);
        response.setHttpExchange(httpExchange);

        view.view(response);
    }

    public void deleteAllGroups(HttpExchange httpExchange, Map result) throws wrongDataBaseConnection, noItemWithSuchIdException {
        Response response = new Response();

        groupService.deleteAll();
        response.setStatusCode(204);
        response.setHttpExchange(httpExchange);
        response.setData(null);

        view.view(response);

    }

    public void putGroup(HttpExchange httpExchange, Map result) throws noItemWithSuchNameException, wrongDataBaseConnection, wrongNotUniqueValue, IOException, WrongJsonException, MissedJsonFieldException, WrongJsonInputData {
        Response response = new Response();
        response.setHttpExchange(httpExchange);

        String body = getBodyString(httpExchange);
        Group group = readJSON.selectGroup(body);

        groupService.create(group);

        Group fetched = groupService.listByCriteria(group.getName());
        int id = fetched.getId();

        response.setStatusCode(201);
        response.setData(writeJSON.createCreatedIdReply(id));
        response.setHttpExchange(httpExchange);

        view.view(response);
    }

    public void postGroup(HttpExchange httpExchange, Map result) throws wrongNotUniqueValue, wrongDataBaseConnection, IOException, MissedJsonFieldException, WrongJsonException, noItemWithSuchNameException, noItemWithSuchIdException, WrongServerJsonException, WrongJsonInputData {
        Response response = new Response();
        response.setHttpExchange(httpExchange);

        String body = getBodyString(httpExchange);
        Group group = readJSON.selectGroup(body);

        groupService.update(group);

        response.setStatusCode(204);
        response.setHttpExchange(httpExchange);
        response.setData(null);

        view.view(response);
    }


    //goods
    public void getGoods(HttpExchange httpExchange, Map result) {
        try {
            Map<String, Object> params = (Map<String, Object>) (result.get("requestParameters"));
            String categoryStr = (String) params.get("categoryId");
            if (categoryStr != null) {
                Response response = new Response();
                int categoryId = Integer.parseInt(categoryStr);

                List<Goods> goodsList = goodsService.getByGroupId(categoryId);

                response.setTemplate("list");
                response.setStatusCode(200);
                response.setData(writeJSON.createGoodsListReply(goodsList));
                response.setHttpExchange(httpExchange);

                view.view(response);
            } else {
                Response response = new Response();

                List<Goods> goodsList = goodsService.getAll();

                response.setTemplate("list");
                response.setStatusCode(200);
                response.setData(writeJSON.createGoodsListReply(goodsList));
                response.setHttpExchange(httpExchange);

                view.view(response);
            }
        } catch (wrongDataBaseConnection | WrongServerJsonException | noItemWithSuchIdException e) {
            e.printStackTrace();
        }
    }

    public void getGoodsById(HttpExchange httpExchange, Map result) throws noItemWithSuchIdException, wrongDataBaseConnection, WrongServerJsonException {

        String[] parts = (String[]) result.get("requestUriPathParts");
        int id = Integer.parseInt(parts[3]);

        Response response = new Response();
        Goods goods = goodsService.listByCriteria(id);
        response.setStatusCode(200);

        response.setData(writeJSON.createGoodsReply(goods));
        response.setHttpExchange(httpExchange);

        view.view(response);
    }

    public void deleteAllGoods(HttpExchange httpExchange, Map result) throws wrongDataBaseConnection, noItemWithSuchIdException {

        Map<String, Object> params = (Map<String, Object>) (result.get("requestParameters"));
        String categoryStr = (String) params.get("categoryId");
        if (categoryStr != null) {
            Response response = new Response();
            int categoryId = Integer.parseInt(categoryStr);

            goodsService.deleteByGroupId(categoryId);

            response.setStatusCode(204);
            response.setData(null);
            response.setHttpExchange(httpExchange);

            view.view(response);
        } else {
            Response response = new Response();

            goodsService.deleteAll();
            response.setStatusCode(204);
            response.setHttpExchange(httpExchange);
            response.setData(null);

            view.view(response);
        }
    }

    public void deleteGoodsById(HttpExchange httpExchange, Map result) throws wrongDataBaseConnection, noItemWithSuchIdException {

        String[] parts = (String[]) result.get("requestUriPathParts");
        int id = Integer.parseInt(parts[3]);

        Response response = new Response();
        try {
            goodsService.delete(id);


            response.setStatusCode(204);
        } catch (noItemWithSuchIdException e) {

            response.setStatusCode(404);
        }
        response.setHttpExchange(httpExchange);
        response.setData(null);


        view.view(response);
    }

    public void putGoods(HttpExchange httpExchange, Map result) throws noItemWithSuchNameException, wrongDataBaseConnection, wrongNotUniqueValue, IOException, WrongJsonException, MissedJsonFieldException, WrongJsonInputData {
        Response response = new Response();
        response.setHttpExchange(httpExchange);

        String body = getBodyString(httpExchange);
        Goods goods = readJSON.selectGoods(body);

        goodsService.create(goods);

        Goods fetched = goodsService.listByCriteria(goods.getName());
        int id = fetched.getId();

        response.setStatusCode(201);
        response.setData(writeJSON.createCreatedIdReply(id));
        response.setHttpExchange(httpExchange);

        view.view(response);
    }


    private String getBodyString(HttpExchange httpExchange) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(httpExchange.getRequestBody(), "UTF-8");

        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        return bufferedReader.readLine();
    }

    public void postGoods(HttpExchange httpExchange, Map result) throws wrongNotUniqueValue, wrongDataBaseConnection, IOException, MissedJsonFieldException, WrongJsonException, noItemWithSuchNameException, noItemWithSuchIdException, WrongServerJsonException, WrongJsonInputData {
        Response response = new Response();
        response.setHttpExchange(httpExchange);

        String body = getBodyString(httpExchange);
        Goods goods = readJSON.selectGoods(body);


        try {
            goodsService.update(goods);


            response.setStatusCode(204);
        } catch (noItemWithSuchIdException e) {

            response.setStatusCode(404);
        }

        response.setHttpExchange(httpExchange);
        response.setData(null);

        view.view(response);
    }


    public void unknownEndpoint(HttpExchange httpExchange, Map result) {
        Response response = new Response();

        response.setStatusCode(404);
        response.setData("{\"message\": \"Unknown endpoint\"}");
        response.setHttpExchange(httpExchange);

        view.view(response);
    }

    @Override
    public void handle(HttpExchange httpExchange) {
        Response response = new Response();
        response.setHttpExchange(httpExchange);
        try {
            String methodToCallName = null;

            Map<String, Object> result = new HashMap<>();

            URI requestUri = httpExchange.getRequestURI();
            result.put("requestUri", requestUri);
            System.out.println(result);
            String requestUriPath = requestUri.getPath();
            result.put("requestUriPath", requestUriPath);

            String[] requestUriPathParts = requestUriPath.split("/");
            result.put("requestUriPathParts", requestUriPathParts);

            String method1 = httpExchange.getRequestMethod();
            String method = method1.toLowerCase();
            result.put("method", method);

            String query = requestUri.getRawQuery();
            result.put("query", query);

            ;
            int start = requestUri.toString().indexOf('?');
            String paramsStr;
            if (start == -1) {
                paramsStr = null;
            } else {
                paramsStr = requestUri.toString().substring(start + 1);
            }

            Map<String, Object> requestParameters = HttpUtil.parseQuery(paramsStr);
            result.put("requestParameters", requestParameters);


            httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

            if (httpExchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                httpExchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, PUT, DELETE, OPTIONS");
                httpExchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
                httpExchange.sendResponseHeaders(204, -1);
                return;
            }


            if (method.equals("get")) {

                if (Pattern.matches("^/api/goods$", requestUriPath)) {
                    getGoods(httpExchange, result);
                } else if (Pattern.matches("^/api/goods/\\d+$", requestUriPath)) {
                    getGoodsById(httpExchange, result);
                } else if (Pattern.matches("^/api/group$", requestUriPath)) {
                    getGroup(httpExchange, result);
                } else if (Pattern.matches("^/api/group/\\d+$", requestUriPath)) {
                    getGroupById(httpExchange, result);
                } else {
                    unknownEndpoint(httpExchange, result);
                }
            } else if (method.equals("delete")) {
                if (Pattern.matches("^/api/goods$", requestUriPath)) {
                    deleteAllGoods(httpExchange, result);
                } else if (Pattern.matches("^/api/goods/\\d+$", requestUriPath)) {
                    deleteGoodsById(httpExchange, result);
                } else if (Pattern.matches("^/api/group$", requestUriPath)) {
                    deleteAllGroups(httpExchange, result);
                } else if (Pattern.matches("^/api/group/\\d+$", requestUriPath)) {
                    deleteGroupById(httpExchange, result);
                } else {
                    unknownEndpoint(httpExchange, result);
                }
            } else if (method.equals("put")) {
                if (Pattern.matches("^/api/goods$", requestUriPath)) {
                    putGoods(httpExchange, result);
                } else if (Pattern.matches("^/api/group$", requestUriPath)) {
                    putGroup(httpExchange, result);
                } else {
                    unknownEndpoint(httpExchange, result);
                }
            } else if (method.equals("post")) {
                if (Pattern.matches("^/api/goods$", requestUriPath)) {
                    postGoods(httpExchange, result);
                } else if (Pattern.matches("^/api/group$", requestUriPath)) {
                    postGroup(httpExchange, result);
                } else {
                    unknownEndpoint(httpExchange, result);
                }
            }


        } catch (MissedJsonFieldException | noItemWithSuchIdException e) {
            response.setStatusCode(404);
            response.setData(writeJSON.createErrorReply("No field"));
            view.view(response);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WrongJsonInputData | WrongJsonException e) {
            response.setStatusCode(409);
            response.setData(writeJSON.createErrorReply("Wrong input data"));
            view.view(response);
        } catch (wrongDataBaseConnection e) {
            System.out.println("Wrong database connection");
        } catch (noItemWithSuchNameException e) {
            System.out.println("No item with such name");
        } catch (wrongNotUniqueValue e) {
            System.out.println("Not unique value");
        } catch (WrongServerJsonException e) {
            e.printStackTrace();
        }
    }


}
