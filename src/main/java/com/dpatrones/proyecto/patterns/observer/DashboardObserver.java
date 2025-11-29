package com.dpatrones.proyecto.patterns.observer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    
    private final String nombrePanel;
    private int contadorNotificaciones;
    
    public DashboardObserver(String nombrePanel) {
        this.nombrePanel = nombrePanel;
        this.contadorNotificaciones = 0;
    }

    @Override
    public void actualizar(String mensaje) {
        contadorNotificaciones++;
        String timestamp = LocalDateTime.now().format(FORMATTER);
        
        System.out.println("[" + timestamp + "] [" + nombrePanel + "] ACTUALIZACIÓN #" + contadorNotificaciones);
        System.out.println("   -> " + mensaje);
        System.out.println("   Actualizando componentes gráficos...");
    }

    @Override
    public String getNombre() {
        return nombrePanel;
    }
    
    /**
     * Retorna el número de notificaciones recibidas
     */
    public int getContadorNotificaciones() {
        return contadorNotificaciones;
    }
    
    /**
     * Reinicia el contador de notificaciones
     */
    public void resetearContador() {
        this.contadorNotificaciones = 0;
    }
}
