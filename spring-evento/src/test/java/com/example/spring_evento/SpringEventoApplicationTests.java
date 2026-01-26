package com.example.spring_evento;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SpringEventoApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetAllEventos() throws Exception {
        mockMvc.perform(get("/eventos"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetEventoById() throws Exception {
        mockMvc.perform(get("/eventos/1"))
            .andExpect(status().isOk());
    }

    @Test
    void testGetEventoByIdNotFound() throws Exception {
        mockMvc.perform(get("/eventos/999"))
            .andExpect(status().isNotFound());
    }

    @Test
    void testCreateEvento() throws Exception {
        String eventoJson = "{\"nombre\":\"Test Evento\",\"descripcion\":\"Test\",\"fecha\":\"2026-02-01\"}";
        mockMvc.perform(post("/eventos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(eventoJson))
            .andExpect(status().isCreated());
    }

    @Test
    void testUpdateEvento() throws Exception {
        String eventoJson = "{\"nombre\":\"Updated Evento\",\"descripcion\":\"Updated\",\"fecha\":\"2026-02-01\"}";
        mockMvc.perform(put("/eventos/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(eventoJson))
            .andExpect(status().isOk());
    }

}