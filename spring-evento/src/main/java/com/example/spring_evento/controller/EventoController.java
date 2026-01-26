package com.example.spring_evento.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_evento.adapter.EventoAdapter;
import com.example.spring_evento.model.Evento;
import com.example.spring_evento.response.EventoResponse;
import com.example.spring_evento.service.EventoService;

@RestController
@RequestMapping("/eventos")
public class EventoController {
    @Autowired
    private EventoService eventoService;

    @Autowired
    private EventoAdapter eventoAdapter;

        @GetMapping
    public ResponseEntity<List<EventoResponse>> getAllEventos() {
        List<Evento> eventos = eventoService.getAllEventos();
        return ResponseEntity.ok(eventoAdapter.of(eventos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventoResponse> getEventoById(@PathVariable Long id) {
        Evento evento = eventoService.getEventoById(id)
                .orElseThrow(() -> new EventoNotFoundException(id));
        return ResponseEntity.ok(eventoAdapter.of(evento));
    }

    @PostMapping
    public ResponseEntity<EventoResponse> createEvento(@RequestBody Evento evento) {
        if (evento == null) {
            return ResponseEntity.badRequest().build();
        }
        Evento createdEvento = eventoService.createEvento(evento);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventoAdapter.of(createdEvento));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventoResponse> updateEvento(@PathVariable Long id, @RequestBody Evento evento) {
        if (evento == null) {
            return ResponseEntity.badRequest().build();
        }
        if (evento.getId() != null && !id.equals(evento.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Evento existing = eventoService.getEventoById(id)
                .orElseThrow(() -> new EventoNotFoundException(id));

        evento.setId(existing.getId());
        Evento updated = eventoService.updateEvento(id, evento);
        return ResponseEntity.ok(eventoAdapter.of(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvento(@PathVariable Long id) {
        if (eventoService.getEventoById(id).isEmpty()) {
            throw new EventoNotFoundException(id);
        }
        eventoService.deleteEvento(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(EventoNotFoundException.class)
    public ResponseEntity<String> handleEventoNotFound(EventoNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}