package com.dpatrones.proyecto.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "productos")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer stock;
    
    // Atributos de la prenda
    private String talla;      // XS, S, M, L, XL, XXL
    private String color;
    private String tipoTela;   // Algodón, Poliéster, Lino, etc.
    private String categoria;  // Camiseta, Pantalón, Vestido, etc.
}
