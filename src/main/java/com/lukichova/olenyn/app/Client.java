package com.lukichova.olenyn.app;

import com.google.common.primitives.UnsignedLong;
import com.lukichova.olenyn.app.Exceptions.wrongConnectionException;
import com.lukichova.olenyn.app.entities.Message;
import com.lukichova.olenyn.app.entities.Packet;
import com.lukichova.olenyn.app.network.Network;
import com.lukichova.olenyn.app.network.TCPNetwork;

import java.io.IOException;
import java.net.Socket;


public class Client {
    private TCPNetwork network;

    Client() {
    }

    public static void main(String[] args) throws Exception {
        Message testMessage = new Message(Message.cTypes.ADD_PRODUCT.ordinal(), 1, "time");
        Packet packet = new Packet((byte) 1, UnsignedLong.ONE, testMessage);


        Client client = new Client();
        client.connect(2305);
        client.request(packet);
        client.disconnect();

    }

    public void connect(int serverPort) throws Exception {
        network = new TCPNetwork(new Socket("localhost", serverPort));
        System.out.println("Client is connected");
    }

    public Packet request(Packet packet) throws Exception {
        if (network == null) {
            throw new wrongConnectionException("Not connected");
        }
        network.send(packet);
        return network.receive();
    }

    public void disconnect() throws Exception {
        network.close();
        System.out.println("Client is disconnected");
    }
}
