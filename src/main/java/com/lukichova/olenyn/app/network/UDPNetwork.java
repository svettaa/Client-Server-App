
package com.lukichova.olenyn.app.network;

import com.lukichova.olenyn.app.classes.Processor;
import com.lukichova.olenyn.app.entities.Message;
import com.lukichova.olenyn.app.entities.Packet;
import com.lukichova.olenyn.app.utils.NetworkProperties;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class UDPNetwork implements Network {
    private DatagramSocket socket;
    private Boolean isServer = false;

    @Override
    public void listen() throws IOException {
        String portProperty = NetworkProperties.getProperty("port");
        if (portProperty == null)
            portProperty = "2305";

        socket = new DatagramSocket(Integer.parseInt(portProperty));
        isServer = true;
    }

    @Override
    public Packet receive() {
        try {
            byte maxPacketBuffer[] = new byte[Packet.packetMaxSize];

            DatagramPacket datagramPacket = new DatagramPacket(maxPacketBuffer, maxPacketBuffer.length);
            socket.receive(datagramPacket);

            ByteBuffer byteBuffer = ByteBuffer.wrap(maxPacketBuffer);
            Integer wLen = byteBuffer.getInt(Packet.packetPartFirstLengthWithoutwLen);

            byte fullPacket[] = byteBuffer.slice(0, Packet.packetPartFirstLength + Message.BYTES_WITHOUT_MESSAGE + wLen).array();

            System.out.println("Received");
            System.out.println(Arrays.toString(fullPacket) + "\n");

            Packet packet = new Packet(fullPacket);
            System.err.println(packet.getBMsq().getMessage());

            packet.setClientInetAddress(datagramPacket.getAddress());
            packet.setClientPort(datagramPacket.getPort());

            if (isServer)
                Processor.process( packet);
            else
                return packet;
        } catch (Exception e) {
            System.err.println("Error:" + socket);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void connect() throws IOException {
        socket = new DatagramSocket();
    }

    @Override
    public void send(Packet packet) throws IOException {
        String hostProperty = NetworkProperties.getProperty("host");
        if (hostProperty == null)
            hostProperty = "localhost";

        String portProperty = NetworkProperties.getProperty("port");
        if (portProperty == null)
            portProperty = "2305";

        InetAddress inetAddress = packet.getClientInetAddress() != null ? packet.getClientInetAddress() : InetAddress.getByName(hostProperty);
        Integer port = packet.getClientPort() != null ? packet.getClientPort() : Integer.parseInt(portProperty);

        byte[] packetBytes = packet.toPacket();

        DatagramPacket datagramPacket = new DatagramPacket(packetBytes, packetBytes.length, inetAddress, port);
        socket.send(datagramPacket);

        System.out.println("Send");
        System.out.println(Arrays.toString(packetBytes) + "\n");
    }

    @Override
    public void close() {
        socket.close();
    }
}
