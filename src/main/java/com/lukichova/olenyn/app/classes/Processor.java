package com.lukichova.olenyn.app.classes;
import com.google.common.primitives.UnsignedLong;
import com.lukichova.olenyn.app.entities.Message;
import com.lukichova.olenyn.app.entities.Packet;
import com.lukichova.olenyn.app.network.Network;

import java.io.IOException;

public class Processor {

    public static void process(Network network, Packet packet) {
        String message = packet.getBMsq().getMessage();

        Message answerMessage;
        if (message.equals("time")) {
            answerMessage = new Message(1, 1, "now()");
        } else {
            answerMessage = new Message(1, 1, "other");
        }
        Packet answerPacket = new Packet((byte) 1, UnsignedLong.ONE, answerMessage);

        try {
            network.send(answerPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
