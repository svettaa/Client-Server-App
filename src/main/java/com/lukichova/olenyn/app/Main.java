package com.lukichova.olenyn.app;

import com.lukichova.olenyn.app.Exceptions.wrongDecryptException;
import com.lukichova.olenyn.app.entities.Message;
import com.lukichova.olenyn.app.entities.Packet;
import com.google.common.primitives.UnsignedLong;


public class Main {

    public static void main(String[] args) throws Exception, wrongDecryptException {
        UnsignedLong unsignedLongbPktId = UnsignedLong.valueOf(Long.MAX_VALUE);
        unsignedLongbPktId = unsignedLongbPktId.plus(UnsignedLong.valueOf("2305"));

        Message testMessage = new Message(Message.cTypes.GET_PRODUCT.ordinal(), 4, "We love programming!!!");
        Packet packet = new Packet((byte) 1,unsignedLongbPktId, testMessage);
        System.out.println("-----------------------------Out packet------------------------------");
        System.out.println(packet);
        byte[] encodedPacket = packet.toPacket();
        Packet decodedPacket = new Packet(encodedPacket);
        System.out.println("-----------------------------In packet------------------------------");
        System.out.println(decodedPacket);
    }
}