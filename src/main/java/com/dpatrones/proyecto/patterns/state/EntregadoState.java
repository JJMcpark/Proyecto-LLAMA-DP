package com.dpatrones.proyecto.patterns.state;

import com.dpatrones.proyecto.model.Pedido;

/**
 * PATRÓN STATE - Estado ENTREGADO
 * Estado final del pedido. No puede avanzar ni cancelarse.
 */
public class EntregadoState implements EstadoPedido {
    
    @Override
    public void siguienteEstado(Pedido pedido) {
        System.out.println("[LOGÍSTICA] Pedido #" + pedido.getId() + " ya fue entregado. Ciclo completado.");
    }

    @Override
    public boolean cancelar(Pedido pedido) {
        System.out.println("[ERROR] Pedido #" + pedido.getId() + " ya fue entregado. Use devolución.");
        return false;
    }

    @Override
    public String getNombre() {
        return "ENTREGADO";
    }
    
    @Override
    public boolean puedeModificarse() {
        return false;
    }
}
