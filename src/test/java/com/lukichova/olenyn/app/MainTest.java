package com.lukichova.olenyn.app;

import com.lukichova.olenyn.app.classes.AES;
import com.lukichova.olenyn.app.entities.Message;
import com.lukichova.olenyn.app.entities.Packet;
import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;

import static com.lukichova.olenyn.app.classes.Key.secretKey;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MainTest {
    @Test
    public void test_Deencode() {
        String sourceText = "Practice_1";
        String cypheredText = AES.encrypt(sourceText, secretKey);
        String decypheredText = AES.decrypt(cypheredText, secretKey);
        assertTrue(sourceText.equals(decypheredText));
    }

    @Test
    public void testEncode_different() {
        String sourceText = "test123";
        String cypheredText = AES.encrypt(sourceText, secretKey);
        assertFalse(sourceText.equals(cypheredText));
    }

    @Test
    public void testDecode_different() {
        String sourceText = "test123";
        String cypheredText = AES.decrypt(sourceText, secretKey);
        assertFalse(sourceText.equals(cypheredText));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_wrongMagic() throws Exception {
        Message testMessage = new Message(3, 4, "test");
        Packet packet = new Packet((byte) 1, 2L, testMessage);
        byte[] encodedPacket = packet.toPacket();
        encodedPacket[0] = 0x14;
        new Packet(encodedPacket);
    }


    @Test(expected = IllegalArgumentException.class)
    public void test_wrongCrc16_1() throws Exception {
        Message testMessage = new Message(3, 4, "test");
        Packet packet = new Packet((byte) 1, 2L, testMessage);
        byte[] encodedPacket = packet.toPacket();
        encodedPacket[1] = 6;
        new Packet(encodedPacket);

    }

    @Test(expected = IllegalArgumentException.class)
    public void test_wrongCrc16_2() throws Exception {
        Message testMessage = new Message(3, 4, "test");
        Packet packet = new Packet((byte) 1, 2L, testMessage);
        byte[] encodedPacket = packet.toPacket();
        encodedPacket[18] = 6;
        new Packet(encodedPacket);
    }

    @Test
    public void test_getter_bSrc() {
        Message testMessage = new Message(3, 4, "test");
        Packet packet = new Packet((byte) 1, 2L, testMessage);
        Assert.assertEquals(Byte.valueOf((byte) 1), packet.getBSrc());
    }

    @Test
    public void test_getter_bPktId() {
        Message testMessage = new Message(3, 4, "test");
        Packet packet = new Packet((byte) 1, 2L, testMessage);
        Assert.assertEquals(Long.valueOf(2L), packet.getBPktId());
    }

    @Test
    public void test_getter_wLen() {
        Message testMessage = new Message(3, 4, "test");
        Packet packet = new Packet((byte) 1, 2L, testMessage);
        Assert.assertEquals(Integer.valueOf(testMessage.getMessageBytes()), packet.getWLen());
    }

    @Test
    public void test_getter_wCrc16_1() {
        Message testMessage = new Message(3, 4, "test");
        Packet packet = new Packet((byte) 1, 2L, testMessage);
        short checkCrc1 = packet.calculateCrc16(packet.getBSrc(), packet.getBPktId());
        Assert.assertEquals(Short.valueOf(checkCrc1), packet.getWCrc16_1());
    }

    @Test
    public void test_getter_wCrc16_2() {
        Message testMessage = new Message(3, 4, "test");
        Packet packet = new Packet((byte) 1, 2L, testMessage);
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