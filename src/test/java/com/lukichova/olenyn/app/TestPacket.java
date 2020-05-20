package com.lukichova.olenyn.app;

import com.google.common.primitives.UnsignedLong;
import com.lukichova.olenyn.app.entities.Message;
import com.lukichova.olenyn.app.entities.Packet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestPacket {
    private Message testMessage;
    private Packet packet;
    UnsignedLong unsignedLongbPktId = UnsignedLong.valueOf(Long.MAX_VALUE);


    @Before
    public void before_tests() throws Exception{
        unsignedLongbPktId = unsignedLongbPktId.plus(UnsignedLong.valueOf("17"));
        testMessage = new Message(3, 4, "test");
        packet = new Packet((byte) 1, unsignedLongbPktId, testMessage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_wrongMagic() throws Exception {
        byte[] encodedPacket = packet.toPacket();
        encodedPacket[0] = 0x14;
        new Packet(encodedPacket);
    }


    @Test(expected = IllegalArgumentException.class)
    public void test_wrongCrc16_1() throws Exception {
        byte[] encodedPacket = packet.toPacket();
        encodedPacket[1] = 6;
        new Packet(encodedPacket);

    }

    @Test(expected = IllegalArgumentException.class)
    public void test_wrongCrc16_2() throws Exception {
        byte[] encodedPacket = packet.toPacket();
        encodedPacket[18] = 6;
        new Packet(encodedPacket);
    }

    @Test
    public void test_getter_bSrc() {
        Assert.assertEquals(Byte.valueOf((byte) 1), packet.getBSrc());
    }

    @Test
    public void test_getter_bPktId() {
        Assert.assertEquals(unsignedLongbPktId, packet.getBPktId());
    }

    @Test
    public void test_getter_wLen() {
        Assert.assertEquals(Integer.valueOf(testMessage.getMessageBytes()), packet.getWLen());
    }

    @Test
    public void test_getter_wCrc16_1() {
        UnsignedLong moreThanLongbPktId = UnsignedLong.valueOf(Long.MAX_VALUE);
        moreThanLongbPktId = moreThanLongbPktId.plus(UnsignedLong.valueOf("17"));
        Message testMessage = new Message(3, 4, "test");
        Packet packet = new Packet((byte) 1, moreThanLongbPktId, testMessage);
        short checkCrc1 = packet.calculateCrc16(packet.getBSrc(), packet.getBPktId());
        Assert.assertEquals(Short.valueOf(checkCrc1), packet.getWCrc16_1());
    }

    @Test
    public void test_getter_wCrc16_2() {
        UnsignedLong moreThanLongbPktId = UnsignedLong.valueOf(Long.MAX_VALUE);
        moreThanLongbPktId = moreThanLongbPktId.plus(UnsignedLong.valueOf("17"));
        Message testMessage = new Message(3, 4, "test");
        Packet packet = new Packet((byte) 1, moreThanLongbPktId, testMessage);
        short checkCrc2 = packet.calculateCrc16(packet.getBMsq());
        Assert.assertEquals(Short.valueOf(checkCrc2), packet.getWCrc16_2());
    }

    @Test
    public void testToString() {
        Packet packet = new Packet();
        String expected = "Packet(bSrc=null, bPktId=null, wLen=null, bMsq=null, wCrc16_1=null, wCrc16_2=null)";
        Assert.assertEquals(expected, packet.toString());
    }
}
