package com.lukichova.olenyn.app;

import com.google.common.primitives.UnsignedLong;
import com.lukichova.olenyn.app.Exceptions.closedSocketException;
import com.lukichova.olenyn.app.Exceptions.unavailableServer;
import com.lukichova.olenyn.app.Exceptions.wrongConnectionException;
import com.lukichova.olenyn.app.Exceptions.wrongDecryptException;
import com.lukichova.olenyn.app.entities.Message;
import com.lukichova.olenyn.app.entities.Packet;
import org.junit.Assert;
import org.junit.Test;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import static com.lukichova.olenyn.app.resoures.Resoures.NETWORK_PORT;

public class TestClient {
    public static final int AMOUNT_OF_TRIES = 5;
    private Message testMessage;
    private Packet packet;
    UnsignedLong unsignedLongbPktId = UnsignedLong.valueOf(Long.MAX_VALUE);

    @Test
    public void testClient() throws Exception, wrongDecryptException {
        Client client = new Client();
        client.connect(NETWORK_PORT);
        unsignedLongbPktId = unsignedLongbPktId.plus(UnsignedLong.valueOf("2305"));
        testMessage = new Message(3, 4, "time");
        packet = new Packet((byte) 1, unsignedLongbPktId, testMessage);

        for (int i = 0; i < 10; i++) {
            Packet response = client.request(packet, AMOUNT_OF_TRIES);
            String message = response.getBMsq().getMessage();
            Assert.assertEquals(message, "OK");
        }

        client.disconnect();
    }

    @Test(expected = wrongConnectionException.class)
    public void testWrongConnection() throws Exception, wrongDecryptException {
        Client client = new Client();
        unsignedLongbPktId = unsignedLongbPktId.plus(UnsignedLong.valueOf("2305"));
        testMessage = new Message(3, 4, "time");
        packet = new Packet((byte) 1, unsignedLongbPktId, testMessage);
        client.request(packet, AMOUNT_OF_TRIES);
    }

    @Test
    public void testConcurrency() throws Exception {
        final int packetsInside = 10;
        final int threads = 15;
        AtomicInteger packetsDoneActual = new AtomicInteger(0);
        long packetsDoneExpected = threads * packetsInside;

        testMessage = new Message(3, 4, "time");
        packet = new Packet((byte) 1, unsignedLongbPktId, testMessage);

        Thread[] array = new Thread[threads];
        for (int i = 0; i < threads; i++) {
            array[i] = new Thread(() -> {
                try {
                    Client client = new Client();
                    client.connect(NETWORK_PORT);
                    for (int j = 0; j < packetsInside; j++) {
                        Packet answer = client.request(packet, AMOUNT_OF_TRIES);
                        String message = answer.getBMsq().getMessage();
                        Assert.assertEquals(message, "OK");
                        packetsDoneActual.incrementAndGet();
                    }
                    client.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        for (int i = 0; i < threads; i++) {
            array[i].start();
        }
        for (int i = 0; i < threads; i++) {
            array[i].join();
        }
        Assert.assertEquals(packetsDoneExpected, packetsDoneActual.longValue());
    }

    @Test
    public void testDeathPackets() throws Exception, wrongDecryptException {
        Client client = new Client();
        client.connect(NETWORK_PORT);
        unsignedLongbPktId = unsignedLongbPktId.plus(UnsignedLong.valueOf("2305"));
        testMessage = new Message(4, 4, "time");
        packet = new Packet((byte) 1, unsignedLongbPktId, testMessage);

        Packet response;

        response = client.request(packet, AMOUNT_OF_TRIES);
        Assert.assertEquals(response.getBMsq().getMessage(), "OK");

        packet.setBPktId(UnsignedLong.valueOf(0x13));
        response = client.request(packet, AMOUNT_OF_TRIES);
        Assert.assertEquals(response.getBMsq().getMessage(), "OK");

        client.requestDeathPacket();

        response = client.request(packet, AMOUNT_OF_TRIES);
        Assert.assertEquals(response.getBMsq().getMessage(), "OK");

        client.disconnect();
    }
}