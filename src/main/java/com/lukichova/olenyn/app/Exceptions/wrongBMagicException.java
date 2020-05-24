package com.lukichova.olenyn.app.Exceptions;

public class wrongBMagicException extends Exception {


    public wrongBMagicException(String unexpected_bMagic) {
        super(unexpected_bMagic);
    }
}