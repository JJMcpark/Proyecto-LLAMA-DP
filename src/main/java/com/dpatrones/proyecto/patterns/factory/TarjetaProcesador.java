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
        return true;
    }
}
