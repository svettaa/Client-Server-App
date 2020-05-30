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

    public ByteArrayOutputStream processing(InputStream serverInputStream) throws IOException {
        Integer state = 0;
        Integer wLen = 0;
        Boolean packetIncomplete = true;

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
        if(packetIncomplete)
            return null;
        return packetBytes;
    }
}
