package com.lukichova.olenyn.app.entities;
import lombok.Builder;
@Builder(toBuilder = true)
public class Message {

    Byte bSrc;
    Long bPktId;
    Integer wLen;
    String wCrc161;
    Message message;
    String   wCrc162;
}
