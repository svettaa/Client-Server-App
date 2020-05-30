package com.lukichova.olenyn.app;

import com.google.common.primitives.UnsignedLong;
import com.lukichova.olenyn.app.Exceptions.wrongBMagicException;
import com.lukichova.olenyn.app.Exceptions.wrongCrc1Exception;
import com.lukichova.olenyn.app.Exceptions.wrongCrc2Exception;
import com.lukichova.olenyn.app.Exceptions.wrongDecryptException;
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
        unsignedLongbPktId = unsignedLongbPktId.plus(UnsignedLong.valueOf("2305"));
        testMessage = new Message(Message.cTypes.ADD_PRODUCT_TO_GROUP.ordinal(), 4, "test");
        packet = new Packet((byte) 1, unsignedLongbPktId, testMessage);
    }

    @Test
    public void test_coder() throws Exception, wrongDecryptException {
        byte[] encodedPacket = packet.toPacket();
        Packet decodedPacket = new Packet(encodedPacket);
        Assert.assertEquals(packet, decodedPacket);
    }

    @Test(expected = wrongBMagicException.class)
    public void test_wrongMagic() throws Exception, wrongDecryptException {
        byte[] encodedPacket = packet.toPacket();
        encodedPacket[0] = 0x14;
        new Packet(encodedPacket);
    }


    @Test(expected = wrongCrc1Exception.class)
    public void test_wrongCrc16() throws Exception, wrongDecryptException {
        byte[] encodedPacket = packet.toPacket();
        encodedPacket[1] = 6;
        new Packet(encodedPacket);

    }

    @Test(expected = wrongCrc2Exception.class)
    public void test_wrongCrc16_2() throws Exception, wrongDecryptException {
        byte[] encodedPacket = packet.toPacket();
        encodedPacket[21] = 6;
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
    public void testToString() {
        String expected = "Packet( bPktId: 9223372036854778112, bSrc: 1,wLen: 4, Message( CType:5, BUserId: 4, message: test)";
        Assert.assertEquals(expected, packet.toString());
    }


}
