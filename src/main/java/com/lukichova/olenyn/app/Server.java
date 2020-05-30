package com.lukichova.olenyn.app;

import com.lukichova.olenyn.app.classes.Processor;
import com.lukichova.olenyn.app.entities.Packet;
import com.lukichova.olenyn.app.network.Network;
import com.lukichova.olenyn.app.network.TCPNetwork;
import com.lukichova.olenyn.app.network.UDPNetwork;
import com.lukichova.olenyn.app.utils.NetworkProperties;
import lombok.var;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static void main(String[] args) throws Exception {

        try (ServerSocket listener = new ServerSocket(2305)) {
            ExecutorService pool = Executors.newFixedThreadPool(10);
            while (true) {
                pool.execute(new Server.Listener(listener.accept()));
            }
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
                String networkType = NetworkProperties.getProperty("type");
                Network network;

                if (networkType.toLowerCase().equals("tcp"))
                    network = new TCPNetwork(socket);
                else
                    network = new UDPNetwork();

                System.out.println("Server is running via " + network + " connection");

                while(true) {
                    Packet packet = network.receive();
                    if(packet == null)
                        break;
                    Packet answer = Processor.process(packet);
                    network.send(answer);
                }

                network.close();
                System.out.println("Connection closed");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
