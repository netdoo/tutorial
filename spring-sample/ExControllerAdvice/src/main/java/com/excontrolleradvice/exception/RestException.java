package com.excontrolleradvice.exception;

public class RestException {

    String message;
    String status;

    public void setMessage(String _msg) {
        this.message = _msg;
    }

    public String getMessage() {
        return this.message;
    }

    public void setStatus(String _status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }
}
