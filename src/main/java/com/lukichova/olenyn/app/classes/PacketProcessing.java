package com.lukichova.olenyn.app.classes;

import com.lukichova.olenyn.app.entities.Message;
import com.lukichova.olenyn.app.entities.Packet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class PacketProcessing {
    public PacketProcessing() {
    }

    public byte[] processing(InputStream serverInputStream, byte maxPacketBuffer[]) throws IOException {
        serverInputStream.read(maxPacketBuffer);

        ByteBuffer byteBuffer = ByteBuffer.wrap(maxPacketBuffer);
        Integer wLen = byteBuffer.getInt(Packet.packetPartFirstLengthWithoutwLen);

        byte fullPacket1[] = byteBuffer.slice().array();
        byte fullPacket[] = new byte[Packet.packetPartFirstLength + Message.BYTES_WITHOUT_MESSAGE + wLen+4];
        for (int i = 0; i < fullPacket.length; i++) {
            fullPacket[i] = fullPacket1[i];
        }

        return fullPacket;
    }
}