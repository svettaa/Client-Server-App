package com.lukichova.olenyn.app.Exceptions;

public class wrongCrc2Exception extends Exception {


    public wrongCrc2Exception(String unexpected_crc_2) {
        super(unexpected_crc_2);
    }
}