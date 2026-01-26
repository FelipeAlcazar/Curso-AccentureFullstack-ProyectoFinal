package com.example.spring_evento;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.spring_evento.adapter.EventoAdapter;
import com.example.spring_evento.controller.EventoController;
import com.example.spring_evento.model.Evento;
import com.example.spring_evento.response.EventoResponse;
import com.example.spring_evento.service.EventoService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(EventoController.class)
class SpringEventoApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventoService eventoService;

    @MockBean
    private EventoAdapter eventoAdapter;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void testGetAllEventos() throws Exception {
        when(eventoService.getAllEventos()).thenReturn(Arrays.asList(new Evento()));
        when(eventoAdapter.of(anyList())).thenReturn(Arrays.asList(new EventoResponse()));

        mockMvc.perform(get("/eventos"))
            .andExpect(status().isOk());
    }

    @Test
    void testGetAllEventosNoEvents() throws Exception {
        when(eventoService.getAllEventos()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/eventos"))
            .andExpect(status().isNotFound());
    }

    @Test
    void testGetEventoById() throws Exception {
        when(eventoService.getEventoById(1L)).thenReturn(Optional.of(new Evento()));
        when(eventoAdapter.of(any(Evento.class))).thenReturn(new EventoResponse());

        mockMvc.perform(get("/eventos/1"))
            .andExpect(status().isOk());
    }

    @Test
    void testGetEventoByIdNotFound() throws Exception {

        when(eventoService.getEventoById(-1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/eventos/-1"))
            .andExpect(status().isNotFound());
    }

    @Test
    void testCreateEvento() throws Exception {
        Evento evento = new Evento();
        evento.setNombre("Nuevo Evento");
        
        Evento createdEvento = new Evento();
        createdEvento.setId(1L);
        createdEvento.setNombre("Nuevo Evento");
        
        when(eventoService.createEvento(any(Evento.class))).thenReturn(createdEvento);
        when(eventoAdapter.of(any(Evento.class))).thenReturn(new EventoResponse());

        mockMvc.perform(post("/eventos")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(evento)))
            .andExpect(status().isCreated());
    }

    @Test
    void testUpdateEvento() throws Exception {
        Long id = 1L;
        Evento evento = new Evento();
        evento.setId(id);
        evento.setNombre("Evento Actualizado");
        
        when(eventoService.getEventoById(id)).thenReturn(java.util.Optional.of(new Evento()));
        when(eventoService.updateEvento(id, evento)).thenReturn(evento);
        when(eventoAdapter.of(any(Evento.class))).thenReturn(new EventoResponse());

        mockMvc.perform(put("/eventos/" + id)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(evento)))
            .andExpect(status().isOk());
    }

    @Test
    void testUpdateEventoNotFound() throws Exception {
        Long id = 999L;
        Evento evento = new Evento();
        evento.setId(id);
        
        when(eventoService.getEventoById(id)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(put("/eventos/" + id)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(evento)))
            .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteEvento() throws Exception {
        Long id = 1L;
        when(eventoService.getEventoById(id)).thenReturn(java.util.Optional.of(new Evento()));

        mockMvc.perform(delete("/eventos/" + id))
            .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteEventoNotFound() throws Exception {
        Long id = 999L;
        when(eventoService.getEventoById(id)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(delete("/eventos/" + id))
            .andExpect(status().isNotFound());
    }
}