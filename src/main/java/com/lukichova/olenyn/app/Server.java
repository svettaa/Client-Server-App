package com.lukichova.olenyn.app;

import com.lukichova.olenyn.app.Exceptions.*;
import com.lukichova.olenyn.app.classes.Processor;
import com.lukichova.olenyn.app.entities.Packet;
import com.lukichova.olenyn.app.network.Network;
import com.lukichova.olenyn.app.network.TCPNetwork;
import com.lukichova.olenyn.app.network.UDPNetwork;


import javax.sound.sampled.Port;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.lukichova.olenyn.app.resoures.Resoures.*;

public class Server {

    private static ExecutorService processPool = Executors.newFixedThreadPool(10);

    public static void main(String[] args){
        try {
            if (NETWORK_TYPE.toLowerCase().equals("tcp")) {
                try (ServerSocket listener = new ServerSocket(NETWORK_PORT)) {
                    System.out.println("Server is running...");
                    ExecutorService pool = Executors.newFixedThreadPool(15);
                    while (true) {
                        pool.execute(new Server.Listener(listener.accept()));
                    }
                }
            } else {
                DatagramSocket socket = new DatagramSocket(NETWORK_PORT);
                boolean running = true;
                UDPNetwork network = new UDPNetwork(socket);
                System.out.println("Server is running via " + network + " connection");
                while (running) {
                    Packet incoming = network.receive();
                    processPool.execute(() -> {
                        try {
                            network.send(incoming);
                        } catch (wrongSendException e) {
                            System.out.println("Errors while sending");
                        }
                    });
                }
                network.close();
            }
        } catch (IOException | interruptedConnectionException e) {
            System.out.println("");
        } catch (wrongCrc2Exception e) {
            e.printStackTrace();
        } catch (wrongCrc1Exception e) {
            e.printStackTrace();
        } catch (wrongBMagicException e) {
            e.printStackTrace();
        } catch (wrongDecryptException e) {
            e.printStackTrace();
        }

    }

    private static class Listener implements Runnable {
        private Socket socket;

        Listener(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                System.out.println("Connection opened");

                Network network;

                if (NETWORK_TYPE.toLowerCase().equals("tcp")) {

                    network = new TCPNetwork(socket);

                } else
                    network = new UDPNetwork();

                System.out.println("Server is running via " + network + " connection");


                while (true) {

                    try {
                        Packet packet = null;
                        packet = network.receive();
                        Packet finalPacket = packet;
                        processPool.execute(() -> {
                            Packet answer = Processor.process(finalPacket);
                            try {
                                network.send(answer);
                            } catch (Exception e) {
                                System.out.println("Errors while sending");
                            }
                        });
                    } catch (closedSocketException e) {
                        break;
                    } catch (SocketException e) {
                        System.out.println("ERROR: Client is unavailable");
                        break;
                    }
                }
                network.close();
                System.out.println("Connection closed");

            } catch (wrongConnectionException e) {
                System.out.println("Wrong TCP connection");
            } catch (wrongDecryptException e) {
                System.out.println("Errors in decryption");
            } catch (IOException e) {
                System.out.println("IOException: socket was closed");
            } catch (wrongBMagicException e) {
                System.out.println("Wrong bMagic");
            } catch (wrongCrc1Exception e) {
                System.out.println("Wrong CRC1");
            } catch (wrongCrc2Exception e) {
                System.out.println("Wrong CRC2");
            } catch (wrongCloseSocketException e) {
                System.out.println("Error while closing");
            } catch (interruptedConnectionException e) {
                System.out.println("Your connection was interrupted");
            }
        }
    }
}