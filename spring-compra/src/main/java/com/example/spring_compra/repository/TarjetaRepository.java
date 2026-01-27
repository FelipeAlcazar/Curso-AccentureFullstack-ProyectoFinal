package com.example.spring_compra.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.spring_compra.model.Tarjeta;

public interface TarjetaRepository extends JpaRepository<Tarjeta, Long> {

}
