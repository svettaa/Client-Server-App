package com.lukichova.olenyn.app.JSON;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lukichova.olenyn.app.DB.Goods;
import com.lukichova.olenyn.app.DB.Group;
import com.lukichova.olenyn.app.Exceptions.MissedJsonFieldEsxception;
import com.lukichova.olenyn.app.Exceptions.WrongJsoneException;

import java.math.BigDecimal;

public class ReadJSON {


    public String selectToken(String json) throws WrongJsoneException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readValue(json, JsonNode.class);

            return root.get("token").asText();
        } catch (JsonProcessingException e) {
            throw new WrongJsoneException();
        }
    }

    public Integer selectId(String json) throws WrongJsoneException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readValue(json, JsonNode.class);

            return root.get("id").asInt();
        } catch (JsonProcessingException e) {
            throw new WrongJsoneException();
        }
    }


    public Group selectGroup(String json) throws WrongJsoneException, MissedJsonFieldEsxception {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readValue(json, JsonNode.class);

            Integer id = getIntFieldIfExists(root, "id");
            String name = root.get("name").asText();
            String description = getStringFieldIfExists(root, "description");

            return new Group(id, name, description);
        } catch (JsonProcessingException e) {
            throw new WrongJsoneException();
        } catch (NullPointerException e) {
            throw new MissedJsonFieldEsxception();
        }
    }


    public Goods selectGoods(String json) throws WrongJsoneException, MissedJsonFieldEsxception {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readValue(json, JsonNode.class);

            Integer id = getIntFieldIfExists(root, "id");
            String name = root.get("name").asText();

            Integer leftAmount = getIntFieldIfExists(root, "left_amount");
            String producer = root.get("producer").asText();
            String description = getStringFieldIfExists(root, "description");
            BigDecimal price = new BigDecimal(root.get("price").asText());
            Integer categoryId = root.get("group_id").asInt();

            return new Goods(id, name, price, leftAmount, producer, description, categoryId);
        } catch (JsonProcessingException | NumberFormatException e) {
            throw new WrongJsoneException();
        } catch (NullPointerException e) {
            throw new MissedJsonFieldEsxception();
        }
    }

    public Integer selectGroupId(String json) throws WrongJsoneException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readValue(json, JsonNode.class);

            return root.get("group_id").asInt();
        } catch (JsonProcessingException e) {
            throw new WrongJsoneException();
        }
    }


    public String selectName(String json) throws WrongJsoneException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readValue(json, JsonNode.class);

            return root.get("name").asText();
        } catch (JsonProcessingException e) {
            throw new WrongJsoneException();
        }
    }


    public Integer selectLeftAmount(String json) throws WrongJsoneException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readValue(json, JsonNode.class);

            return root.get("left_amount").asInt();
        } catch (JsonProcessingException e) {
            throw new WrongJsoneException();
        }
    }


    private String getStringFieldIfExists(JsonNode parent, String field) {
        JsonNode jsonNode = parent.get(field);
        if (jsonNode == null || jsonNode.isNull())
            return null;
        return jsonNode.asText();
    }

    private Integer getIntFieldIfExists(JsonNode parent, String field) {
        JsonNode jsonNode = parent.get(field);
        if (jsonNode == null || jsonNode.isNull())
            return null;
        return jsonNode.asInt();
    }
}
