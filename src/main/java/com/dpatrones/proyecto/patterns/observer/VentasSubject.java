package com.dpatrones.proyecto.patterns.observer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * PATRÓN OBSERVER - Subject (Sujeto)
 * 
 * Esta clase mantiene una lista de observadores y los notifica
 * cuando ocurren cambios en las ventas o pedidos.
 * 
 * Los paneles de Swing (Dashboard, Inventario, etc.) se registran
 * como observadores para actualizarse automáticamente.
 * 
 * Implementación thread-safe usando el patrón Holder (Bill Pugh Singleton).
 */
public class VentasSubject {
    
    // CopyOnWriteArrayList es thread-safe para iteración concurrente
    private final List<VentasObserver> observadores;
    
    private VentasSubject() {
        this.observadores = new CopyOnWriteArrayList<>();
    }
    
    /**
     * Holder interno - se carga solo cuando se llama getInstance()
     * Es thread-safe sin necesidad de synchronized
     */
    private static class Holder {
        private static final VentasSubject INSTANCE = new VentasSubject();
    }
    
    /**
     * Obtiene la instancia única
     */
    public static VentasSubject getInstance() {
        return Holder.INSTANCE;
    }
    
    /**
     * Registra un nuevo observador (evita duplicados)
     */
    public void agregarObservador(VentasObserver observador) {
        if (observador == null) {
            throw new IllegalArgumentException("El observador no puede ser nulo");
        }
        if (!observadores.contains(observador)) {
            observadores.add(observador);
            System.out.println("[OBSERVER] Observador registrado: " + observador.getNombre());
        } else {
            System.out.println("[OBSERVER] Observador ya registrado: " + observador.getNombre());
        }
    }
    
    /**
     * Elimina un observador de la lista
     */
    public void eliminarObservador(VentasObserver observador) {
        observadores.remove(observador);
        System.out.println("[OBSERVER] Observador eliminado: " + observador.getNombre());
    }
    
    /**
     * Notifica a TODOS los observadores de un cambio
     */
    public void notificarObservadores(String mensaje) {
        System.out.println("[OBSERVER] Notificando a " + observadores.size() + " observadores...");
        for (VentasObserver observador : observadores) {
            observador.actualizar(mensaje);
        }
    }
    
    /**
     * Métodos de conveniencia para notificar eventos específicos
     */
    public void notificarNuevaVenta(Long pedidoId, Double monto) {
        notificarObservadores("NUEVA_VENTA: Pedido #" + pedidoId + " por S/." + monto);
    }
    
    public void notificarCambioEstado(Long pedidoId, String nuevoEstado) {
        notificarObservadores("CAMBIO_ESTADO: Pedido #" + pedidoId + " -> " + nuevoEstado);
    }
    
    public void notificarStockBajo(Long productoId, String nombreProducto, int stockActual) {
        notificarObservadores("STOCK_BAJO: " + nombreProducto + " (ID:" + productoId + ") - Stock: " + stockActual);
    }
    
    public int getCantidadObservadores() {
        return observadores.size();
    }
}
