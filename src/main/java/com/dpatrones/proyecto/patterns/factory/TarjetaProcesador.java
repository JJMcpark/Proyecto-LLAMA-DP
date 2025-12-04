package com.dpatrones.proyecto.patterns.factory;

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
        System.out.println("[FACTORY] Procesando S/." + monto + " con TARJETA...");
        System.out.println("[FACTORY] Tarjeta: ****" + (numeroTarjeta != null ? numeroTarjeta.substring(Math.max(0, numeroTarjeta.length() - 4)) : "0000"));
        System.out.println("[FACTORY] Pago con tarjeta APROBADO.");
        return true;
    }

    @Override
    public String getNombre() {
        return "TARJETA";
    }

    @Override
    public boolean validarDatos() {
        return true;
    }
}
