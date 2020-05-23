package com.lukichova.olenyn.app.network;

import com.lukichova.olenyn.app.entities.Packet;
import com.lukichova.olenyn.app.entities.Message;
import com.lukichova.olenyn.app.classes.Processor;


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

    OutputStream socketOutputStream;
    InputStream serverInputStream;


    public TCPNetwork(Socket socket) throws Exception{
        this.socket = socket;
        socketOutputStream = socket.getOutputStream();
        serverInputStream = socket.getInputStream();
    }

    @Override
    public Packet receive() {
        Integer state = 0;
        Integer wLen = 0;
        Boolean packetIncomplete = true;

        try {
            ByteBuffer byteBuffer = ByteBuffer.allocate(Long.BYTES);
            ByteArrayOutputStream packetBytes = new ByteArrayOutputStream();

            byte oneByte[] = new byte[1];

            while (packetIncomplete && (serverInputStream.read(oneByte)) != -1) {
                if (Packet.bMagic.equals(oneByte[0])) {
                    state = 0;
                    byteBuffer = ByteBuffer.allocate(Packet.packetPartFirstLengthWithoutwLen - Packet.bMagic.BYTES);
                    packetBytes.reset();
                } else {
                    byteBuffer.put(oneByte);
                    switch (state) {
                        case 0:
                            if (!byteBuffer.hasRemaining()) {
                                byteBuffer = ByteBuffer.allocate(Integer.BYTES);
                                state = 1;
                            }
                            break;

                        case 1:
                            if (!byteBuffer.hasRemaining()) {
                                wLen = byteBuffer.getInt(0);
                                byteBuffer = ByteBuffer.allocate(Short.BYTES + Message.BYTES_WITHOUT_MESSAGE + wLen + Short.BYTES);
                                state = 2;
                            }
                            break;

                        case 2:
                            if (!byteBuffer.hasRemaining()) {
                                packetIncomplete = false;
                            }
                            break;
                    }
                }
                packetBytes.write(oneByte);
            }

            byte fullPacket[] = packetBytes.toByteArray();

            System.out.println("Received:");
            Packet packet = new Packet(fullPacket);
            System.out.println(packet.getBMsq().getMessage());

            return packet;
        } catch (Exception e) {
            System.err.println("Error:" + socket);
            e.printStackTrace();
            return null;
        }

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
