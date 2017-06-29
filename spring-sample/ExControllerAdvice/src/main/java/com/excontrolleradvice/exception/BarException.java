package com.excontrolleradvice.exception;

public class BarException extends RuntimeException{
    private String message;

    public BarException(String _msg) {
        this.message = _msg;
    }

    public String getMessage() {
        return this.message;
    }
}
