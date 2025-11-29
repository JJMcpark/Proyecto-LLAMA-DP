package com.dpatrones.proyecto.patterns.decorator;

/**
 * PATRÓN DECORATOR - Decorador de Empaque de Regalo
 * Añade empaque especial para regalo con costo adicional.
 */
public class EmpaqueRegaloDecorator extends ProductoDecorator {
    
    private static final Double COSTO_EMPAQUE = 10.00;
    private String mensajeRegalo;
    
    public EmpaqueRegaloDecorator(IProductoComponente producto) {
        super(producto);
        this.mensajeRegalo = "";
    }
    
    public EmpaqueRegaloDecorator(IProductoComponente producto, String mensajeRegalo) {
        super(producto);
        this.mensajeRegalo = mensajeRegalo;
    }

    @Override
    public Double getPrecio() {
        return super.getPrecio() + COSTO_EMPAQUE;
    }

    @Override
    public String getDescripcion() {
        String desc = super.getDescripcion() + " + Empaque Regalo";
        if (mensajeRegalo != null && !mensajeRegalo.isEmpty()) {
            desc += " (Mensaje: '" + mensajeRegalo + "')";
        }
        return desc;
    }
    
    @Override
    public String getExtras() {
        String extrasAnteriores = super.getExtras();
        String nuevoExtra = "EmpaqueRegalo";
        return extrasAnteriores.isEmpty() ? nuevoExtra : extrasAnteriores + "," + nuevoExtra;
    }
}
