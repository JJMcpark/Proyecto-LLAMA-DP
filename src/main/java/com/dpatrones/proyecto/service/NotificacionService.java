package com.dpatrones.proyecto.service;

import org.springframework.stereotype.Service;

/**
 * Servicio de Notificaciones (simplificado).
 * En un proyecto real, aquí se enviarían emails o SMS.
 */
@Service
public class NotificacionService {
    
    public void enviarConfirmacionPedido(String email, Long pedidoId) {
        System.out.println("[NOTIFICACIÓN] Email enviado a " + email);
        System.out.println("   -> Su pedido #" + pedidoId + " ha sido confirmado.");
    }
    
    public void enviarActualizacionEstado(String email, Long pedidoId, String nuevoEstado) {
        System.out.println("[NOTIFICACIÓN] Email enviado a " + email);
        System.out.println("   -> Su pedido #" + pedidoId + " ahora está: " + nuevoEstado);
    }
    
    public void enviarCodigoSeguimiento(String email, Long pedidoId, String codigoSeguimiento) {
        System.out.println("[NOTIFICACIÓN] Email enviado a " + email);
        System.out.println("   -> Código de seguimiento para pedido #" + pedidoId + ": " + codigoSeguimiento);
    }
}
