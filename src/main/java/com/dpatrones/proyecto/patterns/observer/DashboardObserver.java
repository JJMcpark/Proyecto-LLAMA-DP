package com.dpatrones.proyecto.patterns.observer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        System.out.println("[" + timestamp + "] [OBSERVER-" + nombrePanel + "] Notificación #" + contadorNotificaciones + ": " + mensaje);
    }

    @Override
    public String getNombre() {
        return nombrePanel;
    }
    
    public int getContadorNotificaciones() {
        return contadorNotificaciones;
    }
}
