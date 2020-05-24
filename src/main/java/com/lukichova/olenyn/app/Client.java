package com.lukichova.olenyn.app;

import com.google.common.primitives.UnsignedLong;
import com.lukichova.olenyn.app.entities.Message;
import com.lukichova.olenyn.app.entities.Packet;
import com.lukichova.olenyn.app.network.Network;
import com.lukichova.olenyn.app.network.TCPNetwork;

import java.io.IOException;
import java.net.Socket;


public class Client {
    private TCPNetwork network;

    Client(){}

    public static void main(String[] args) {
        Message testMessage = new Message(1, 1, "time");
        Packet packet = new Packet((byte) 1, UnsignedLong.ONE, testMessage);

/*
        Message secondTestMessage = new Message(1, 1, "notTime");
        Packet secondPacket = new Packet((byte) 1, UnsignedLong.ONE, secondTestMessage);
*/

        try {
            Client client = new Client();
            client.connect(2305);
            client.request(packet);

            client.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connect(int serverPort) throws Exception {
        network = new TCPNetwork(new Socket("localhost", serverPort));
    }

    public Packet request(Packet packet) throws Exception {
        if (network == null) {
            throw new IllegalStateException("Not connected yet");
        }
        network.send(packet);
        return network.receive();
    }

    public void disconnect() throws Exception {
        network.close();
    }
}
