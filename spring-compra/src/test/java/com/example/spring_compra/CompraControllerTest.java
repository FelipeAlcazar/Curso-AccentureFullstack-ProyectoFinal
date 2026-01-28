package com.example.spring_compra;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.spring_compra.controller.CompraController;
import com.example.spring_compra.dto.CompraDto;
import com.example.spring_compra.model.Compra;
import com.example.spring_compra.response.PasarelaPagoResponse;
import com.example.spring_compra.service.CompraService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CompraController.class)
class CompraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompraService compraService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllCompras() throws Exception {
        Compra compra = new Compra();
        compra.setId(1L);
        compra.setEmail("test@example.com");
        when(compraService.getAllCompras()).thenReturn(Arrays.asList(compra));

        mockMvc.perform(get("/compras"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllComprasEmpty() throws Exception {
        when(compraService.getAllCompras()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/compras"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testCompraEntradasSuccess() throws Exception {
        CompraDto compraDto = new CompraDto();
        compraDto.setEmail("test@example.com");
        compraDto.setTarjetaId(1L);
        compraDto.setEventoId(1L);

        PasarelaPagoResponse pasarelaResponse = new PasarelaPagoResponse();
        pasarelaResponse.setStatus("200 OK");

        when(compraService.compraEntradas(
            compraDto.getEmail(),
            compraDto.getTarjetaId(),
            compraDto.getEventoId()
        )).thenReturn(pasarelaResponse);

        mockMvc.perform(post("/compras")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(compraDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void testCompraEntradasFailure() throws Exception {
        CompraDto compraDto = new CompraDto();
        compraDto.setEmail("test@example.com");
        compraDto.setTarjetaId(1L);
        compraDto.setEventoId(1L);

        String errorMessage = "Service error";
        when(compraService.compraEntradas(anyString(), anyLong(), anyLong()))
            .thenThrow(new RuntimeException(errorMessage));

        mockMvc.perform(post("/compras")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(compraDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error al crear la compra: " + errorMessage));
    }
}
