package com.example.spring_compra.response;

import com.example.spring_compra.model.Compra;
import com.example.spring_compra.model.Tarjeta;

public class CompraResponse {
    private Long id;
    private String email;
    private Tarjeta tarjeta;
    private CompraEventoResponse evento;
    private Double precio;
    private String fechaCompra;

    public CompraResponse() {
    }

    public static CompraResponse fromCompra(Compra compra, CompraEventoResponse eventoDetails) {
        if (compra == null) {
            return null;
        }
        CompraResponse response = new CompraResponse();
        response.setId(compra.getId());
        response.setEmail(compra.getEmail());
        response.setTarjeta(compra.getTarjeta());
        response.setEvento(eventoDetails);
        response.setPrecio(compra.getPrecio());
        response.setFechaCompra(compra.getFechaCompra());
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Tarjeta getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(Tarjeta tarjeta) {
        this.tarjeta = tarjeta;
    }

    public void setEvento(CompraEventoResponse evento) {
        this.evento = evento;
    }

    public CompraEventoResponse getEvento() {
        return evento;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(String fechaCompra) {
        this.fechaCompra = fechaCompra;
    }
}