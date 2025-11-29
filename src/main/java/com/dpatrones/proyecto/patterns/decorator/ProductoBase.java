package com.dpatrones.proyecto.patterns.decorator;

import com.dpatrones.proyecto.model.Producto;

/**
 * PATRÓN DECORATOR - Componente concreto base
 * Envuelve un Producto de la BD y lo hace compatible con el patrón Decorator.
 */
public class ProductoBase implements IProductoComponente {
    
    private Producto producto;
    
    public ProductoBase(Producto producto) {
        this.producto = producto;
    }

    @Override
    public Double getPrecio() {
        return producto.getPrecio();
    }

    @Override
    public String getDescripcion() {
        return producto.getNombre() + " - " + producto.getColor() + " - Talla " + producto.getTalla();
    }

    @Override
    public String getExtras() {
        return ""; // Sin extras en el producto base
    }
    
    public Producto getProductoOriginal() {
        return this.producto;
    }
}
