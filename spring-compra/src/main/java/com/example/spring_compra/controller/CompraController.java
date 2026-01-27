package com.example.spring_compra.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_compra.model.Compra;
import com.example.spring_compra.model.Tarjeta;
import com.example.spring_compra.repository.TarjetaRepository;
import com.example.spring_compra.response.CompraResponse;
import com.example.spring_compra.service.CompraService;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/compras")
public class CompraController {

    @Autowired
    private CompraService compraService;


    @GetMapping
    public ResponseEntity<List<CompraResponse>> getAllCompras() {
        List<Compra> compras = compraService.getAllCompras();
        if (compras.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        List<CompraResponse> responses = compras.stream()
                .map(compra -> CompraResponse.fromCompra(compra, compra.getEvento()))
                .toList();
        
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<CompraResponse> compraEntradas(
            @RequestParam String email,
            @RequestParam Long tarjetaId,
            @RequestParam Long eventoId) {
        try {
            
            Compra savedCompra = compraService.compraEntradas(email, tarjetaId, eventoId);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(CompraResponse.fromCompra(savedCompra, savedCompra.getEvento()));
        } catch (Exception e) {
            throw new RuntimeException("Error al crear la compra: " + e.getMessage(), e);
        }
    }
}
