package com.lukichova.olenyn.app.JSON;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lukichova.olenyn.app.DB.Goods;
import com.lukichova.olenyn.app.DB.Group;
import com.lukichova.olenyn.app.Exceptions.WrongServerJsonException;
import com.sun.net.httpserver.HttpExchange;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;

public class WriteJSON {


    public void writeResponseAutorization(HttpExchange exchange,
                                          int statusCode, Object response) throws IOException {
        ObjectMapper OBJECT_MAPPER = new ObjectMapper();
        final byte[] bytes = OBJECT_MAPPER.writeValueAsBytes(response);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        exchange.getResponseBody().write(bytes);
    }

    public String createCreatedIdReply(int id){
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode rootNode = mapper.createObjectNode();
            rootNode.put("id", id);
            return mapper.writeValueAsString(rootNode);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
    public String createSuccessfulReply(String message) throws WrongServerJsonException {
        try {
            OutputStream outputStream = new ByteArrayOutputStream();
            ObjectMapper mapper = new ObjectMapper();

            ObjectNode rootNode = mapper.createObjectNode();
            rootNode.put("message", message);

            mapper.writeValue(outputStream, rootNode);
            return outputStream.toString();
        } catch (Exception e) {
            throw new WrongServerJsonException();
        }
    }

    public String createTokenReply(String jws) throws WrongServerJsonException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode rootNode = mapper.createObjectNode();
            rootNode.put("token", jws);
            return mapper.writeValueAsString(rootNode);
        } catch (JsonProcessingException e) {
            throw new WrongServerJsonException();
        }
    }

    public String createErrorReply(String error){
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode rootNode = mapper.createObjectNode();
            rootNode.put("error", error);
            return mapper.writeValueAsString(rootNode);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
    public String createCostReply(BigDecimal cost) throws WrongServerJsonException {
        try {
            OutputStream outputStream = new ByteArrayOutputStream();
            ObjectMapper mapper = new ObjectMapper();

            ObjectNode rootNode = mapper.createObjectNode();
            rootNode.put("cost", cost.toString());

            mapper.writeValue(outputStream, rootNode);
            return outputStream.toString();
        } catch (Exception e) {
            throw new WrongServerJsonException();
        }
    }
    public String createGoodsReply(Goods goods) throws WrongServerJsonException {
        try {
            OutputStream outputStream = new ByteArrayOutputStream();
            ObjectMapper mapper = new ObjectMapper();

            ObjectNode root = mapper.createObjectNode();
            populateWithGoods(root, goods);

            mapper.writeValue(outputStream, root);
            return outputStream.toString();
        } catch (Exception e) {
            throw new WrongServerJsonException();
        }
    }

    public String createGoodsListReply(List<Goods> goods) throws WrongServerJsonException {
        try {
            OutputStream outputStream = new ByteArrayOutputStream();
            ObjectMapper mapper = new ObjectMapper();

            ObjectNode rootNode = mapper.createObjectNode();
            ArrayNode array = rootNode.putArray("list");

            for (Goods good : goods) {
                ObjectNode element = array.addObject();
                populateWithGoods(element, good);
            }

            mapper.writeValue(outputStream, rootNode);
            return outputStream.toString();
        } catch (Exception e) {
            throw new WrongServerJsonException();
        }
    }

    private void populateWithGoods(ObjectNode node, Goods goods){
        node.put("id", goods.getId());
        node.put("name", goods.getName());
        node.put("producer", goods.getProducer());
        node.put("description", goods.getDescription());
        node.put("left_amount", goods.getLeft_amount());
        node.put("price", goods.getPrice().toString());
        node.put("group_id", goods.getGroup_id());
    }

    public String createGroupReply(Group group) throws WrongServerJsonException {
        try {
            OutputStream outputStream = new ByteArrayOutputStream();
            ObjectMapper mapper = new ObjectMapper();

            ObjectNode root = mapper.createObjectNode();
            populateWithGroup(root, group);

            mapper.writeValue(outputStream, root);
            return outputStream.toString();
        } catch (Exception e) {
            throw new WrongServerJsonException();
        }
    }

    public String createGroupListReply(List<Group> groups) throws WrongServerJsonException {
        try {
            OutputStream outputStream = new ByteArrayOutputStream();
            ObjectMapper mapper = new ObjectMapper();

            ObjectNode rootNode = mapper.createObjectNode();
            ArrayNode array = rootNode.putArray("list");

            for (Group group : groups) {
                ObjectNode element = array.addObject();
                populateWithGroup(element, group);
            }

            mapper.writeValue(outputStream, rootNode);
            return outputStream.toString();
        } catch (Exception e) {
            throw new WrongServerJsonException();
        }
    }

    private void populateWithGroup(ObjectNode node, Group group){
        node.put("id", group.getId());
        node.put("name", group.getName());
        node.put("description", group.getDescription());
    }

}
