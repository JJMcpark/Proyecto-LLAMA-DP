package com.dpatrones.proyecto.patterns.state;

import com.dpatrones.proyecto.model.Pedido;

/**
 * PATRÓN STATE - Estado ENVIADO
 * El pedido está en tránsito. Avanza a ENTREGADO.
 */
public class EnviadoState implements EstadoPedido {
    
    @Override
    public void siguienteEstado(Pedido pedido) {
        pedido.setEstadoActual(new EntregadoState());
        pedido.setEstado("ENTREGADO");
        System.out.println("[LOGÍSTICA] Pedido #" + pedido.getId() + " -> ENTREGADO al cliente.");
    }

    @Override
    public boolean cancelar(Pedido pedido) {
        System.out.println("[ERROR] Pedido #" + pedido.getId() + " ya está en camino. No se puede cancelar.");
        return false;
    }

    @Override
    public String getNombre() {
        return "ENVIADO";
    }
    
    @Override
    public boolean puedeModificarse() {
        return false;
    }
}
