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
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;


import static com.lukichova.olenyn.app.resoures.Resoures.*;


public class Client {
    Network network;
    Socket socket;

    Client() {
    }

    public static final int AMOUNT_OF_TRIES = 5;

    public static void main(String[] args) {
        try {
            Message testMessage = new Message(Message.cTypes.GET_PRODUCTS.ordinal(), 1, "milk 50");
            Packet packet = new Packet((byte) 1, UnsignedLong.ONE, testMessage);


            Client client = new Client();
            client.connect(NETWORK_PORT);
            //Thread.sleep(3000);
            client.request(packet, AMOUNT_OF_TRIES);


            client.disconnect();
        } catch (wrongDisconnectException e) {
            System.out.println("Error while disconnecting");
        } catch (wrongConnectionException e) {
            System.out.println("Error while connecting");
        } catch (InterruptedException e) {
            System.out.println("Some errors occurred");
        } catch (com.lukichova.olenyn.app.Exceptions.unavailableServer unavailableServer) {
            System.out.println("Server is unavailable");
        } catch (wrongDecryptException e) {
            System.out.println("Errors in decryption");
        } catch (wrongBMagicException e) {
            System.out.println("Wrong bMagic");
        } catch (wrongCrc1Exception e) {
            System.out.println("Wrong CRC1");
        } catch (wrongCrc2Exception e) {
            System.out.println("Wrong CRC2");
        } catch (IOException e) {
            System.out.println("IOException: socket was closed");
        } catch (interruptedConnectionException e) {
            System.out.println("Your connection was interrupted");
        } catch (wrongSendException e) {
            System.out.println("Errors while sending");
        } catch (requestFailed e) {
            System.out.println("Errors in request()");
        } catch (wrongEcryptException e) {
            System.out.println("Errors in encryption");
        }
    }

    public void connect(int serverPort) throws wrongConnectionException, unavailableServer, InterruptedException, IOException {

        if (NETWORK_TYPE.toLowerCase().equals("tcp")) {
            try {
                network = new TCPNetwork(new Socket(NETWORK_HOST, serverPort));
            } catch (IOException e) {
                reconnect();
            }
        } else {
            DatagramSocket datagramSocket = new DatagramSocket();
            datagramSocket.setSoTimeout(2000);
            network = new UDPNetwork(datagramSocket);
        }

        System.out.println("Client is running via " + network + " connection");

    }

    public void reconnect() throws unavailableServer, InterruptedException {

        for (int i = 0; i < 5; i++) {
            try {
                socket = new Socket(NETWORK_HOST, NETWORK_PORT);
                network = new TCPNetwork(socket);
                return;
            } catch (Exception e) {
                System.out.println("Can't connect - trying to reconnect");
                Thread.sleep(1000);
            }
        }
        throw new unavailableServer();
    }

    public void requestDeathPacket() throws IOException {
        network.sendDeath();
        System.out.println("sent death");
    }

    public Packet request(Packet packet, int k)
            throws wrongDecryptException, wrongSendException, wrongConnectionException,
            requestFailed, unavailableServer, InterruptedException, wrongCrc2Exception,
            wrongBMagicException, interruptedConnectionException, wrongCrc1Exception,
            IOException, wrongEcryptException {

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
            return answerPacketOne;

        } catch (closedSocketException | SocketException e) {
            reconnect();
            request(packet, k);


        } catch (SocketTimeoutException e) {
            k--;
            if (k == 0) {
                throw new unavailableServer();
            }
            System.out.println("Retrying");
            return request(packet, k);
        }
        return null;
    }


    public void disconnect() throws wrongDisconnectException {

        try {
            network.close();
            System.out.println("Client is disconnected");
        } catch (wrongCloseSocketException e) {
            throw new wrongDisconnectException();
        }

    }
}
