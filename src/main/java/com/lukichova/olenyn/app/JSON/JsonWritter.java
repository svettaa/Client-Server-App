package com.lukichova.olenyn.app.JSON;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.lukichova.olenyn.app.Exceptions.WrongServerJsonException;


import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;

public class JsonWritter {

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
            rootNode.put("body", message);

            mapper.writeValue(outputStream,
                    rootNode);
            return outputStream.toString();
        } catch (Exception e) {
            throw new WrongServerJsonException();
        }
    }


   /* public String generateOneEntityReply(EntityDB entityDB) throws WrongServerJsonException {
        try {
            OutputStream outputStream = new ByteArrayOutputStream();
            ObjectMapper mapper = new ObjectMapper();

            ObjectNode rootNode = mapper.createObjectNode();
            entityDB.populateJsonNode(rootNode);

            mapper.writeValue(outputStream, rootNode);
            return outputStream.toString();
        } catch (Exception e) {
            throw new WrongServerJsonException();
        }
    }
*/




  /*  public String generateListReply(List<? extends EntityDB> entitiesDB) throws WrongServerJsonException {
        try {
            OutputStream outputStream = new ByteArrayOutputStream();
            ObjectMapper mapper = new ObjectMapper();

            ObjectNode rootNode = mapper.createObjectNode();

            ArrayNode array = rootNode.putArray("content");
            for (EntityDB entityDB : entitiesDB) {
                ObjectNode element = array.addObject();
                entityDB.populateJsonNode(element);
            }

            mapper.writeValue(outputStream, rootNode);
            return outputStream.toString();
        } catch (Exception e) {
            throw new WrongServerJsonException();
        }
    }


    public String generatePagingReply(List<? extends EntityDB> entitiesDB, PagingInfo pagingInfo)
            throws WrongServerJsonException {

        try {
            OutputStream outputStream = new ByteArrayOutputStream();
            ObjectMapper mapper = new ObjectMapper();

            ObjectNode rootNode = mapper.createObjectNode();
            rootNode.put("offset", pagingInfo.getOffset());
            rootNode.put("limit", pagingInfo.getLimit());
            rootNode.put("total", pagingInfo.getTotal());

            ArrayNode array = rootNode.putArray("content");
            for (EntityDB entityDB : entitiesDB) {
                ObjectNode element = array.addObject();
                entityDB.populateJsonNode(element);
            }

            mapper.writeValue(outputStream, rootNode);
            return outputStream.toString();
        } catch (Exception e) {
            throw new WrongServerJsonException();
        }
    }
*/
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

}
