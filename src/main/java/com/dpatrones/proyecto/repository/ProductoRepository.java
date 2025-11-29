package com.dpatrones.proyecto.repository;

import com.dpatrones.proyecto.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    List<Producto> findByCategoria(String categoria);
    
    List<Producto> findByTalla(String talla);
    
    List<Producto> findByColor(String color);
    
    List<Producto> findByStockGreaterThan(Integer minStock);
    
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
}
