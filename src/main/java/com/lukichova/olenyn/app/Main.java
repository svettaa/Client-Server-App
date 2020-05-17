package com.lukichova.olenyn.app;

import com.lukichova.olenyn.app.entities.Message;
import com.lukichova.olenyn.app.entities.Packet;


public class Main {

    public static void main(String[] args) throws Exception{
        Message testMessage = new Message(3, 4, "We love programming!!!");
        Packet packet = new Packet((byte) 1, 2L, testMessage);
        System.out.println("-----------------------------Out packet------------------------------");
        System.out.println(packet);
        byte[] encodedPacket = packet.toPacket();
        Packet decodedPacket = new Packet(encodedPacket);
        System.out.println("-----------------------------In packet------------------------------");
        System.out.println(decodedPacket);
    }
}
