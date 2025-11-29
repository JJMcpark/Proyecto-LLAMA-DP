package com.dpatrones.proyecto.patterns.decorator;

/**
 * PATRÓN DECORATOR - Decorador de Estampado Personalizado
 * Añade un estampado al producto con costo adicional.
 * 
 * Soporta diferentes tipos de diseños: personalizados, logos, imágenes.
 */
public class EstampadoDecorator extends ProductoDecorator {
    
    private static final Double COSTO_ESTAMPADO = 15.00;
    
    private final String disenoEstampado;
    
    public EstampadoDecorator(IProductoComponente producto) {
        super(producto);
        this.disenoEstampado = "Diseño estándar";
    }
    
    public EstampadoDecorator(IProductoComponente producto, String disenoEstampado) {
        super(producto);
        this.disenoEstampado = (disenoEstampado != null && !disenoEstampado.trim().isEmpty()) 
            ? disenoEstampado.trim() 
            : "Diseño estándar";
    }

    @Override
    public Double getPrecio() {
        return super.getPrecio() + COSTO_ESTAMPADO;
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " + Estampado (" + disenoEstampado + ")";
    }
    
    @Override
    public String getExtras() {
        String extrasAnteriores = super.getExtras();
        String nuevoExtra = "Estampado";
        return extrasAnteriores.isEmpty() ? nuevoExtra : extrasAnteriores + "," + nuevoExtra;
    }
    
    /**
     * Retorna el diseño del estampado
     */
    public String getDisenoEstampado() {
        return disenoEstampado;
    }
    
    /**
     * Retorna el costo del estampado
     */
    public static Double getCostoEstampado() {
        return COSTO_ESTAMPADO;
    }
}
