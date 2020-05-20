package com.lukichova.olenyn.app;

import com.lukichova.olenyn.app.entities.Message;
import com.lukichova.olenyn.app.entities.Packet;
import com.google.common.primitives.UnsignedLong;


public class Main {

    public static void main(String[] args) throws Exception{
        UnsignedLong moreThanLongbPktId = UnsignedLong.valueOf(Long.MAX_VALUE);
        moreThanLongbPktId = moreThanLongbPktId.plus(UnsignedLong.valueOf("2305"));

      //  UnsignedLong l1 = UnsignedLong.parseInt("12345678901234567890");
        Message testMessage = new Message(3, 4, "We love programming!!!");
        Packet packet = new Packet((byte) 1,moreThanLongbPktId, testMessage);
        System.out.println("-----------------------------Out packet------------------------------");
        System.out.println(packet);
        byte[] encodedPacket = packet.toPacket();
        Packet decodedPacket = new Packet(encodedPacket);
        System.out.println("-----------------------------In packet------------------------------");
        System.out.println(decodedPacket);
    }
}
