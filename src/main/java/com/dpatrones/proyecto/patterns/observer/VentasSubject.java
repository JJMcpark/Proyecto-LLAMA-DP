package com.dpatrones.proyecto.patterns.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * PATRÓN OBSERVER - Subject (Sujeto)
 * 
 * Esta clase mantiene una lista de observadores y los notifica
 * cuando ocurren cambios en las ventas o pedidos.
 * 
 * Los paneles de Swing (Dashboard, Inventario, etc.) se registran
 * como observadores para actualizarse automáticamente.
 */
public class VentasSubject {
    
    private static VentasSubject instance;
    
    private List<VentasObserver> observadores;
    
    private VentasSubject() {
        this.observadores = new ArrayList<>();
    }
    
    public static VentasSubject getInstance() {
        if (instance == null) {
            instance = new VentasSubject();
        }
        return instance;
    }
    
    /**
     * Registra un nuevo observador
     */
    public void agregarObservador(VentasObserver observador) {
        observadores.add(observador);
        System.out.println("[OBSERVER] Observador registrado: " + observador.getNombre());
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
