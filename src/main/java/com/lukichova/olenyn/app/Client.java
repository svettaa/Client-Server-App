package com.lukichova.olenyn.app;

import com.google.common.primitives.UnsignedLong;
import com.lukichova.olenyn.app.Exceptions.*;
import com.lukichova.olenyn.app.entities.Message;
import com.lukichova.olenyn.app.entities.Packet;
import com.lukichova.olenyn.app.network.Network;
import com.lukichova.olenyn.app.network.TCPNetwork;
import com.lukichova.olenyn.app.network.UDPNetwork;


import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import static com.lukichova.olenyn.app.resoures.Resoures.*;


public class Client {
    Network network;

    Client() {
    }

    public static void main(String[] args) throws wrongClientMainException, wrongDecryptException {
        try {
            Message testMessage = new Message(Message.cTypes.ADD_PRODUCT.ordinal(), 1, "time");
            Packet packet = new Packet((byte) 1, UnsignedLong.ONE, testMessage);


            Client client = new Client();
            client.connect(NETWORK_PORT);
            client.request(packet);

            client.disconnect();
        } catch (wrongDisconnectException|wrongConnectionException|wrongClientRequestException e) {
           throw new wrongClientMainException();
        }

    }

    public void connect(int serverPort) throws wrongConnectionException {

        try {
            if (NETWORK_TYPE.toLowerCase().equals("tcp"))
                network = new TCPNetwork(new Socket(NETWORK_HOST, serverPort));
            else {
                network = new UDPNetwork();

                network.connect();
            }

            System.out.println("Client running via " + network + " connection");
        } catch (IOException e) {
           throw new wrongConnectionException("Client can not connect ");
        }

    }

    public Packet request(Packet packet) throws wrongClientRequestException, wrongDecryptException {
        try {
            if (network == null) {
                throw new wrongConnectionException("Not connected");
            }

            network.send(packet);
            return network.receive();
        }
           catch (Exception e) {
            throw new wrongClientRequestException();
        }
    }

    public void disconnect() throws wrongDisconnectException {

        try {
            network.close();
            System.out.println("Client is disconnected");
        } catch (IOException |wrongCloseSocketException e) {
            throw new wrongDisconnectException();
        }

    }
}
