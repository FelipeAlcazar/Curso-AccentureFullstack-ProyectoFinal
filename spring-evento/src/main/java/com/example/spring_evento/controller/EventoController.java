package com.example.spring_evento.controller;

import java.util.List;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/eventos")
@Tag(name = "Eventos", description = "API para gestión de eventos")
public class EventoController {
    @Autowired
    private EventoService eventoService;

    @Autowired
    private EventoAdapter eventoAdapter;
    
    @Operation(summary = "Obtener todos los eventos", description = "Devuelve una lista de todos los eventos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de eventos obtenida exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron eventos")
    })
    @GetMapping
    public ResponseEntity<List<EventoResponse>> getAllEventos() {
        List<Evento> eventos = eventoService.getAllEventos();
        if (eventos.isEmpty()) {
            throw new EventoNotFoundException("No se encontraron eventos");
        }
        return ResponseEntity.ok(eventoAdapter.of(eventos));
    }

    @Operation(summary = "Obtener evento por ID", description = "Devuelve un evento específico por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Evento encontrado"),
        @ApiResponse(responseCode = "404", description = "Evento no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EventoResponse> getEventoById(
            @Parameter(description = "ID del evento", required = true) @PathVariable Long id) {
        Evento evento = eventoService.getEventoById(id)
                .orElseThrow(() -> new EventoNotFoundException(id));
        return ResponseEntity.ok(eventoAdapter.of(evento));
    }

    @Operation(summary = "Crear nuevo evento", description = "Crea un nuevo evento")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Evento creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<EventoResponse> createEvento(@RequestBody Evento evento) {
        if (evento == null) {
            return ResponseEntity.badRequest().build();
        }
        Evento createdEvento = eventoService.createEvento(evento);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventoAdapter.of(createdEvento));
    }

    @Operation(summary = "Actualizar evento", description = "Actualiza un evento existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Evento actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Evento no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EventoResponse> updateEvento(
            @Parameter(description = "ID del evento", required = true) @PathVariable Long id,
            @RequestBody Evento evento) {
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

    @Operation(summary = "Eliminar evento", description = "Elimina un evento por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Evento eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Evento no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvento(
            @Parameter(description = "ID del evento", required = true) @PathVariable Long id) {
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