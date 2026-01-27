package com.example.spring_compra.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_compra.model.Compra;
import com.example.spring_compra.response.CompraResponse;
import com.example.spring_compra.response.PasarelaPagoResponse;
import com.example.spring_compra.service.CompraService;
import org.springframework.http.HttpStatus;
import com.example.spring_compra.dto.CompraDto;

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
    public ResponseEntity<PasarelaPagoResponse> compraEntradas(@RequestBody CompraDto request) {
        try {
            PasarelaPagoResponse pago = compraService.compraEntradas(
                request.getEmail(),
                request.getTarjetaId(),
                request.getEventoId()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(pago);
        } catch (Exception e) {
            throw new CompraException("Error al crear la compra: " + e.getMessage());
        }
    }
}
