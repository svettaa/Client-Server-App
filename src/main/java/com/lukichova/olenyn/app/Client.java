package com.lukichova.olenyn.app;

import com.google.common.primitives.UnsignedLong;
import com.lukichova.olenyn.app.Exceptions.wrongConnectionException;
import com.lukichova.olenyn.app.entities.Message;
import com.lukichova.olenyn.app.entities.Packet;
import com.lukichova.olenyn.app.network.Network;
import com.lukichova.olenyn.app.network.TCPNetwork;
import com.lukichova.olenyn.app.network.UDPNetwork;


import java.net.Socket;

import static com.lukichova.olenyn.app.resoures.Resoures.*;


public class Client {
    Network network;

    Client() {
    }

    public static void main(String[] args) throws Exception {
        Message testMessage = new Message(Message.cTypes.ADD_PRODUCT.ordinal(), 1, "time");
        Packet packet = new Packet((byte) 1, UnsignedLong.ONE, testMessage);


        Client client = new Client();
        client.connect(NETWORK_PORT);
        client.request(packet);
        client.disconnect();

    }

    public void connect(int serverPort) throws Exception {

        if (NETWORK_TYPE.toLowerCase().equals("tcp"))
            network = new TCPNetwork(new Socket(NETWORK_HOST, NETWORK_PORT));
        else {
            network = new UDPNetwork();
            network.connect();
        }

        System.out.println("Client running via " + network + " connection");
    }

    public Packet request(Packet packet) throws Exception {
        if (network == null) {
            throw new wrongConnectionException("Not connected");
        }
        try
        network.send(packet);
        return network.receive();
    }

    public void disconnect() throws Exception {
        network.close();
        System.out.println("Client is disconnected");
    }
}
