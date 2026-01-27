package com.example.spring_compra.controller;

public class CompraException extends RuntimeException {
    private String errorCode;
    private int httpStatus;

    public CompraException(String message) {
        super(message);
        this.errorCode = "COMPRA_ERROR";
        this.httpStatus = 400;
    }

    public CompraException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = 400;
    }

    public CompraException(String message, String errorCode, int httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}