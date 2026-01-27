package com.example.spring_compra.service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.spring_compra.dto.PasarelaPagoDto;
import com.example.spring_compra.response.PasarelaPagoResponse;
import com.example.spring_compra.feignClients.EventoFeignClient;
import com.example.spring_compra.model.Compra;
import com.example.spring_compra.model.Tarjeta;
import com.example.spring_compra.repository.CompraRepository;
import com.example.spring_compra.repository.TarjetaRepository;
import com.example.spring_compra.response.CompraEventoResponse;
import com.example.spring_compra.feignClients.PasarelaFeignClient;

@Service
public class CompraServiceImpl implements CompraService {

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private TarjetaRepository tarjetaRepository;

    @Autowired
    private EventoFeignClient eventoFeignClient;

    @Autowired
    private PasarelaFeignClient pasarelaFeignClient;
    
    private static final String EMISOR = "EventTickets";

    @Override
    public Compra compraEntradas(String email, Long tarjetaId, Long eventoId) {
        var evento = eventoFeignClient.getEvento(eventoId);
        var tarjeta = tarjetaRepository.findById(tarjetaId)
            .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada"));

        var dto = new PasarelaPagoDto();
        dto.setNombreTitular(normalizeNombreTitular(tarjeta.getNombreTitular()));
        dto.setNumeroTarjeta(tarjeta.getNumeroTarjeta());
        dto.setMesCaducidad(tarjeta.getMesCaducidad());
        dto.setYearCaducidad(tarjeta.getYearCaducidad());
        dto.setCvv(tarjeta.getCvv());
        dto.setEmisor(EMISOR);
        dto.setConcepto("Compra de entrada: " + evento.getNombre());
        dto.setCantidad(String.valueOf(generarPrecioAleatorio(evento.getPrecioMinimo(), evento.getPrecioMaximo())));

        try {
            var response = pasarelaFeignClient.compra(dto);
            
            if (!response.isSuccess() && !isAcceptableError(response.getError())) {
                throw new RuntimeException("Pago rechazado: " + response.getError());
            }
            
            if (response.getMessage() != null) {
                System.out.println("Pasarela: " + String.join("; ", response.getMessage()));
            }
            
        } catch (feign.FeignException e) {
            System.err.println("Payment gateway error: " + e.status());
        }

        var compra = new Compra();
        compra.setEmail(email);
        compra.setTarjeta(tarjeta);
        compra.setEventoId(eventoId);
        compra.setEvento(evento);
        return compraRepository.save(compra);
    }

    private String generarPrecioAleatorio(Double precioMinimo, Double precioMaximo) {
        if (precioMinimo == null || precioMaximo == null) {
            throw new IllegalArgumentException("Precios mínimo y máximo son requeridos");
        }
        Random random = new Random();
        double valor = precioMinimo + (precioMaximo - precioMinimo) * random.nextDouble();
        int entero = java.math.BigDecimal.valueOf(valor)
                .setScale(0, java.math.RoundingMode.HALF_UP)
                .intValue();
        return Integer.toString(entero);
    }

    @Override
    public List<Compra> getAllCompras() {
        List<Compra> compras = compraRepository.findAll();
        
        return compras.stream()
            .map(compra -> {
                if (compra.getEventoId() != null) {
                    try {
                        CompraEventoResponse eventoDetails = eventoFeignClient.getEvento(compra.getEventoId());
                        compra.setEvento(eventoDetails);
                    } catch (Exception e) {
                        System.err.println("Error fetching evento " + compra.getEventoId() + ": " + e.getMessage());
                    }
                }
                return compra;
            })
            .collect(Collectors.toList());
    }

    private String normalizeNombreTitular(String nombre) {
        if (nombre == null) throw new IllegalArgumentException("Nombre del titular requerido");
        String ascii = java.text.Normalizer.normalize(nombre.trim(), java.text.Normalizer.Form.NFD)
            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
            .replaceAll("\\s+", " ")
            .toUpperCase();
        // must be 1–3 words, letters only
        if (!ascii.matches("^[A-Z]+( [A-Z]+){0,2}$")) {
            throw new IllegalArgumentException("NombreTitular inválido. Use 1–3 palabras en mayúsculas sin acentos.");
        }
        return ascii;
    }

    private boolean isAcceptableError(String error) {
        return error != null && (error.startsWith("400.0001") 
            || error.startsWith("400.0002") 
            || error.startsWith("500.0001"));
    }

    private PasarelaPagoResponse parseError(String raw) {
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().readValue(raw, PasarelaPagoResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing pasarela response");
        }
    }

}