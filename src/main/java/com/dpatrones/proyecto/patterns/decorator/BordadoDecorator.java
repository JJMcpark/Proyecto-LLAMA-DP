package com.dpatrones.proyecto.patterns.decorator;

/**
 * PATRÓN DECORATOR - Decorador de Bordado
 * Añade un bordado personalizado con costo adicional.
 */
public class BordadoDecorator extends ProductoDecorator {
    
    private static final Double COSTO_BORDADO = 20.00;
    private String textoBordado;
    
    public BordadoDecorator(IProductoComponente producto) {
        super(producto);
        this.textoBordado = "Texto personalizado";
    }
    
    public BordadoDecorator(IProductoComponente producto, String textoBordado) {
        super(producto);
        this.textoBordado = textoBordado;
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
}
