package com.dpatrones.proyecto.patterns.decorator;

/**
 * PATRÓN DECORATOR - Interface base para productos
 * Define el contrato que tanto el producto base como los decoradores deben cumplir.
 */
public interface IProductoComponente {
    
    /**
     * Retorna el precio total del producto (incluyendo extras)
     */
    Double getPrecio();
    
    /**
     * Retorna la descripción del producto con sus extras
     */
    String getDescripcion();
    
    /**
     * Retorna solo los extras aplicados
     */
    String getExtras();
}
