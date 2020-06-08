package com.lukichova.olenyn.app.entities;

import com.lukichova.olenyn.app.Exceptions.wrongDecryptException;
import com.lukichova.olenyn.app.Exceptions.wrongEcryptException;
import com.lukichova.olenyn.app.classes.AES;
import lombok.Data;

import java.nio.ByteBuffer;


@Data
public class Message {


    public enum cTypes {
        GET_PRODUCTS, //0
        UPDATE_PRODUCT, //1
        DELETE_PRODUCT, //2
        CREATE_PRODUCT, //3
        LIST_BY_CRITERIA, //4
        GET_GROUPS,
        ADD_GROUP,
        ADD_PRODUCT_TO_GROUP //
    }


    Integer cType;
    Integer bUserId;
    String message;

    public static final int BYTES_WITHOUT_MESSAGE = Integer.BYTES + Integer.BYTES;
    public static final int MAX_MESSAGE_SIZE = 255;
    public static final int BYTES_MAX_SIZE = BYTES_WITHOUT_MESSAGE + MAX_MESSAGE_SIZE;
    public Message() {  }

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

    public void encode() throws wrongEcryptException {
        message = AES.encrypt(message);
    }


    public void decode() throws wrongDecryptException {
        message = AES.decrypt(message);
    }
}
