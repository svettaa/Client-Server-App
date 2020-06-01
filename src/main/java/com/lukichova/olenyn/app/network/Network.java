package com.lukichova.olenyn.app.network;

import com.lukichova.olenyn.app.Exceptions.*;
import com.lukichova.olenyn.app.entities.Packet;

import java.io.IOException;

public interface Network {
    Packet receive() throws wrongDecryptException, IOException, closedSocketException, wrongBMagicException, wrongCrc1Exception, wrongCrc2Exception, interruptedConnectionException;

    void send(Packet packet) throws wrongSendException, IOException, wrongEcryptException;

    void sendDeath() throws IOException;

    void close() throws wrongCloseSocketException;
}
