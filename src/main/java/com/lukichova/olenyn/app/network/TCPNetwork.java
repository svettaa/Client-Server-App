package com.lukichova.olenyn.app.network;

import com.lukichova.olenyn.app.Exceptions.wrongReceiveException;
import com.lukichova.olenyn.app.classes.PacketProcessing;
import com.lukichova.olenyn.app.entities.Packet;
import com.lukichova.olenyn.app.entities.Message;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class TCPNetwork implements Network {

    Socket socket;

    OutputStream socketOutputStream;
    InputStream serverInputStream;


    public TCPNetwork(Socket socket) throws Exception{
        this.socket = socket;
        socketOutputStream = socket.getOutputStream();
        serverInputStream = socket.getInputStream();
    }

    @Override
    public Packet receive() throws Exception {

        PacketProcessing pr =new PacketProcessing();
        ByteArrayOutputStream  packetBytes= pr.processing(serverInputStream);
        byte fullPacket[] = packetBytes.toByteArray();
        System.out.println("Received:");
        Packet packet = new Packet(fullPacket);
        System.out.println(packet.getBMsq().getMessage());
        return packet;

    }

    @Override
    public void send(Packet packet) throws IOException{

        byte[] packetBytes = packet.toPacket();

        socketOutputStream.write(packetBytes);
        socketOutputStream.flush();

        System.out.println("Send");
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

}
