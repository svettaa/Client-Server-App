package com.lukichova.olenyn.app.network;


import com.lukichova.olenyn.app.Exceptions.*;
import com.lukichova.olenyn.app.classes.Processor;
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

    public UDPNetwork(DatagramSocket socket) throws SocketException {
        this.socket = socket;
    }

    @Override
    public Packet receive()
            throws wrongCrc1Exception, wrongBMagicException, wrongCrc2Exception,
            wrongDecryptException, IOException {
        byte maxPacketBuffer[] = new byte[Packet.packetMaxSize];

        DatagramPacket datagramPacket = new DatagramPacket(maxPacketBuffer, maxPacketBuffer.length);
        socket.receive(datagramPacket);

//        ByteBuffer byteBuffer = ByteBuffer.wrap(maxPacketBuffer);
//        Integer wLen = byteBuffer.getInt(Packet.packetPartFirstLengthWithoutwLen);
//
//        byte fullPacket1[] = byteBuffer.slice().array();
//        byte fullPacket[] = new byte[Packet.packetPartFirstLength + Message.BYTES_WITHOUT_MESSAGE + wLen + 4];
//        for (int i = 0; i < fullPacket.length; i++) {
//            fullPacket[i] = fullPacket1[i];
//        }

        byte fullPacket[] = datagramPacket.getData();

        System.out.println("Received");
        System.out.println(Arrays.toString(fullPacket) + "\n");

        Packet packet = new Packet(fullPacket);

        System.err.println(packet.getBMsq().getMessage());

        packet.setClientInetAddress(datagramPacket.getAddress());
        packet.setClientPort(datagramPacket.getPort());

        return packet;

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
        } catch (wrongEcryptException | IOException e) {
            throw new wrongSendException();
        }

    }

    @Override
    public void sendDeath() throws IOException {
        InetAddress inetAddress = InetAddress.getByName(NETWORK_HOST);
        Integer port = NETWORK_PORT;

        byte[] packetBytes = new byte[]{19, 19, 19, 19};

        DatagramPacket datagramPacket = new DatagramPacket(packetBytes, packetBytes.length, inetAddress, port);
        socket.send(datagramPacket);
    }

    @Override
    public void close() {
        socket.close();
    }
}
