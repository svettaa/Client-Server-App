package com.lukichova.olenyn.app.classes;
import com.google.common.primitives.UnsignedLong;
import com.lukichova.olenyn.app.entities.Message;
import com.lukichova.olenyn.app.entities.Packet;

public class Processor {

    public static Packet process(Packet packet) {
        String message = packet.getBMsq().getMessage();

        Message answerMessage;
        if (message.equals("time")) {
            answerMessage = new Message(1, 1, "ok");
        } else {
            answerMessage = new Message(1, 1, "other");
        }
        Packet answerPacket = new Packet((byte) 1, UnsignedLong.ONE, answerMessage);

        return answerPacket;
    }
}
