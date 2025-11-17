package com.dpatrones.proyecto.design.observer;

/**
 * PATRÓN OBSERVER
 * Interface para observar cambios (notificaciones)
 */
public interface NotificationObserver {
    void update(String message, String type);
}
