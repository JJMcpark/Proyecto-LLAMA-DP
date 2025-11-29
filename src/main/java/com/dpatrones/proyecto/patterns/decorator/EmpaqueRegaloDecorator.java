package com.dpatrones.proyecto.patterns.decorator;

/**
 * PATRÓN DECORATOR - Decorador de Empaque de Regalo
 * Añade empaque especial para regalo con costo adicional.
 * 
 * Incluye caja decorativa, moño y tarjeta con mensaje personalizado.
 */
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
        // Validar y truncar mensaje si es necesario
        if (mensajeRegalo == null) {
            this.mensajeRegalo = "";
        } else if (mensajeRegalo.length() > MAX_MENSAJE) {
            this.mensajeRegalo = mensajeRegalo.substring(0, MAX_MENSAJE);
            System.out.println("[DECORATOR] Mensaje de regalo truncado a " + MAX_MENSAJE + " caracteres.");
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
    
    /**
     * Retorna el mensaje de regalo
     */
    public String getMensajeRegalo() {
        return mensajeRegalo;
    }
    
    /**
     * Verifica si tiene mensaje personalizado
     */
    public boolean tieneMensaje() {
        return mensajeRegalo != null && !mensajeRegalo.isEmpty();
    }
    
    /**
     * Retorna el costo del empaque
     */
    public static Double getCostoEmpaque() {
        return COSTO_EMPAQUE;
    }
}
