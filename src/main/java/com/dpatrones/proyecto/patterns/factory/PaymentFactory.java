package com.dpatrones.proyecto.patterns.factory;

public class PaymentFactory {
    
    public static IProcesadorPago crearProcesador(String tipoPago) {
        if (tipoPago == null) {
            throw new IllegalArgumentException("El tipo de pago no puede ser nulo");
        }
        
        return switch (tipoPago.toUpperCase()) {
            case "TARJETA" -> new TarjetaProcesador();
            case "PAYPAL" -> new PayPalProcesador();
            case "CONTRAENTREGA" -> new ContraentregaProcesador();
            default -> throw new IllegalArgumentException("Tipo de pago no soportado: " + tipoPago);
        };
    }
    
    public static IProcesadorPago crearProcesadorTarjeta(String numero, String cvv, String expiracion) {
        return new TarjetaProcesador(numero, cvv, expiracion);
    }
    
    public static IProcesadorPago crearProcesadorPayPal(String email) {
        return new PayPalProcesador(email);
    }
    
    public static IProcesadorPago crearProcesadorContraentrega(String metodo, String telefono) {
        return new ContraentregaProcesador(metodo, telefono);
    }
    
    public static boolean esTipoPagoValido(String tipoPago) {
        if (tipoPago == null) return false;
        return switch (tipoPago.toUpperCase()) {
            case "TARJETA", "PAYPAL", "CONTRAENTREGA" -> true;
            default -> false;
        };
    }
    
    public static String[] getTiposPagoDisponibles() {
        return new String[]{"TARJETA", "PAYPAL", "CONTRAENTREGA"};
    }
}
