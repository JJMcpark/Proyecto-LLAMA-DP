package com.dpatrones.proyecto.design.observer;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * PATRÓN OBSERVER - Observable
 * Notifica cuando ocurren eventos (comentarios, likes, etc.)
 *
 * Esta clase se registra como bean de Spring para inyección en servicios.
 */
@Component
public class PostEventManager {

    private final List<NotificationObserver> observers = new CopyOnWriteArrayList<>();

    public void subscribe(NotificationObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void unsubscribe(NotificationObserver observer) {
        observers.remove(observer);
    }

    public void notifyCommentAdded(String postTitle, String commenterName) {
        String message = commenterName + " comentó en: " + postTitle;
        notifyAllObservers(message, "COMMENT");
    }

    public void notifyLikeAdded(String postTitle, String userName) {
        String message = userName + " le gustó tu post: " + postTitle;
        notifyAllObservers(message, "LIKE");
    }

    public void notifyFollowed(String userName) {
        String message = userName + " te está siguiendo";
        notifyAllObservers(message, "FOLLOW");
    }

    private void notifyAllObservers(String message, String type) {
        for (NotificationObserver observer : observers) {
            try {
                observer.update(message, type);
            } catch (Exception e) {
                // Log and continue notifying others (avoid breaking the loop)
                // Using System.err to avoid adding logging dependency here
                System.err.println("Error notifying observer: " + e.getMessage());
            }
        }
    }
}
