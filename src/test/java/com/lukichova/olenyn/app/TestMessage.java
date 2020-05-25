package com.lukichova.olenyn.app;

import com.lukichova.olenyn.app.entities.Message;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TestMessage {

    @Test
    public void test_endecodeMessage() throws Exception{
        Message testMessage = new Message(Message.cTypes.ADD_PRODUCT_TITLE.ordinal(), 4, "We love programming!!!");
        Message encodedMessage = new Message(3, 4, "We love programming!!!");
        encodedMessage.encode();
        encodedMessage.decode();
        assertTrue(testMessage.equals(encodedMessage));
    }

}
