package com.lukichova.olenyn.app.entities;

import com.github.snksoft.crc.CRC;
import lombok.Getter;
import lombok.ToString;
import java.nio.ByteBuffer;

@ToString
public class Packet {
    final static Byte bMagic = 0x13;

    public Packet() { }

    public Packet(Byte bSrc, Long bPktId, Message bMsq) {
        this.bSrc = bSrc;
        this.bPktId = bPktId;
        wCrc16_1 = calculateCrc16(bSrc, bPktId);

        this.bMsq = bMsq;
        wLen = bMsq.getMessage().length();
        wCrc16_2 = calculateCrc16(bMsq);
    }

    @Getter
    Byte bSrc;

    @Getter
    Long bPktId;

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
        bPktId = buffer.getLong();
        wLen = buffer.getInt();

        wCrc16_1 = buffer.getShort();
        short checkCrc1= calculateCrc16(bSrc,bPktId);
        if(wCrc16_1 != checkCrc1)
            throw new IllegalArgumentException("Different Crc1");
        bMsq = new Message();
        bMsq.setCType(buffer.getInt());
        bMsq.setBUserId(buffer.getInt());

        byte[] messageBody = new byte[wLen];
        buffer.get(messageBody);
        bMsq.setMessage(new String(messageBody));

        wCrc16_2 = buffer.getShort();
        short checkCrc2 = calculateCrc16(bMsq);
        if(wCrc16_2 != checkCrc2)
            throw new IllegalArgumentException("Different Crc2");

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

    public Short calculateCrc16(Byte bSrc, Long bPktId) {
        String packet = bMagic.toString() + bSrc.toString() + bPktId.toString();
        return (short) CRC.calculateCRC(CRC.Parameters.CRC16, packet.getBytes());
    }

    public Short calculateCrc16(Message bMsq) {
        String packet = bMsq.toString();
        return (short) CRC.calculateCRC(CRC.Parameters.CRC16, packet.getBytes());
    }

    public byte[] toPacket() {
        Message message = getBMsq();
        message.encode();

        setbMsq(message);

        Integer packetLength = bMagic.BYTES + bSrc.BYTES + bPktId.BYTES + wLen.BYTES + wCrc16_1.BYTES + message.getMessageBytesLength() + wCrc16_2.BYTES;
        return ByteBuffer.allocate(packetLength)
                .put(bMagic)
                .put(bSrc)
                .putLong(bPktId)
                .putInt(wLen)
                .putShort(wCrc16_1)
                .put(message.toPacketPart())
                .putShort(wCrc16_2)
                .array();
    }
}
