package com.example.spring_compra.dto;

public class CompraDto {
    private String email;
    private Long tarjetaId;
    private Long eventoId;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Long getTarjetaId() { return tarjetaId; }
    public void setTarjetaId(Long tarjetaId) { this.tarjetaId = tarjetaId; }

    public Long getEventoId() { return eventoId; }
    public void setEventoId(Long eventoId) { this.eventoId = eventoId; }
    
}