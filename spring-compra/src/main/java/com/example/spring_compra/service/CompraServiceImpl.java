package com.example.spring_compra.service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.spring_compra.dto.PasarelaPagoDto;
import com.example.spring_compra.feignClients.EventoFeignClient;
import com.example.spring_compra.feignClients.PasarelaFeignClient;
import com.example.spring_compra.model.Compra;
import com.example.spring_compra.model.Tarjeta;
import com.example.spring_compra.repository.CompraRepository;
import com.example.spring_compra.repository.TarjetaRepository;
import com.example.spring_compra.response.CompraEventoResponse;
import com.example.spring_compra.response.PasarelaPagoResponse;

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
    public PasarelaPagoResponse compraEntradas(String email, Long tarjetaId, Long eventoId) {
        CompraEventoResponse evento = eventoFeignClient.getEvento(eventoId);
        Tarjeta tarjeta = tarjetaRepository.findById(tarjetaId)
            .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada"));

        PasarelaPagoDto pasarelaPagoDto = new PasarelaPagoDto();
        pasarelaPagoDto.setNombreTitular(normalizeNombreTitular(tarjeta.getNombreTitular()));
        pasarelaPagoDto.setNumeroTarjeta(tarjeta.getNumeroTarjeta());
        pasarelaPagoDto.setMesCaducidad(tarjeta.getMesCaducidad());
        pasarelaPagoDto.setYearCaducidad(tarjeta.getYearCaducidad());
        pasarelaPagoDto.setCvv(tarjeta.getCvv());
        pasarelaPagoDto.setEmisor(EMISOR);
        pasarelaPagoDto.setConcepto("Compra de entrada: " + evento.getNombre());
        pasarelaPagoDto.setCantidad(String.valueOf(generarPrecioAleatorio(evento.getPrecioMinimo(), evento.getPrecioMaximo())));
        
        PasarelaPagoResponse response;
        try {
            response = pasarelaFeignClient.compra(pasarelaPagoDto);
        } catch (feign.FeignException e) {
            throw new ResponseStatusException(
                HttpStatus.valueOf(e.status()),
                e.contentUTF8(),
                e
            );
        }

        Compra compra = new Compra();
        compra.setEmail(email);
        compra.setTarjeta(tarjeta);
        compra.setEventoId(eventoId);
        compra.setEvento(evento);
        compra.setPrecio(Double.valueOf(pasarelaPagoDto.getCantidad()));
        compra.setFechaCompra(java.time.LocalDateTime.now()
            .format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        compraRepository.save(compra);

        return response;
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
        for (Compra compra : compras) {
            if (compra.getEventoId() != null) {
                compra.setEvento(eventoFeignClient.getEvento(compra.getEventoId()));
            }
        }
        return compras;
    }

    private String normalizeNombreTitular(String nombre) {
        if (nombre == null) throw new IllegalArgumentException("Nombre del titular requerido");
        String ascii = java.text.Normalizer.normalize(nombre.trim(), java.text.Normalizer.Form.NFD)
            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
            .replaceAll("\\s+", " ")
            .toUpperCase();

        if (!ascii.matches("^[A-Z]+( [A-Z]+){0,2}$")) {
            throw new IllegalArgumentException("NombreTitular inválido. Use 1–3 palabras en mayúsculas sin acentos.");
        }
        return ascii;
    }
}