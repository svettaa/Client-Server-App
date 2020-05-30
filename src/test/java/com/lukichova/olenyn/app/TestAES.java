package com.lukichova.olenyn.app;

import com.lukichova.olenyn.app.Exceptions.wrongDecryptException;
import com.lukichova.olenyn.app.classes.AES;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestAES {

    @Test
    public void test_Deencode() throws Exception, wrongDecryptException {
        String sourceText = "test123";
        String cypheredText = AES.encrypt(sourceText);
        String decypheredText = AES.decrypt(cypheredText);
        assertTrue(sourceText.equals(decypheredText));
    }

    @Test
    public void testEncode_different() throws Exception{
        String sourceText = "test123";
        String cypheredText = AES.encrypt(sourceText);
        assertFalse(sourceText.equals(cypheredText));
    }

}
