package com.dpatrones.proyecto.patterns.observer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InventarioObserver implements VentasObserver {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private int alertasStockBajo;

    public InventarioObserver() {
        this.alertasStockBajo = 0;
    }

    @Override
    public void actualizar(String mensaje) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        System.out.println("[" + timestamp + "] [OBSERVER-Inventario] " + mensaje);
        
        if (mensaje.contains("STOCK_BAJO")) {
            alertasStockBajo++;
            System.out.println("   ALERTA: Stock bajo detectado. Total alertas: " + alertasStockBajo);
        }
    }

    @Override
    public String getNombre() {
        return "Inventario";
    }
    
    public int getAlertasStockBajo() {
        return alertasStockBajo;
    }
}
