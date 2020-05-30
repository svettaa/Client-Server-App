package com.lukichova.olenyn.app.network;

import com.lukichova.olenyn.app.classes.PacketProcessing;
import com.lukichova.olenyn.app.entities.Packet;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TCPNetwork implements Network {

    Socket socket;

    OutputStream socketOutputStream;
    InputStream serverInputStream;


    public TCPNetwork(Socket socket) throws Exception {
        this.socket = socket;
        socketOutputStream = socket.getOutputStream();
        serverInputStream = socket.getInputStream();
    }

    @Override
    public void listen() throws IOException {

    }

    @Override
    public Packet receive() throws IOException {
        PacketProcessing pr = new PacketProcessing();
        ByteArrayOutputStream packetBytes = pr.processing(serverInputStream);
        if(packetBytes == null)
            return null;
        byte fullPacket[] = packetBytes.toByteArray();
        System.out.println("Received:");
        Packet packet = new Packet(fullPacket);
        System.out.println(packet.getBMsq().getMessage());
        return packet;
    }

    @Override
    public void connect() throws IOException {

    }

    @Override
    public void send(Packet packet) throws Exception {

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
