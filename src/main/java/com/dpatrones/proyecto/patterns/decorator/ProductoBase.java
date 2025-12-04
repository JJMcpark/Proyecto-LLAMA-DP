package com.dpatrones.proyecto.patterns.decorator;

import com.dpatrones.proyecto.model.Producto;

public class ProductoBase implements IProductoComponente {
    
    private final Producto producto;
    
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
        return "";
    }
    
    public Producto getProductoOriginal() {
        return this.producto;
    }
}
