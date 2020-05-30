package com.lukichova.olenyn.app.network;

import com.lukichova.olenyn.app.classes.Processor;
import com.lukichova.olenyn.app.entities.Message;
import com.lukichova.olenyn.app.entities.Packet;
import com.lukichova.olenyn.app.resoures.Resoures;
import com.lukichova.olenyn.app.utils.NetworkProperties;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
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

        //    byte fullPacket[] = byteBuffer.get(0, Packet.packetPartFirstLength + Message.BYTES_WITHOUT_MESSAGE + wLen).array();
            byte[] byteBuffer1 = byteBuffer.array();
            ByteBuffer byteBuffer2=byteBuffer.put(byteBuffer1,0, Packet.packetPartFirstLength + Message.BYTES_WITHOUT_MESSAGE + wLen);
            byte fullPacket[] = byteBuffer2.array();
            System.out.println("Received");
            System.out.println(Arrays.toString(fullPacket) + "\n");

            Packet packet = new Packet(fullPacket);
            System.err.println(packet.getBMsq().getMessage());

            packet.setClientInetAddress(datagramPacket.getAddress());
            packet.setClientPort(datagramPacket.getPort());

            return packet;
        } catch (Exception e) {
            System.err.println("Error:" + socket);
            e.printStackTrace();
        }
        return null;
    }




    public void connect() throws IOException {
        socket = new DatagramSocket();
    }

    @Override
    public void send(Packet packet) throws Exception {

        InetAddress inetAddress = packet.getClientInetAddress() != null ? packet.getClientInetAddress() : InetAddress.getByName(NETWORK_HOST);
        Integer port = packet.getClientPort() != null ? packet.getClientPort() : NETWORK_PORT;

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
