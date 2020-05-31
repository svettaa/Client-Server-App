package com.lukichova.olenyn.app.network;


import com.lukichova.olenyn.app.Exceptions.*;
import com.lukichova.olenyn.app.entities.Message;
import com.lukichova.olenyn.app.entities.Packet;
import com.lukichova.olenyn.app.utils.NetworkProperties;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static com.lukichova.olenyn.app.resoures.Resoures.NETWORK_HOST;
import static com.lukichova.olenyn.app.resoures.Resoures.NETWORK_PORT;


public class UDPNetwork implements Network {
    private DatagramSocket socket;
    private Boolean isServer = false;

    public UDPNetwork(DatagramSocket socket){
        this.socket = socket;
    }

    public UDPNetwork(){
    }


    public void listen() throws wrongUDPListenExceprion {
        try{String portProperty = NetworkProperties.getProperty("port");
        if (portProperty == null)
            portProperty = "2305";

        socket = new DatagramSocket(Integer.parseInt(portProperty));
        isServer = true;
        } catch (SocketException e) {
            throw new wrongUDPListenExceprion();
        }
    }

    @Override
    public Packet receive() throws interruptedConnectionException {
        try {
            byte maxPacketBuffer[] = new byte[Packet.packetMaxSize];

            DatagramPacket datagramPacket = new DatagramPacket(maxPacketBuffer, maxPacketBuffer.length);
            socket.receive(datagramPacket);

            ByteBuffer byteBuffer = ByteBuffer.wrap(maxPacketBuffer);
            Integer wLen = byteBuffer.getInt(Packet.packetPartFirstLengthWithoutwLen);

           byte fullPacket1[] = byteBuffer.slice().array();
            byte fullPacket[] = new byte[Packet.packetPartFirstLength + Message.BYTES_WITHOUT_MESSAGE + wLen+4];
            for (int i = 0; i < fullPacket.length; i++) {
                fullPacket[i] = fullPacket1[i];
            }

            System.out.println("Received");
            System.out.println(Arrays.toString(fullPacket) + "\n");

            Packet packet = new Packet(fullPacket1);
            System.err.println(packet.getBMsq().getMessage());

            packet.setClientInetAddress(datagramPacket.getAddress());
            packet.setClientPort(datagramPacket.getPort());

            return packet;
        } catch (Exception e) {
            throw new interruptedConnectionException();

        } catch (wrongDecryptException e) {
            e.printStackTrace();
        }
        return null;
    }




    public void connect() throws wrongConnectionException {

        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            throw new wrongConnectionException("Wrong UDP connection");
        }
    }

    @Override
    public void send(Packet packet) throws wrongSendException {
try {
    InetAddress inetAddress = packet.getClientInetAddress() != null ? packet.getClientInetAddress() : InetAddress.getByName(NETWORK_HOST);
    Integer port = packet.getClientPort() != null ? packet.getClientPort() : NETWORK_PORT;

    byte[] packetBytes = packet.toPacket();

    DatagramPacket datagramPacket = new DatagramPacket(packetBytes, packetBytes.length, inetAddress, port);
    socket.send(datagramPacket);

    System.out.println("Send");
    System.out.println(Arrays.toString(packetBytes) + "\n");
} catch ( Exception e) {

   throw new wrongSendException();
}

    }

    @Override
    public void close() {
        socket.close();
    }
}
