package com.lukichova.olenyn.app;

import com.google.common.primitives.UnsignedLong;
import com.lukichova.olenyn.app.Exceptions.unavailableServer;
import com.lukichova.olenyn.app.entities.Message;
import com.lukichova.olenyn.app.entities.Packet;
import org.junit.Test;

import static com.lukichova.olenyn.app.Client.AMOUNT_OF_TRIES;
import static com.lukichova.olenyn.app.resoures.Resoures.NETWORK_PORT;

public class TestWithoutServer {
    @Test(expected = unavailableServer.class)
    public void catchUnavailableServer() throws Exception {
        UnsignedLong unsignedLongbPktId = UnsignedLong.valueOf(Long.MAX_VALUE);
        Message testMessage = new Message(3, 4, "time");
        Packet packet = new Packet((byte) 1, unsignedLongbPktId, testMessage);
        Client client = new Client();
        client.connect(NETWORK_PORT);
        Thread.sleep(3000);
        client.request(packet, AMOUNT_OF_TRIES);
    }
}
