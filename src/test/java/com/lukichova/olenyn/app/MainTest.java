package com.lukichova.olenyn.app;

import com.google.common.primitives.UnsignedLong;
import com.lukichova.olenyn.app.classes.AES;
import com.lukichova.olenyn.app.entities.Message;
import com.lukichova.olenyn.app.entities.Packet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.lukichova.olenyn.app.classes.Key.secretKey;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MainTest {
    private Message testMessage;
    private Packet packet;

    @Before
    public void before_tests() throws Exception{
        UnsignedLong unsignedLongbPktId = UnsignedLong.valueOf(Long.MAX_VALUE);
        unsignedLongbPktId = unsignedLongbPktId.plus(UnsignedLong.valueOf("17"));
        testMessage = new Message(3, 4, "test");
        packet = new Packet((byte) 1, unsignedLongbPktId, testMessage);
    }

    @Test
    public void test_Deencode() throws Exception{
        String sourceText = "test123";
        String cypheredText = AES.encrypt(sourceText, secretKey);
        String decypheredText = AES.decrypt(cypheredText, secretKey);
        assertTrue(sourceText.equals(decypheredText));
    }

    @Test
    public void testEncode_different() throws Exception{
        String sourceText = "test123";
        String cypheredText = AES.encrypt(sourceText, secretKey);
        assertFalse(sourceText.equals(cypheredText));
    }

    @Test
    public void test_endecodeMessage() throws Exception{
        Message testMessage = new Message(3, 4, "We love programming!!!");
        Message encodedMessage = new Message(3, 4, "We love programming!!!");
        encodedMessage.encode();
        encodedMessage.decode();
        assertTrue(testMessage.equals(encodedMessage));
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
    public void testToString() {
        Packet packet = new Packet();
        String expected = "Packet(bSrc=null, bPktId=null, wLen=null, bMsq=null, wCrc16_1=null, wCrc16_2=null)";
        Assert.assertEquals(expected, packet.toString());
    }


}