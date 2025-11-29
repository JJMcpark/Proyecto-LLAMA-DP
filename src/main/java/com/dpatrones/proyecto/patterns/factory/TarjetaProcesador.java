package com.dpatrones.proyecto.patterns.factory;

/**
 * PATRÓN FACTORY - Procesador de Pago con Tarjeta
 */
public class TarjetaProcesador implements IProcesadorPago {
    
    private String numeroTarjeta;
    private String cvv;
    private String fechaExpiracion;
    
    public TarjetaProcesador() {
    }
    
    public TarjetaProcesador(String numeroTarjeta, String cvv, String fechaExpiracion) {
        this.numeroTarjeta = numeroTarjeta;
        this.cvv = cvv;
        this.fechaExpiracion = fechaExpiracion;
    }

    @Override
    public boolean procesarPago(Double monto) {
        System.out.println("[PAGO] Procesando S/." + monto + " con TARJETA...");
        System.out.println("[PAGO] Tarjeta: ****" + (numeroTarjeta != null ? numeroTarjeta.substring(Math.max(0, numeroTarjeta.length() - 4)) : "0000"));
        System.out.println("[PAGO] Conectando con banco...");
        System.out.println("[PAGO] Pago con tarjeta APROBADO.");
        return true;
    }

    @Override
    public String getNombre() {
        return "TARJETA";
    }

    @Override
    public boolean validarDatos() {
        System.out.println("[PAGO] Validando datos de tarjeta...");
        if (numeroTarjeta == null || numeroTarjeta.isEmpty()) {
            System.out.println("[PAGO] Advertencia: Número de tarjeta no especificado");
        }
        if (cvv == null || cvv.isEmpty()) {
            System.out.println("[PAGO] Advertencia: CVV no especificado");
        }
        if (fechaExpiracion == null || fechaExpiracion.isEmpty()) {
            System.out.println("[PAGO] Advertencia: Fecha de expiración no especificada");
        }
        return true;
    }
}
