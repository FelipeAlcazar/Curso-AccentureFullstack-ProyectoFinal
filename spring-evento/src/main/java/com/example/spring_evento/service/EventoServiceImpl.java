package com.example.spring_evento.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.spring_evento.model.Evento;
import com.example.spring_evento.repository.EventoRepository;

@Service
public class EventoServiceImpl implements EventoService {
    
    @Autowired
    private EventoRepository eventoRepository;

    @Override
    public List<Evento> getAllEventos() {
        return eventoRepository.findAll();
    }

    @Override
    public Optional<Evento> getEventoById(Long id) {
        return eventoRepository.findById(id);
    }

    @Override
    public Evento createEvento(Evento evento) {
        return eventoRepository.save(evento);
    }

    @Override
    public Evento updateEvento(Long id, Evento evento) {
        evento.setId(id);
        return eventoRepository.save(evento);
    }

    @Override
    public void deleteEvento(Long id) {
        if (id != null) {
            eventoRepository.deleteById(id);
        }
    }

}
