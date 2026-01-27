package com.example.spring_compra.feignClients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.spring_compra.response.CompraEventoResponse;

@FeignClient(name = "evento", url= "http://localhost:7777")
public interface EventoFeignClient {

    @GetMapping("/eventos/{id}")
    public CompraEventoResponse getEvento (@PathVariable Long id);
    
    @GetMapping("/eventos")
    public List<CompraEventoResponse> getEventos ();
}
