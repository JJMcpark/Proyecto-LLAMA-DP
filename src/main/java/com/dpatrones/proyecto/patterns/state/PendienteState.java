package com.dpatrones.proyecto.patterns.state;

import com.dpatrones.proyecto.model.Pedido;

/**
 * PATRÓN STATE - Estado PENDIENTE
 * Estado inicial del pedido. Puede cancelarse y avanzar a PAGADO.
 */
public class PendienteState implements EstadoPedido {
    
    @Override
    public void siguienteEstado(Pedido pedido) {
        pedido.setEstadoActual(new PagadoState());
        pedido.setEstado("PAGADO");
        System.out.println("[LOGÍSTICA] Pedido #" + pedido.getId() + " -> PAGADO. Preparando para envío.");
    }

    @Override
    public boolean cancelar(Pedido pedido) {
        pedido.setEstado("CANCELADO");
        System.out.println("[LOGÍSTICA] Pedido #" + pedido.getId() + " CANCELADO por el usuario.");
        return true;
    }

    @Override
    public String getNombre() {
        return "PENDIENTE";
    }
    
    @Override
    public boolean puedeModificarse() {
        return true; // El pedido puede modificarse mientras no se pague
    }
}
