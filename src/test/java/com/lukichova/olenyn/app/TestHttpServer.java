package com.lukichova.olenyn.app;

import org.junit.Test;

public class TestHttpServer {

    @Test
    public void testHTTP() throws Exception {
        ReceivedResponse response;
        String token;



        // delete all

        response = new ReceivedResponse("DELETE", "/api/goods",
                null, null);
        response.assertResponse(204, null);


        response = new ReceivedResponse("DELETE", "/api/group",
                null, null);
        response.assertResponse(204, null);


        // test groups
        response = new ReceivedResponse("PUT", "/api/group",
                "{\"id\": 1, \"name\": \"Diary products\", \"description\": \"Tasty diary products\"}", null);
        response.assertResponse(201, "{\"id\":1}");


        response = new ReceivedResponse("PUT", "/api/group",
                "{\"id\": 2, \"name\": \"Drinks\"}", null);
        response.assertResponse(201, "{\"id\":2}");


        response = new ReceivedResponse("PUT", "/api/group",
                "{\"id\": 3, \"name\": \"Clothes\", \"description\": \"Clothes\"}", null);
        response.assertResponse(201, "{\"id\":3}");


        response = new ReceivedResponse("POST", "/api/group",
                "{\"id\": 3, \"name\": \"Jeans\", \"description\": \"Jeans\"}", null);
        response.assertResponse(204, null);

        response = new ReceivedResponse("GET", "/api/group/3",
                null, null);
        response.assertResponse(200, "{\"id\":3,\"name\":\"Jeans\",\"description\":\"Jeans\"}");

        //error 409
        response = new ReceivedResponse("PUT", "/api/group",
                "{\"name\": }", null);
        response.assertResponse(409, null);

        //error 404
        response = new ReceivedResponse("GET", "/api/group/02329",
                null, null);
        response.assertResponse(404, null);


        //test goods
        response = new ReceivedResponse("PUT", "/api/goods",
                "{\"id\": 1, \"name\": \"milk\", \"price\": 30, \"left_amount\": 10, \"producer\": \"Yagotinske\", \"description\": \"Tasty milk\", \"group_id\": 1}", null);
        response.assertResponse(201, "{\"id\":1}");

        response = new ReceivedResponse("PUT", "/api/goods",
                "{\"id\": 2, \"name\": \"kefir\", \"price\": 25, \"left_amount\": 12, \"producer\": \"Yagotinske\", \"description\": \"Tasty kefir\", \"group_id\": 1}", null);
        response.assertResponse(201, "{\"id\":2}");

        response = new ReceivedResponse("PUT", "/api/goods",
                "{\"id\": 10, \"name\": \"cola\", \"price\": 10, \"left_amount\": 16, \"producer\": \"Coca Cola\", \"description\": \"Tasty cola\", \"group_id\": 2}", null);
        response.assertResponse(201, "{\"id\":10}");


        response = new ReceivedResponse("PUT", "/api/goods",
                "{\"id\": 11, \"name\": \"sprite\", \"price\": 11, \"left_amount\": 50, \"producer\": \"Coca Cola\", \"description\": \"Tasty sprite\", \"group_id\": 2}", null);
        response.assertResponse(201, "{\"id\":11}");


        response = new ReceivedResponse("PUT", "/api/goods",
                "{\"id\": 12, \"name\": \"long jeans\", \"price\": 100, \"left_amount\": 50, \"producer\": \"Levis\", \"description\": \"Beautiful jeans\", \"group_id\": 3}", null);
        response.assertResponse(201, "{\"id\":12}");

        //error 409
        response = new ReceivedResponse("PUT", "/api/goods",
                "{\"id\": 5, \"name\": \"aa\", \"price\": -25, \"left_amount\": 12, \"producer\": \"Yagotinske\", \"description\": \"Tasty aa\", \"group_id\": 1}", null);
        response.assertResponse(409, null);


        response = new ReceivedResponse("POST", "/api/goods",
                "{\"id\": 11, \"name\": \"fanta\", \"price\": 11, \"left_amount\": 50, \"producer\": \"Coca Cola\", \"description\": \"Tasty fanta\", \"group_id\": 2}", null);
        response.assertResponse(204, null);

        response = new ReceivedResponse("GET", "/api/goods/10",
                null, null);
        response.assertResponse(200, "{\"id\":10,\"name\":\"cola\",\"producer\":\"Coca Cola\",\"description\":\"Tasty cola\",\"left_amount\":16,\"price\":\"10\",\"group_id\":2}");

        //error 404
        response = new ReceivedResponse("GET", "/api/goods/02329",
                null, null);
        response.assertResponse(404, null);
    }
}
