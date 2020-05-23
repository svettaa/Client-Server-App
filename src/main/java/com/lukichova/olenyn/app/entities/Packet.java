package com.lukichova.olenyn.app.entities;

import com.github.snksoft.crc.CRC;
import lombok.Getter;
import com.google.common.primitives.UnsignedLong;

import java.nio.ByteBuffer;

//@ToString
public class Packet {
    public final static Byte bMagic = 0x13;

    public final static Integer packetPartFirstLengthWithoutwLen = bMagic.BYTES + Byte.BYTES + Long.BYTES;
    public final static Integer packetPartFirstLength = packetPartFirstLengthWithoutwLen + Integer.BYTES;
    public final static Integer packetPartFirstLengthWithCRC16 = packetPartFirstLength + Short.BYTES;
    public byte[] packetPartFirst;
    public byte[] packetPartSecond;

    public Packet() { }



    public Packet(Byte bSrc, UnsignedLong bPktId, Message bMsq) {
        this.bSrc = bSrc;
        this.bPktId = bPktId;

        this.bMsq = bMsq;
        wLen = bMsq.getMessage().length();
    }


    @Getter
    UnsignedLong bPktId;

    @Getter
    Byte bSrc;



    @Getter
    Integer wLen;

    @Getter
    Message bMsq;

    public Packet(byte[] encodedPacket) throws Exception {
        ByteBuffer buffer = ByteBuffer.wrap(encodedPacket);

        Byte expectedBMagic = buffer.get();
        if (!expectedBMagic.equals(bMagic))
            throw new IllegalArgumentException("Unexpected bMagic");

        bSrc = buffer.get();
        bPktId = UnsignedLong.fromLongBits(buffer.getLong());
        wLen = buffer.getInt();

        wCrc16_1 = buffer.getShort();
        bMsq = new Message();
        bMsq.setCType(buffer.getInt());
        bMsq.setBUserId(buffer.getInt());

        byte[] messageBody = new byte[wLen];
        buffer.get(messageBody);
        bMsq.setMessage(new String(messageBody));

        wCrc16_2 = buffer.getShort();

        bMsq.decode();
        setbMsq(bMsq);
    }

    public void setbMsq(Message bMsq) {
        this.bMsq = bMsq;
        wLen = bMsq.getMessage().length();
        String packet = bMsq.toString();
        wCrc16_2 = (short) CRC.calculateCRC(CRC.Parameters.CRC16, packet.getBytes());
    }

    @Getter
    Short wCrc16_1;

    @Getter
    Short wCrc16_2;



    public byte[] toPacket() {
        Message message = getBMsq();
        try {
            message.encode();
        } catch (Exception e) {
            e.printStackTrace();
        }

        setbMsq(message);

        packetPartFirst = ByteBuffer.allocate(packetPartFirstLength)
                .put(bMagic)
                .put(bSrc)
                .putLong(bPktId.longValue())
                .putInt(wLen)
                .array();
        wCrc16_1 = calculateCrc16(packetPartFirst);

        Integer packetPartSecondLength = message.getMessageBytesLength();
        packetPartSecond = ByteBuffer.allocate(packetPartSecondLength)
                .put(message.toPacketPart())
                .array();

        wCrc16_2 = calculateCrc16(packetPartSecond);

        Integer packetLength = packetPartFirstLength + wCrc16_1.BYTES + packetPartSecondLength + wCrc16_2.BYTES;

        return ByteBuffer.allocate(packetLength)
                .put(packetPartFirst)
                .putShort(wCrc16_1)
                .put(packetPartSecond)
                .putShort(wCrc16_2).array();
    }

    public static Short calculateCrc16(byte[] packetPart) {
        return (short) CRC.calculateCRC(CRC.Parameters.CRC16, packetPart);
    }
    @Override
    public String toString() {
       return "Packet( bPktId: " + bPktId +", bSrc: "+ bSrc+",wLen: " + wLen+", "+ "Message( CType:"+ bMsq.getCType()+", BUserId: "+
                bMsq.getBUserId()+", message: " +bMsq.getMessage() + ")"+ "wCrc16_1:" + wCrc16_1+", wCrc16_2: " +wCrc16_2 + ")";

    }

}
