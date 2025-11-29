package com.dpatrones.proyecto.patterns.observer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * PATRÓN OBSERVER - Observador de Inventario
 * 
 * Se actualiza cuando hay cambios que afectan el inventario
 * (ventas, stock bajo, nuevos productos, etc.)
 * 
 * Detecta automáticamente eventos de stock bajo para alertar.
 */
public class InventarioObserver implements VentasObserver {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private int alertasStockBajo;

    public InventarioObserver() {
        this.alertasStockBajo = 0;
    }

    @Override
    public void actualizar(String mensaje) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        System.out.println("[" + timestamp + "] [PANEL INVENTARIO] Notificación recibida");
        System.out.println("   -> " + mensaje);
        
        // Detectar alertas de stock bajo
        if (mensaje.contains("STOCK_BAJO")) {
            alertasStockBajo++;
            System.out.println("   ⚠️ ALERTA: Stock bajo detectado. Total alertas: " + alertasStockBajo);
        }
        
        System.out.println("   Recargando datos de inventario...");
    }

    @Override
    public String getNombre() {
        return "Panel de Inventario";
    }
    
    /**
     * Retorna el número de alertas de stock bajo recibidas
     */
    public int getAlertasStockBajo() {
        return alertasStockBajo;
    }
    
    /**
     * Reinicia el contador de alertas
     */
    public void resetearAlertas() {
        this.alertasStockBajo = 0;
    }
}
