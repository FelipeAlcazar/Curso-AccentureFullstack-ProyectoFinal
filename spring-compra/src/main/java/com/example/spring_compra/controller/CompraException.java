package com.example.spring_compra.controller;
import java.util.Map;
public class CompraException extends RuntimeException {
    private Map<String, Object> info;
    private String infoAdicional;

    public CompraException(String message) {
        super(message);
    }

    public CompraException(String message, Map<String, Object> info, String infoAdicional) {
        super(message);
        this.info = info;
        this.infoAdicional = infoAdicional;
    }

    public Map<String, Object> getInfo() {
        return info;
    }

    public String getInfoAdicional() {
        return infoAdicional;
    }
}
