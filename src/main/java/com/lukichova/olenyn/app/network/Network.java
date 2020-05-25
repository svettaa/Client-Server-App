package com.lukichova.olenyn.app.network;

import com.lukichova.olenyn.app.entities.Packet;

import java.io.IOException;

public interface Network {
    Packet receive() throws Exception;

    void send(Packet packet) throws IOException;

    void close() throws IOException;
}
