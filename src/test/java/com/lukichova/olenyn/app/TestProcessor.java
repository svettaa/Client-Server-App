package com.lukichova.olenyn.app;

import com.google.common.primitives.UnsignedLong;
import com.lukichova.olenyn.app.classes.Processor;
import com.lukichova.olenyn.app.entities.Message;
import com.lukichova.olenyn.app.entities.Packet;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestProcessor {
    private Message testMessage;
    private Packet packet;
    UnsignedLong unsignedLongbPktId = UnsignedLong.valueOf(Long.MAX_VALUE);

    @Test
    public void process1() {

        unsignedLongbPktId = unsignedLongbPktId.plus(UnsignedLong.valueOf("2305"));
        testMessage = new Message(3, 4, "time");
        packet = new Packet((byte) 1, unsignedLongbPktId, testMessage);
        Packet answer = Processor.process(packet);
        Assert.assertEquals("OK",answer.getBMsq().getMessage());
    }
    @Test
    public void process2() {

        unsignedLongbPktId = unsignedLongbPktId.plus(UnsignedLong.valueOf("2305"));
        testMessage = new Message(3, 4, "notime");
        packet = new Packet((byte) 1, unsignedLongbPktId, testMessage);
        Packet answer = Processor.process(packet);
        Assert.assertEquals("other",answer.getBMsq().getMessage());
    }
}