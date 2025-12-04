package com.dpatrones.proyecto.patterns.decorator;

public interface IProductoComponente {
    
    Double getPrecio();
    
    String getDescripcion();
    
    String getExtras();
    
    default String getPrecioFormateado() {
        return "S/." + String.format("%.2f", getPrecio());
    }
    
    default boolean tieneExtras() {
        String extras = getExtras();
        return extras != null && !extras.isEmpty();
    }
    
    default int contarExtras() {
        String extras = getExtras();
        if (extras == null || extras.isEmpty()) {
            return 0;
        }
        return extras.split(",").length;
    }
}
