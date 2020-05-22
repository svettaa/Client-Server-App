package com.lukichova.olenyn.app;

import com.lukichova.olenyn.app.classes.Processor;
import com.lukichova.olenyn.app.entities.Packet;
import com.lukichova.olenyn.app.network.Network;
import com.lukichova.olenyn.app.network.TCPNetwork;
import lombok.var;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executors;

public class Server {

    public static void main(String[] args) throws Exception {

        try (var listener = new ServerSocket(2305)) {
            System.out.println("The capitalization server is running...");
            var pool = Executors.newFixedThreadPool(20);
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
                Network network = new TCPNetwork(socket);
                Packet packet = network.receive();
                Packet answer = Processor.process(packet);
                network.send(answer);
                network.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
