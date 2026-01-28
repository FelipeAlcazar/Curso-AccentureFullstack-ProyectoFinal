package com.example.spring_compra.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/compras")
@Tag(name = "Compras", description = "API para gestión de compras de entradas")
public class CompraController {

    @Autowired
    private CompraService compraService;


    @Operation(summary = "Obtener todas las compras", description = "Devuelve una lista con el historial de compras")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de compras recuperada con éxito"),
        @ApiResponse(responseCode = "204", description = "No hay compras registradas")
    })
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

    @Operation(summary = "Realizar una compra", description = "Procesa la compra de entradas para un evento específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Compra realizada con éxito"),
        @ApiResponse(responseCode = "400", description = "Datos de compra inválidos o error en el proceso")
    })
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

    @ExceptionHandler(CompraException.class)
    public ResponseEntity<String> handleCompraException(CompraException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
