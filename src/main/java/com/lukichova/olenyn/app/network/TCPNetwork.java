package com.lukichova.olenyn.app.network;


import com.lukichova.olenyn.app.Exceptions.*;
import com.lukichova.olenyn.app.classes.PacketProcessing;
import com.lukichova.olenyn.app.entities.Packet;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

public class TCPNetwork implements Network {

    Socket socket;

    OutputStream socketOutputStream;
    InputStream serverInputStream;


    public TCPNetwork(Socket socket) throws wrongConnectionException {

        this.socket = socket;
        try {
            socketOutputStream = socket.getOutputStream();
            serverInputStream = socket.getInputStream();
        } catch (IOException e) {
            throw new wrongConnectionException("Wrong TCPNetwork connection");
        }
    }

    @Override
    public void listen() throws IOException {

    }


    public Packet receive() throws IOException, closedSocketException, wrongDecryptException, wrongBMagicException, wrongCrc1Exception, wrongCrc2Exception {
        InputStream serverInputStream = socket.getInputStream();

        byte maxPacketBuffer[] = new byte[Packet.packetMaxSize];
        PacketProcessing pr = new PacketProcessing();
        byte fullPacket[] = pr.processing(serverInputStream, maxPacketBuffer);


        System.out.println("Received");
        System.out.println(Arrays.toString(fullPacket) + "\n");

        Packet packet = new Packet(fullPacket);
        System.err.println(packet.getBMsq().getMessage());

        return packet;
    }

    @Override
    public void connect(){

    }

    @Override
    public void send(Packet packet) throws IOException, wrongEcryptException {
        byte[] packetBytes;

        packetBytes = packet.toPacket();


        socketOutputStream.write(packetBytes);
        socketOutputStream.flush();

        System.out.println("Send");

    }

    @Override
    public void close() throws wrongCloseSocketException {
        try {
            socket.close();
        } catch (IOException e) {
            throw new wrongCloseSocketException();
        }
    }

}