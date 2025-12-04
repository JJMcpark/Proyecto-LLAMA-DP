package com.dpatrones.proyecto.patterns.factory;

public class PayPalProcesador implements IProcesadorPago {
    
    private String emailPayPal;
    
    public PayPalProcesador() {
    }
    
    public PayPalProcesador(String emailPayPal) {
        this.emailPayPal = emailPayPal;
    }

    @Override
    public boolean procesarPago(Double monto) {
        System.out.println("[FACTORY] Procesando S/." + monto + " con PAYPAL...");
        System.out.println("[FACTORY] Cuenta: " + (emailPayPal != null ? emailPayPal : "No especificada"));
        System.out.println("[FACTORY] Pago PayPal APROBADO.");
        return true;
    }

    @Override
    public String getNombre() {
        return "PAYPAL";
    }

    @Override
    public boolean validarDatos() {
        return true;
    }
}
