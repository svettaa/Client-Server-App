package com.lukichova.olenyn.app.Json;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data

public class GroupJs {

    private final Integer id;
    @NonNull
    private final String name;
    private final String description;

    public static final int MAX_NAME_LENGTH = 100;
    public static final int MAX_DESCRIPTION_LENGTH = 255;


    public void populateJsonNode(ObjectNode node) {
        node.put("id", getId());
        node.put("name", getName());
        node.put("description", getDescription());
    }
}
