package com.dpatrones.proyecto.patterns.decorator;

/**
 * PATRÓN DECORATOR - Clase abstracta Decorador
 * Clase base para todos los decoradores de producto.
 */
public abstract class ProductoDecorator implements IProductoComponente {
    
    protected IProductoComponente productoDecorado;
    
    public ProductoDecorator(IProductoComponente producto) {
        this.productoDecorado = producto;
    }

    @Override
    public Double getPrecio() {
        return productoDecorado.getPrecio();
    }

    @Override
    public String getDescripcion() {
        return productoDecorado.getDescripcion();
    }
    
    @Override
    public String getExtras() {
        return productoDecorado.getExtras();
    }
}
