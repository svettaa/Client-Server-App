package com.lukichova.olenyn.app.classes;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class AES {
    public static final String algorithm = "AES";
    private static SecretKeySpec secretKey;
    static String secret = "sonyaandsvetathebestprogrammers";
    private static byte[] key;
  /* AES() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest sha = null;
        key = secret.getBytes("UTF-8");
        sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16);
        secretKey = new SecretKeySpec(key, algorithm);
    }*/
    public static String encrypt(String strToEncrypt) throws Exception
    {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));


    }

    public static String decrypt(String strToDecrypt) throws Exception
    {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));

    }

    public static void setKey(String myKey) throws Exception
    {
        MessageDigest sha = null;
        key = secret.getBytes("UTF-8");
        sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16);
        secretKey = new SecretKeySpec(key, algorithm);

    }
}