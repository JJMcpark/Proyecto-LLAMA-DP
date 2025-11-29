package com.dpatrones.proyecto.patterns.observer;

/**
 * PATRÓN OBSERVER - Observador de Inventario
 * 
 * Se actualiza cuando hay cambios que afectan el inventario
 * (ventas, stock bajo, nuevos productos, etc.)
 */
public class InventarioObserver implements VentasObserver {

    @Override
    public void actualizar(String mensaje) {
        System.out.println("[PANEL INVENTARIO] Notificación recibida");
        System.out.println("   -> " + mensaje);
        System.out.println("   Recargando datos de inventario...");
    }

    @Override
    public String getNombre() {
        return "Panel de Inventario";
    }
}
