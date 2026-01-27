package com.example.spring_compra.response;

import java.util.List;
import java.util.Map;

public class PasarelaPagoResponse {
    private String timestamp;
    private String status;
    private String error;
    private List<String> message;
    private Map<String, Object> info;
    private String infoadicional;

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
    public List<String> getMessage() { return message; }
    public void setMessage(List<String> message) { this.message = message; }
    public Map<String, Object> getInfo() { return info; }
    public void setInfo(Map<String, Object> info) { this.info = info; }
    public String getInfoadicional() { return infoadicional; }
    public void setInfoadicional(String infoadicional) { this.infoadicional = infoadicional; }

    public boolean isSuccess() {
        return error != null && error.startsWith("200");
    }
}