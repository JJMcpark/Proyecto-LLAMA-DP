package com.dpatrones.proyecto.patterns.observer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class VentasSubject {
    
    private final List<VentasObserver> observadores;
    
    private VentasSubject() {
        this.observadores = new CopyOnWriteArrayList<>();
    }
    
    private static class Holder {
        private static final VentasSubject INSTANCE = new VentasSubject();
    }
    
    public static VentasSubject getInstance() {
        return Holder.INSTANCE;
    }
    
    public void agregarObservador(VentasObserver observador) {
        if (observador == null) {
            throw new IllegalArgumentException("El observador no puede ser nulo");
        }
        if (!observadores.contains(observador)) {
            observadores.add(observador);
            System.out.println("[OBSERVER] Registrado: " + observador.getNombre());
        }
    }
    
    public void eliminarObservador(VentasObserver observador) {
        observadores.remove(observador);
        System.out.println("[OBSERVER] Eliminado: " + observador.getNombre());
    }
    
    public void notificarObservadores(String mensaje) {
        System.out.println("[OBSERVER] Notificando a " + observadores.size() + " observadores...");
        for (VentasObserver observador : observadores) {
            observador.actualizar(mensaje);
        }
    }
    
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
