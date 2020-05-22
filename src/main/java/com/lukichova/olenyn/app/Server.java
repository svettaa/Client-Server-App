package com.lukichova.olenyn.app;

import com.lukichova.olenyn.app.network.Network;
import com.lukichova.olenyn.app.network.TCPNetwork;

import java.io.IOException;

public class Server {

    public static void main(String[] args) {
        try {
            Network network = new TCPNetwork();
            network.listen();

            network.receive();

            network.receive();

            network.receive();

            network.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
