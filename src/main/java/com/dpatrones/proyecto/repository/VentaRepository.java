package com.dpatrones.proyecto.repository;

import com.dpatrones.proyecto.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaRepository extends JpaRepository<Venta, Long> {
}
