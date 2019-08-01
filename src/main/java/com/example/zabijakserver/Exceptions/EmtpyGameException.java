package com.example.zabijakserver.Exceptions;

import javassist.NotFoundException;

public class EmtpyGameException extends Exception {
    public EmtpyGameException(String s) {
        super(s);
    }
}
