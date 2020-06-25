package com.lukichova.olenyn.app;

import lombok.Getter;
import org.junit.Assert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static com.lukichova.olenyn.app.resoures.Resoures.HTTP_SERVER_PORT;

@Getter
public class ReceivedResponse {
    private int status;
    private String Rbody;

    public ReceivedResponse(String method, String endpoint, String body, String token) throws IOException {
        URL url = new URL("http://localhost:" + HTTP_SERVER_PORT + endpoint);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod(method);

        if (token != null) {
            con.setRequestProperty("x-auth", token);
        }

        if (body != null) {
            con.setDoOutput(true);
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = body.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
        }

        status = con.getResponseCode();
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();
            Rbody = content.toString();
        } catch (Exception e) {
            Rbody = "";
        }
    }

    public void assertResponse(int status, String body){
        Assert.assertEquals(status, getStatus());
        if (body != null) {
            Assert.assertEquals(body, getRbody());
        }
    }

}
