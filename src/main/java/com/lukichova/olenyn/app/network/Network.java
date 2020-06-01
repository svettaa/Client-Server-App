package com.lukichova.olenyn.app.network;

import com.lukichova.olenyn.app.Exceptions.*;
import com.lukichova.olenyn.app.entities.Packet;

import java.io.IOException;

public interface Network {
    void listen() throws IOException, wrongUDPListenException;

    Packet receive() throws wrongDecryptException, IOException, closedSocketException, wrongBMagicException, wrongCrc1Exception, wrongCrc2Exception, interruptedConnectionException;

    void connect() throws IOException, wrongConnectionException, unavailableServer, InterruptedException;

    void send(Packet packet) throws wrongSendException, IOException, wrongEcryptException;

    void close() throws wrongCloseSocketException;
}
