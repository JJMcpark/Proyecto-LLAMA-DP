package com.dpatrones.proyecto.patterns.factory;

/**
 * PATRÓN FACTORY METHOD - Interface Procesador de Pago
 * Define el contrato que todos los procesadores de pago deben cumplir.
 */
public interface IProcesadorPago {
    
    /**
     * Procesa el pago por el monto indicado
     * @param monto cantidad a cobrar
     * @return true si el pago fue exitoso
     */
    boolean procesarPago(Double monto);
    
    /**
     * Retorna el nombre del método de pago
     */
    String getNombre();
    
    /**
     * Valida los datos del pago antes de procesar
     */
    boolean validarDatos();
}
