package com.lukichova.olenyn.app.JSON;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
public class GoodsJSON {
    private final Integer id;
    @NonNull
    private final String name;
    @NonNull
    private final BigDecimal price;
    private final Integer left_amount;
    @NonNull
    private final String producer;
    private final String description;


    @NonNull
    private final Integer group_id;

    public static final int MAX_NAME_LENGTH = 100;
    public static final int MAX_PRODUCER_LENGTH = 100;
    public static final int MAX_DESCRIPTION_LENGTH = 255;
//    private Integer id;
//    private String name;
//    private BigDecimal price;
//    private Integer left_amount;
//    private String producer;
//    private String description;
//    private Integer group_id;


    public void populateJsonNode(ObjectNode node) {
        node.put("id", getId());
        node.put("name", getName());
        node.put("price", getPrice().toString());
        node.put("left_amount", getLeft_amount());
        node.put("producer", getProducer());
        node.put("description", getDescription());
        node.put("group_id", getGroup_id());
    }
}
