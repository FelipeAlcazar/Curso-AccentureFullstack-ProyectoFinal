package com.example.spring_compra.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Tarjetas")
public class Tarjeta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreTitular;
    private String numeroTarjeta;
    private String mesCaducidad;
    private String yearCaducidad;
    private String cvv;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreTitular() {
        return nombreTitular;
    }

    public void setNombreTitular(String nombreTitular) {
        this.nombreTitular = nombreTitular;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public String getMesCaducidad() {
        return mesCaducidad;
    }

    public void setMesCaducidad(String mesCaducidad) {
        this.mesCaducidad = mesCaducidad;
    }

    public String getYearCaducidad() {
        return yearCaducidad;
    }

    public void setYearCaducidad(String yearCaducidad) {
        this.yearCaducidad = yearCaducidad;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}
