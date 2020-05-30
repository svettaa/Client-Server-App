package com.lukichova.olenyn.app.entities;

import com.github.snksoft.crc.CRC;
import com.lukichova.olenyn.app.Exceptions.wrongBMagicException;
import com.lukichova.olenyn.app.Exceptions.wrongCrc1Exception;
import com.lukichova.olenyn.app.Exceptions.wrongCrc2Exception;
import com.lukichova.olenyn.app.Exceptions.wrongDecryptException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import com.google.common.primitives.UnsignedLong;

import java.net.InetAddress;
import java.nio.ByteBuffer;

@Data
public class Packet {
    public final static Byte bMagic = 0x13;

    InetAddress clientInetAddress;
    Integer clientPort;

    public final static Integer packetPartFirstLengthWithoutwLen = bMagic.BYTES + Byte.BYTES + Long.BYTES;
    public final static Integer packetPartFirstLength = packetPartFirstLengthWithoutwLen + Integer.BYTES;
    public final static Integer packetPartFirstLengthWithCRC16 = packetPartFirstLength + Short.BYTES;
    public final static Integer packetMaxSize = packetPartFirstLengthWithCRC16 + Message.BYTES_MAX_SIZE;


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


    public Packet(byte[] encodedPacket) throws Exception, wrongDecryptException {
        ByteBuffer buffer = ByteBuffer.wrap(encodedPacket);

        Byte expectedBMagic = buffer.get();

        if (!expectedBMagic.equals(bMagic))
            throw new wrongBMagicException("Unexpected bMagic");

        bSrc = buffer.get();
        bPktId = UnsignedLong.fromLongBits(buffer.getLong());
        wLen = buffer.getInt();

        Short wCrc16_1 = buffer.getShort();
        byte[] checkCRC16_1 = ByteBuffer.allocate(packetPartFirstLength)
                .put(bMagic)
                .put(bSrc)
                .putLong(bPktId.longValue())
                .putInt(wLen).array();
        if(!wCrc16_1.equals(calculateCrc16(checkCRC16_1))){
            throw new wrongCrc1Exception("Wrong CRC16_1");
        }

        bMsq = new Message();
        bMsq.setCType(buffer.getInt());
        bMsq.setBUserId(buffer.getInt());

        byte[] messageBody = new byte[wLen];
        buffer.get(messageBody);
        bMsq.setMessage(new String(messageBody));

        Short wCrc16_2 = buffer.getShort();

        byte[] checkCRC16_2 = ByteBuffer.allocate(Integer.BYTES + Integer.BYTES + wLen)
                .putInt(bMsq.getCType())
                .putInt(bMsq.getBUserId())
                .put(messageBody).array();
        if(!wCrc16_2.equals(calculateCrc16(checkCRC16_2))){
            throw new wrongCrc2Exception("Wrong CRC16_2");
        }

        bMsq.decode();
        setbMsq(bMsq);
    }

    public void setbMsq(Message bMsq) {
        this.bMsq = bMsq;
        wLen = bMsq.getMessage().length();
    }

    public byte[] toPacket() throws Exception{
        Message message = new Message();
        message.setCType(getBMsq().getCType());
        message.setBUserId(getBMsq().getBUserId());
        message.setMessage(getBMsq().getMessage());
        message.encode();



        byte[] packetPartFirst = ByteBuffer.allocate(packetPartFirstLength)
                .put(bMagic)
                .put(bSrc)
                .putLong(bPktId.longValue())
                .putInt(message.getMessage().length())
                .array();
        Short wCrc16_1 = calculateCrc16(packetPartFirst);

        Integer packetPartSecondLength = message.getMessageBytesLength();
        byte[] packetPartSecond = ByteBuffer.allocate(packetPartSecondLength)
                .put(message.toPacketPart())
                .array();

        Short wCrc16_2 = calculateCrc16(packetPartSecond);

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

       return "Packet( bPktId: " + bPktId +", " +
               "bSrc: "+ bSrc+"," +
               "wLen: " + wLen+", "+
               "Message( CType:"+ bMsq.getCType()+", BUserId: "+
                bMsq.getBUserId()+", message: " +bMsq.getMessage() + ")";

    }

}
