package com.lukichova.olenyn.app.network;


import com.lukichova.olenyn.app.Exceptions.wrongDecryptException;
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
    public Packet receive() throws IOException {
        InputStream serverInputStream = socket.getInputStream();

        try {
            byte maxPacketBuffer[] = new byte[Packet.packetMaxSize];

            serverInputStream.read(maxPacketBuffer);

            ByteBuffer byteBuffer = ByteBuffer.wrap(maxPacketBuffer);
            Integer wLen = byteBuffer.getInt(Packet.packetPartFirstLengthWithoutwLen);

            byte fullPacket[] = byteBuffer.slice().array();

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
        } catch (Exception | wrongDecryptException e) {
            System.err.println("Error:" + socket);
            e.printStackTrace();
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
