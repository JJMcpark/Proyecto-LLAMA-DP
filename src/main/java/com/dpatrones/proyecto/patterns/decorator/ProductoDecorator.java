package com.dpatrones.proyecto.patterns.decorator;

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
