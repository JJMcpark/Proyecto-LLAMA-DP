package com.dpatrones.proyecto.patterns.decorator;

/**
 * PATRÓN DECORATOR - Decorador de Bordado
 * Añade un bordado personalizado con costo adicional.
 * 
 * El texto del bordado tiene un límite de 50 caracteres.
 */
public class BordadoDecorator extends ProductoDecorator {
    
    private static final Double COSTO_BORDADO = 20.00;
    private static final int MAX_CARACTERES = 50;
    
    private final String textoBordado;
    
    public BordadoDecorator(IProductoComponente producto) {
        super(producto);
        this.textoBordado = "Texto personalizado";
    }
    
    public BordadoDecorator(IProductoComponente producto, String textoBordado) {
        super(producto);
        // Validar y truncar texto si es necesario
        if (textoBordado == null || textoBordado.trim().isEmpty()) {
            this.textoBordado = "Texto personalizado";
        } else if (textoBordado.length() > MAX_CARACTERES) {
            this.textoBordado = textoBordado.substring(0, MAX_CARACTERES);
            System.out.println("[DECORATOR] Texto de bordado truncado a " + MAX_CARACTERES + " caracteres.");
        } else {
            this.textoBordado = textoBordado.trim();
        }
    }

    @Override
    public Double getPrecio() {
        return super.getPrecio() + COSTO_BORDADO;
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " + Bordado ('" + textoBordado + "')";
    }
    
    @Override
    public String getExtras() {
        String extrasAnteriores = super.getExtras();
        String nuevoExtra = "Bordado";
        return extrasAnteriores.isEmpty() ? nuevoExtra : extrasAnteriores + "," + nuevoExtra;
    }
    
    /**
     * Retorna el texto del bordado
     */
    public String getTextoBordado() {
        return textoBordado;
    }
    
    /**
     * Retorna el costo del bordado
     */
    public static Double getCostoBordado() {
        return COSTO_BORDADO;
    }
}
