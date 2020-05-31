package com.lukichova.olenyn.app.network;

import com.lukichova.olenyn.app.Exceptions.wrongCloseSocketException;
import com.lukichova.olenyn.app.Exceptions.wrongConnectionException;
import com.lukichova.olenyn.app.Exceptions.wrongDecryptException;
import com.lukichova.olenyn.app.Exceptions.wrongUDPListenExceprion;
import com.lukichova.olenyn.app.entities.Packet;

import java.io.IOException;

public interface Network {
    void listen() throws IOException, wrongUDPListenExceprion;

    Packet receive() throws Exception, wrongDecryptException;

    void connect() throws IOException, wrongConnectionException;

    void send(Packet packet) throws Exception;

    void close() throws IOException, wrongCloseSocketException;
}
