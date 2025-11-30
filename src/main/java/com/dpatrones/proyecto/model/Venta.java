package com.dpatrones.proyecto.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "ventas")
@Data
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id")
    private Long usuarioId; // por ahora será null (sin login)

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

}