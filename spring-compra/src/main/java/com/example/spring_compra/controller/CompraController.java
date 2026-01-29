package com.example.spring_compra.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_compra.dto.CompraDto;
import com.example.spring_compra.model.Compra;
import com.example.spring_compra.response.CompraResponse;
import com.example.spring_compra.response.PasarelaPagoResponse;
import com.example.spring_compra.service.CompraService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
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

    @CircuitBreaker(name = "eventoCB", fallbackMethod = "getAllComprasFallback")
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
    public ResponseEntity<PasarelaPagoResponse> handleCompraException(CompraException ex) {
        PasarelaPagoResponse errorResponse = new PasarelaPagoResponse();
        errorResponse.setStatus("200");

        String exMsg = ex.getMessage();

        // Map error codes to custom messages
        var errorMap = Map.of(
            "400.0003", "El número de tarjeta proporcionado no es correcto. Por favor, revisa el formato y vuelve a intentarlo.",
            "400.0004", "El formato del CVV no es válido. Debe contener solo 3 o 4 dígitos números.",
            "500.0001", "El sistema se encuentra inestable",
            "400.0001", "No hay fondos suficientes en la cuenta",
            "400.0002", "No se encuentran los datos del cliente"
        );

        String customMessage = errorMap.entrySet().stream()
            .filter(entry -> exMsg != null && exMsg.contains(entry.getKey()))
            .map(Map.Entry::getValue)
            .findFirst()
            .orElse(exMsg);

        String errorCode = errorMap.keySet().stream()
            .filter(code -> exMsg != null && exMsg.contains(code))
            .findFirst()
            .orElse(null);
        errorResponse.setError((errorCode != null ? errorCode : "CompraException"));
        errorResponse.setMessage(List.of(customMessage));
        errorResponse.setInfo(ex.getInfo());
        errorResponse.setInfoadicional(ex.getInfoAdicional());
        errorResponse.setTimestamp(java.time.LocalDateTime.now().toString());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(errorResponse);
    }

    public ResponseEntity<List<CompraResponse>> getAllComprasFallback(Throwable t) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }
}
