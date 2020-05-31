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

        byte fullPacket[] = byteBuffer.slice().array();
        return fullPacket;
    }
}
