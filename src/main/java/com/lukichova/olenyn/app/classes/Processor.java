package com.lukichova.olenyn.app.classes;

import com.google.common.primitives.UnsignedLong;
import com.lukichova.olenyn.app.entities.Message;
import com.lukichova.olenyn.app.entities.Packet;
import com.lukichova.olenyn.app.network.Network;

public class Processor {

    public static Packet process(Packet packet) {
        String message = packet.getBMsq().getMessage();
        Message answerMessage;

        if (message.equals("time")) {
            answerMessage = new Message(1, 1, "OK");
        } else {
            answerMessage = new Message(1, 1, "other");
        }
        Packet answerPacket = new Packet((byte) 1, packet.getBPktId(), answerMessage);
        answerPacket.setClientInetAddress(packet.getClientInetAddress());
        answerPacket.setClientPort(packet.getClientPort());

        return answerPacket;
    }
}
