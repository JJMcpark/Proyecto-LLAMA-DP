package com.dpatrones.proyecto.patterns.decorator;

public class EmpaqueRegaloDecorator extends ProductoDecorator {
    
    private static final Double COSTO_EMPAQUE = 10.00;
    private static final int MAX_MENSAJE = 100;
    
    private final String mensajeRegalo;
    
    public EmpaqueRegaloDecorator(IProductoComponente producto) {
        super(producto);
        this.mensajeRegalo = "";
    }
    
    public EmpaqueRegaloDecorator(IProductoComponente producto, String mensajeRegalo) {
        super(producto);
        if (mensajeRegalo == null) {
            this.mensajeRegalo = "";
        } else if (mensajeRegalo.length() > MAX_MENSAJE) {
            this.mensajeRegalo = mensajeRegalo.substring(0, MAX_MENSAJE);
        } else {
            this.mensajeRegalo = mensajeRegalo.trim();
        }
    }

    @Override
    public Double getPrecio() {
        return super.getPrecio() + COSTO_EMPAQUE;
    }

    @Override
    public String getDescripcion() {
        String desc = super.getDescripcion() + " + Empaque Regalo";
        if (tieneMensaje()) {
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
    
    public String getMensajeRegalo() {
        return mensajeRegalo;
    }
    
    public boolean tieneMensaje() {
        return mensajeRegalo != null && !mensajeRegalo.isEmpty();
    }
    
    public static Double getCostoEmpaque() {
        return COSTO_EMPAQUE;
    }
}
