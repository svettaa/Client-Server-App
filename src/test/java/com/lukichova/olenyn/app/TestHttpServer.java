package com.lukichova.olenyn.app;

import com.lukichova.olenyn.app.JSON.ReadJSON;
import org.junit.Test;

public class TestHttpServer {

    @Test
    public void demo() throws Exception {
        ReadJSON reader = new ReadJSON();
        ReceivedResponse response;
        String token;



        // delete all
        response = new ReceivedResponse("DELETE", "/api/goods",
                null, null);
        response.assertResponse(204, null);


        response = new ReceivedResponse("DELETE", "/api/group",
                null, null);
        response.assertResponse(204, null);


        // add groups
        response = new ReceivedResponse("PUT", "/api/group",
                "{\"id\": 1, \"name\": \"Diary products\", \"description\": \"Tasty diary products\"}", null);
        response.assertResponse(201, "{\"id\":1}");

        response = new ReceivedResponse("PUT", "/api/group",
                "{\"id\": 2, \"name\": \"Drinks\"}", null);
        response.assertResponse(201, "{\"id\":2}");


        response = new ReceivedResponse("PUT", "/api/group",
                "{\"id\": 3, \"name\": \"Clothes\", \"description\": \"Clothes\"}", null);
        response.assertResponse(201, "{\"id\":3}");


        response = new ReceivedResponse("PUT", "/api/group",
                "{\"name\": }", null);
        response.assertResponse(409, null);

    }
}