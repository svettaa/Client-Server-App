package com.lukichova.olenyn.app.network;

import com.lukichova.olenyn.app.Exceptions.wrongDecryptException;
import com.lukichova.olenyn.app.entities.Packet;

import java.io.IOException;

public interface Network {
    void listen() throws IOException;

    Packet receive() throws Exception, wrongDecryptException;

    void connect() throws IOException;

    void send(Packet packet) throws Exception;

    void close() throws IOException;
}
