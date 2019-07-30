package com.example.zabijakserver.Exceptions;

public class InvalidPlayerException extends IndexOutOfBoundsException {
    public InvalidPlayerException(String s) {
        super(s);
    }
}
