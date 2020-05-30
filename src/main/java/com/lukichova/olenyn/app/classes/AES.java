package com.lukichova.olenyn.app.classes;

import com.lukichova.olenyn.app.Exceptions.wrongDecryptException;
import lombok.SneakyThrows;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class AES {
    public static final String algorithm = "AES";
    private static SecretKeySpec secretKey;
    static String secret = "sonyaandsvetathebestprogrammers";
    private static byte[] key;

    public static String encrypt(String strToEncrypt) throws Exception
    {
            setKey();
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));


    }

    @SneakyThrows
    public static String decrypt(String strToDecrypt)
            throws wrongDecryptException {

            try {
                setKey();
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
                cipher.init(Cipher.DECRYPT_MODE, secretKey);
                return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
            } catch (UnsupportedEncodingException | NoSuchAlgorithmException e){
                throw new wrongDecryptException();
        }
    }


    public static void setKey() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        if (key == null) {
        MessageDigest sha = null;
        key = secret.getBytes("UTF-8");
        sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16);
        secretKey = new SecretKeySpec(key, algorithm);

    }}
}