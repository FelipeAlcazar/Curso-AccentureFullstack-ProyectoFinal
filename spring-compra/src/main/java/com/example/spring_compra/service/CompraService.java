package com.example.spring_compra.service;

import java.util.List;

import com.example.spring_compra.model.Compra;

public interface CompraService {
    public Compra compraEntradas(String email, Long tarjetaId, Long eventoId); 
    public List<Compra> getAllCompras();
}
