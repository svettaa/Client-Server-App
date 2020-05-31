package com.lukichova.olenyn.app;

import com.google.common.primitives.UnsignedLong;
import com.lukichova.olenyn.app.Exceptions.requestFailed;
import com.lukichova.olenyn.app.Exceptions.unavailableServer;
import com.lukichova.olenyn.app.Exceptions.wrongConnectionException;
import com.lukichova.olenyn.app.Exceptions.wrongDecryptException;
import com.lukichova.olenyn.app.entities.Message;
import com.lukichova.olenyn.app.entities.Packet;
import com.lukichova.olenyn.app.network.Network;
import com.lukichova.olenyn.app.network.TCPNetwork;
import com.lukichova.olenyn.app.network.UDPNetwork;


import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import static com.lukichova.olenyn.app.resoures.Resoures.*;


public class Client {
    Network network;
    Socket socket;

    Client() {
    }

    public static void main(String[] args) throws Exception, wrongDecryptException {
        Message testMessage = new Message(Message.cTypes.ADD_PRODUCT.ordinal(), 1, "time");
        Packet packet = new Packet((byte) 1, UnsignedLong.ONE, testMessage);


        Client client = new Client();
        client.connect(NETWORK_PORT);
        Thread.sleep(10000);
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

        System.out.println("Client is running via " + network + " connection");
    }

    public void reconnect() throws unavailableServer, InterruptedException {

        for (int i = 0; i < 6; i++) {
            try {
                socket = new Socket(NETWORK_HOST, NETWORK_PORT);
                network = new TCPNetwork(socket);
                return;
            } catch (Exception e) {
                System.out.println("Can't connect - trying to reconnect");
                Thread.sleep(3000);
            }
        }
        throw new unavailableServer();
    }

    public Packet request(Packet packet) throws Exception, wrongDecryptException {
        try {
            if (network == null) {
                throw new wrongConnectionException("Not connected");
            }
            network.send(packet);
            return network.receive();
        } catch (SocketException | unavailableServer e) {
            reconnect();
            throw new requestFailed();
        }
    }

    public void disconnect() throws Exception {
        network.close();
        System.out.println("Client is disconnected");
    }
}
