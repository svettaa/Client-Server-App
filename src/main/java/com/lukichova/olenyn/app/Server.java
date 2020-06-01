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

    public static void main(String[] args) throws unavailableClient {
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

                if (NETWORK_TYPE.toLowerCase().equals("tcp"))
                    network = new TCPNetwork(socket);
                else
                    network = new UDPNetwork();

                System.out.println("Server is running via " + network + " connection");


                while (true) {

                    try {
                        Packet packet = network.receive();
                        processPool.execute(() -> {
                            Packet answer = Processor.process(packet);
                            try {
                                network.send(answer);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    } catch (closedSocketException e) {
                        break;
                    } catch (SocketException e) {
                        System.out.println("Client is unavailable");
                        break;
                    }

                }
                network.close();
                System.out.println("Connection closed");
            } catch (Exception e) {
                e.printStackTrace();
            } catch (wrongDecryptException e) {
                e.printStackTrace();
            }
        }
    }
}