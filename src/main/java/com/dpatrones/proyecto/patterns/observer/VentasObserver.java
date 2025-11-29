package com.dpatrones.proyecto.patterns.observer;

/**
 * PATRÓN OBSERVER - Interface Observer
 * 
 * Define el contrato que deben implementar todos los observadores
 * que quieran ser notificados de cambios en las ventas/pedidos.
 */
public interface VentasObserver {
    
    /**
     * Método llamado cuando hay una actualización en las ventas
     * @param mensaje descripción del cambio ocurrido
     */
    void actualizar(String mensaje);
    
    /**
     * Retorna el nombre del observador (para identificación)
     */
    String getNombre();
}
