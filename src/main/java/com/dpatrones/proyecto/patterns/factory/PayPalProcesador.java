package com.dpatrones.proyecto.patterns.factory;

/**
 * PATRÓN FACTORY - Procesador de Pago PayPal
 */
public class PayPalProcesador implements IProcesadorPago {
    
    private String emailPayPal;
    
    public PayPalProcesador() {
    }
    
    public PayPalProcesador(String emailPayPal) {
        this.emailPayPal = emailPayPal;
    }

    @Override
    public boolean procesarPago(Double monto) {
        System.out.println("[PAGO] Procesando S/." + monto + " con PAYPAL...");
        System.out.println("[PAGO] Redirigiendo a PayPal...");
        System.out.println("[PAGO] Pago PayPal APROBADO.");
        return true;
    }

    @Override
    public String getNombre() {
        return "PAYPAL";
    }

    @Override
    public boolean validarDatos() {
        System.out.println("[PAGO] Validando cuenta PayPal...");
        return true;
    }
}
