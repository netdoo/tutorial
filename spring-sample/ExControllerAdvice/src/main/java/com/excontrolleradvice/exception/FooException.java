package com.excontrolleradvice.exception;

public class FooException  extends RuntimeException{
    private String message;

    public FooException(String _msg) {
        this.message = _msg;
    }

    public String getMessage() {
        return this.message;
    }
}
