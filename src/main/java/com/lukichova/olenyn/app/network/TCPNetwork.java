package com.lukichova.olenyn.app.network;


import com.lukichova.olenyn.app.Exceptions.interruptedConnectionException;
import com.lukichova.olenyn.app.Exceptions.wrongDecryptException;
import com.lukichova.olenyn.app.Exceptions.wrongEcryptException;
import com.lukichova.olenyn.app.classes.PacketProcessing;
import com.lukichova.olenyn.app.classes.Processor;
import com.lukichova.olenyn.app.entities.Packet;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static com.lukichova.olenyn.app.resoures.Resoures.NETWORK_HOST;
import static com.lukichova.olenyn.app.resoures.Resoures.NETWORK_PORT;

public class TCPNetwork implements Network {

    Socket socket;
    ServerSocket serverSocket;

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
    public Packet receive() throws Exception, wrongDecryptException {
        InputStream serverInputStream = socket.getInputStream();

        byte maxPacketBuffer[] = new byte[Packet.packetMaxSize];
        PacketProcessing pr = new PacketProcessing();
        byte fullPacket[] = pr.processing(serverInputStream, maxPacketBuffer);


        System.out.println("Received");
        System.out.println(Arrays.toString(fullPacket) + "\n");

        Packet packet = new Packet(fullPacket);
        System.err.println(packet.getBMsq().getMessage());

        if (serverSocket != null) {
            Processor.process(packet);
            send(packet);
        } else {
            return packet;
        }
        return null;
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
