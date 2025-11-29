package com.dpatrones.proyecto.patterns.factory;

/**
 * PATRÓN FACTORY METHOD - Fábrica de Procesadores de Pago
 * 
 * Esta clase encapsula la lógica de creación de procesadores de pago.
 * Evita el uso de múltiples if-else en el código cliente.
 * 
 * Uso: PaymentFactory.crearProcesador("PAYPAL").procesarPago(100.0);
 */
public class PaymentFactory {
    
    /**
     * Crea el procesador de pago según el tipo indicado
     * @param tipoPago tipo de pago: TARJETA, PAYPAL, CONTRAENTREGA
     * @return instancia del procesador correspondiente
     */
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
    
    /**
     * Crea procesador de tarjeta con datos específicos
     */
    public static IProcesadorPago crearProcesadorTarjeta(String numero, String cvv, String expiracion) {
        return new TarjetaProcesador(numero, cvv, expiracion);
    }
    
    /**
     * Crea procesador PayPal con email
     */
    public static IProcesadorPago crearProcesadorPayPal(String email) {
        return new PayPalProcesador(email);
    }
    
    /**
     * Crea procesador de contraentrega con método específico
     * @param metodo EFECTIVO, YAPE, PLIN
     * @param telefono teléfono de contacto del cliente
     */
    public static IProcesadorPago crearProcesadorContraentrega(String metodo, String telefono) {
        return new ContraentregaProcesador(metodo, telefono);
    }
    
    /**
     * Verifica si un tipo de pago es válido
     */
    public static boolean esTipoPagoValido(String tipoPago) {
        if (tipoPago == null) return false;
        return switch (tipoPago.toUpperCase()) {
            case "TARJETA", "PAYPAL", "CONTRAENTREGA" -> true;
            default -> false;
        };
    }
    
    /**
     * Obtiene lista de tipos de pago disponibles
     */
    public static String[] getTiposPagoDisponibles() {
        return new String[]{"TARJETA", "PAYPAL", "CONTRAENTREGA"};
    }
}
