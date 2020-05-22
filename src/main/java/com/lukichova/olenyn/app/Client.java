package com.lukichova.olenyn.app;

import com.google.common.primitives.UnsignedLong;
import com.lukichova.olenyn.app.entities.Message;
import com.lukichova.olenyn.app.entities.Packet;
import com.lukichova.olenyn.app.network.Network;
import com.lukichova.olenyn.app.network.TCPNetwork;

import java.io.IOException;

public class Client {

    public static void main(String[] args) {
        Message testMessage = new Message(1, 1, "time");
        Packet packet = new Packet((byte) 1, UnsignedLong.ONE, testMessage);

        Message secondTestMessage = new Message(1, 1, "notTime");
        Packet secondPacket = new Packet((byte) 1, UnsignedLong.ONE, secondTestMessage);

        try {
            Network network = new TCPNetwork();
            network.connect();

            network.send(packet);
            network.receive();

            network.send(secondPacket);
            network.receive();

            network.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
