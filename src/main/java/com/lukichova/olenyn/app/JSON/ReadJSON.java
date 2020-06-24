package com.lukichova.olenyn.app.JSON;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lukichova.olenyn.app.DB.Goods;
import com.lukichova.olenyn.app.DB.Group;
import com.lukichova.olenyn.app.Exceptions.MissedJsonFieldException;
import com.lukichova.olenyn.app.Exceptions.WrongJsonException;

import java.math.BigDecimal;

public class ReadJSON {

    public String selectStringSearch(String json) throws WrongJsonException, MissedJsonFieldException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readValue(json, JsonNode.class);


            String name = root.get("value").asText();


            return name;
        } catch (JsonProcessingException e) {
            throw new WrongJsonException();
        } catch (NullPointerException e) {
            throw new MissedJsonFieldException();
        }
    }

    public Goods selectUpdateAmount(String json) throws WrongJsonException, MissedJsonFieldException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readValue(json, JsonNode.class);

            Integer id = root.get("id").asInt();

            Integer leftAmount = root.get("amount").asInt();

            Integer action = root.get("action").asInt();

            if (action == 1)
                return new Goods(id, "add", null, leftAmount, null, null, null);
            if (action == 0)
                return new Goods(id, null, null, leftAmount, null, null, null);
            else throw new WrongJsonException();
        } catch (JsonProcessingException | NumberFormatException e) {
            throw new WrongJsonException();
        } catch (NullPointerException e) {
            throw new MissedJsonFieldException();
        }
    }


    public Group selectGroup(String json) throws WrongJsonException, MissedJsonFieldException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readValue(json, JsonNode.class);

            Integer id = getIntFieldIfExists(root, "id");
            String name = root.get("name").asText();
            String description = getStringFieldIfExists(root, "description");

            return new Group(id, name, description);
        } catch (JsonProcessingException e) {
            throw new WrongJsonException();
        } catch (NullPointerException e) {
            throw new MissedJsonFieldException();
        }
    }


    public Goods selectGoods(String json) throws WrongJsonException, MissedJsonFieldException {
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
            throw new WrongJsonException();
        } catch (NullPointerException e) {
            throw new MissedJsonFieldException();
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
