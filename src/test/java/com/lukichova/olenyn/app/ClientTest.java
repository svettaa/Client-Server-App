package com.lukichova.olenyn.app;

import com.google.common.primitives.UnsignedLong;
import com.lukichova.olenyn.app.entities.Message;
import com.lukichova.olenyn.app.entities.Packet;
import org.junit.Assert;
import org.junit.Test;

public class ClientTest {
    private Message testMessage;
    private Packet packet;
    UnsignedLong unsignedLongbPktId = UnsignedLong.valueOf(Long.MAX_VALUE);

    @Test
    public void testClient() throws Exception {
        Client client = new Client();
        client.connect(2305);
        unsignedLongbPktId = unsignedLongbPktId.plus(UnsignedLong.valueOf("2305"));
        testMessage = new Message(3, 4, "time");
        packet = new Packet((byte) 1, unsignedLongbPktId, testMessage);

        Packet response = client.request(packet);
        String message = response.getBMsq().getMessage();
        Assert.assertEquals(message, "OK");


       client.disconnect();
    }
}