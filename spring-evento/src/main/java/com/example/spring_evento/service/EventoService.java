package com.example.spring_evento.service;

import java.util.List;
import java.util.Optional;

import com.example.spring_evento.model.Evento;

public interface EventoService {
    public List<Evento> getAllEventos();
    public Optional<Evento> getEventoById(Long id);
    public Evento createEvento(Evento evento);
    public Evento updateEvento(Long id, Evento evento);
    public void deleteEvento(Long id);
}
