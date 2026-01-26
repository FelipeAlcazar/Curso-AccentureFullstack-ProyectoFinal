package com.example.spring_evento.controller;

public class EventoNotFoundException extends RuntimeException {
    	private static final long serialVersionUID = 1L;

    public EventoNotFoundException(Long id) {
        super("Nos se ha encontrado un evento con el id: " + id);
    }

    public EventoNotFoundException(String message) {
        super(message);
    }
}
