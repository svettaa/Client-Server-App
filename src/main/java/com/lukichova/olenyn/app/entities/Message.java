package com.lukichova.olenyn.app.entities;

import com.lukichova.olenyn.app.classes.AES;
import lombok.Data;

import java.nio.ByteBuffer;

import static com.lukichova.olenyn.app.classes.Key.secretKey;

@Data
public class Message {
    Integer cType;
    Integer bUserId;
    String message;

    public static final int BYTES_WITHOUT_MESSAGE = Integer.BYTES + Integer.BYTES;

    public Message() { }

    public Message(Integer cType, Integer bUserId, String message) {
        this.cType = cType;
        this.bUserId = bUserId;
        this.message = message;
    }

    public byte[] toPacketPart() {
        return ByteBuffer.allocate(getMessageBytesLength())
                .putInt(cType)
                .putInt(bUserId)
                .put(message.getBytes()).array();
    }

    public int getMessageBytesLength() {
        return BYTES_WITHOUT_MESSAGE + getMessageBytes();
    }

    public Integer getMessageBytes() {
        return message.length();
    }

    public void encode() {
        message = AES.encrypt(message, secretKey);
    }

    public void decode() {
        message = AES.decrypt(message, secretKey);
    }
}
