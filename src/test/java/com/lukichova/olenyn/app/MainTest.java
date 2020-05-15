package com.lukichova.olenyn.app;

import com.lukichova.olenyn.app.classes.AES;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MainTest {
    @Test
    public void testDeencode() {
        String sourceText = "test123";
        String cypheredText = AES.encrypt(sourceText, "aaa");
        String decypheredText = AES.decrypt(cypheredText, "aaa");
        assertTrue(sourceText.equals(decypheredText));
    }
}
