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
     * Retorna solo los extras aplicados (separados por coma)
     */
    String getExtras();
    
    /**
     * Retorna el precio formateado con símbolo de moneda
     */
    default String getPrecioFormateado() {
        return "S/." + String.format("%.2f", getPrecio());
    }
    
    /**
     * Verifica si tiene algún extra aplicado
     */
    default boolean tieneExtras() {
        String extras = getExtras();
        return extras != null && !extras.isEmpty();
    }
    
    /**
     * Cuenta cuántos extras tiene aplicados
     */
    default int contarExtras() {
        String extras = getExtras();
        if (extras == null || extras.isEmpty()) {
            return 0;
        }
        return extras.split(",").length;
    }
}
