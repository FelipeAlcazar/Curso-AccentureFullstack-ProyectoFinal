package com.example.spring_compra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI CompraOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("API Compras")
                .description("Documentación de la API de compras")
                .version("v1.0")
                .contact(new Contact().name("Felipe Alcázar").
                        url("https://felipealcazar.com").email("felipe.alcazar@accenture.com"))
                .license(new License().name("LICENSE").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                .description("Descripcion del proyecto")
                .url("https://miproyecto.es"));
    }
}