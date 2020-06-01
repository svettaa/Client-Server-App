package com.lukichova.olenyn.app;

import com.google.common.primitives.UnsignedLong;
import com.lukichova.olenyn.app.Exceptions.*;
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
import java.net.SocketTimeoutException;


import static com.lukichova.olenyn.app.resoures.Resoures.*;


public class Client {
    Network network;
    Socket socket;

    Client() {
    }

    public static void main(String[] args) {
        try {
            Message testMessage = new Message(Message.cTypes.ADD_PRODUCT.ordinal(), 1, "time");
            Packet packet = new Packet((byte) 1, UnsignedLong.ONE, testMessage);


            Client client = new Client();
            client.connect(NETWORK_PORT);
            Thread.sleep(3000);
            client.request(packet);


            client.disconnect();
        } catch (wrongDisconnectException | wrongConnectionException | wrongClientRequestException | InterruptedException e) {
            System.out.println("Some errors occurred");
        } catch (com.lukichova.olenyn.app.Exceptions.unavailableServer unavailableServer) {
            System.out.println("Server is unavailable");
        } catch (Exception e) {
            e.printStackTrace();
        } 

    }

    public void connect(int serverPort) throws wrongConnectionException, unavailableServer, InterruptedException, IOException {

        try {
            if (NETWORK_TYPE.toLowerCase().equals("tcp"))
                network = new TCPNetwork(new Socket(NETWORK_HOST, serverPort));
            else {
                network = new UDPNetwork();
                network.connect();
            }

            System.out.println("Client is running via " + network + " connection");
        } catch (IOException e) {
            network.connect();
        }
    }

    public void reconnect() throws unavailableServer, InterruptedException {

        for (int i = 0; i < 5; i++) {
            try {
                socket = new Socket(NETWORK_HOST, NETWORK_PORT);
                network = new TCPNetwork(socket);
                return;
            } catch (Exception e) {
                System.out.println("Can't connect - trying to reconnect");
                Thread.sleep(2000);
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

            Packet answerPacketOne = network.receive();
            if (answerPacketOne.getBPktId().equals(packet.getBPktId()))
                System.out.println("CORRECT PACKET RESPONSE");
            else
                System.out.println("WRONG PACKET RESPONSE");
        } catch (closedSocketException | SocketException e) {
            reconnect();
            throw new requestFailed();
        }
        catch (SocketTimeoutException e){
        System.out.println("Need to resend");
    }
        return packet;
    }

    public void disconnect() throws wrongDisconnectException {

        try {
            network.close();
            System.out.println("Client is disconnected");
        } catch (IOException | wrongCloseSocketException e) {
            throw new wrongDisconnectException();
        }

    }
}
