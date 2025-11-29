package com.dpatrones.proyecto.patterns.observer;

/**
 * PATRÓN OBSERVER - Observador de Dashboard
 * 
 * Este observador simula el comportamiento de un panel de Dashboard
 * que se actualiza automáticamente cuando hay cambios en las ventas.
 * 
 * En una implementación real con Swing, este método repintaría
 * los componentes gráficos (tablas, gráficos, etc.)
 */
public class DashboardObserver implements VentasObserver {
    
    private String nombrePanel;
    
    public DashboardObserver(String nombrePanel) {
        this.nombrePanel = nombrePanel;
    }

    @Override
    public void actualizar(String mensaje) {
        System.out.println("[" + nombrePanel + "] ACTUALIZACIÓN RECIBIDA");
        System.out.println("   -> " + mensaje);
        System.out.println("   Actualizando componentes gráficos...");
    }

    @Override
    public String getNombre() {
        return nombrePanel;
    }
}
