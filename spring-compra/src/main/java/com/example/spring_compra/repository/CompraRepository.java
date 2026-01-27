package com.example.spring_compra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.spring_compra.model.Compra;

public interface CompraRepository extends JpaRepository<Compra, Long> {

}
