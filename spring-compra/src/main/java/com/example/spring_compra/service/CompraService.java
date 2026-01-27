package com.example.spring_compra.service;

import java.util.List;

import com.example.spring_compra.model.Compra;
import com.example.spring_compra.response.PasarelaPagoResponse;

public interface CompraService {
    public PasarelaPagoResponse compraEntradas(String email, Long tarjetaId, Long eventoId); 
    public List<Compra> getAllCompras();
}
