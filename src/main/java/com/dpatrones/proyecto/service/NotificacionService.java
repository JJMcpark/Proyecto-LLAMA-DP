package com.dpatrones.proyecto.service;

import org.springframework.stereotype.Service;

@Service
public class NotificacionService {

    public void enviarConfirmacionPedido(String email, Long pedidoId) {
        System.out.println("[EMAIL] Pedido #" + pedidoId + " confirmado -> " + email);
    }

    public void enviarActualizacionEstado(String email, Long pedidoId, String nuevoEstado) {
        System.out.println("[EMAIL] Pedido #" + pedidoId + " estado: " + nuevoEstado + " -> " + email);
    }

    public void enviarCodigoSeguimiento(String email, Long pedidoId, String codigo) {
        System.out.println("[EMAIL] Pedido #" + pedidoId + " tracking: " + codigo + " -> " + email);
    }
}
