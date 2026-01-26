package com.example.spring_evento.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.spring_evento.model.Evento;
import com.example.spring_evento.response.EventoResponse;

@Component
public class EventoAdapter {
        public EventoResponse of(Evento evento) {
        EventoResponse eventoResponse = new EventoResponse();
        eventoResponse.setId(evento.getId());
        eventoResponse.setNombre(evento.getNombre());
        eventoResponse.setDescripcion(evento.getDescripcion());
        eventoResponse.setFechaEvento(evento.getFechaEvento());
        eventoResponse.setHoraEvento(evento.getHoraEvento());
        eventoResponse.setPrecioMinimo(evento.getPrecioMinimo());
        eventoResponse.setPrecioMaximo(evento.getPrecioMaximo());
        eventoResponse.setLocalidad(evento.getLocalidad());
        eventoResponse.setGenero(evento.getGenero());
        eventoResponse.setNombreRecinto(evento.getNombreRecinto());
        return eventoResponse;
    }

    public List<EventoResponse> of(List<Evento> eventos) {
        return eventos.stream()
                     .map(this::of)
                     .collect(Collectors.toList());
    }
}
