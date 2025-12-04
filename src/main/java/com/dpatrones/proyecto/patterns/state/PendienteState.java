package com.dpatrones.proyecto.patterns.state;

import com.dpatrones.proyecto.model.Pedido;

public class PendienteState implements EstadoPedido {
    
    @Override
    public void siguienteEstado(Pedido pedido) {
        pedido.setEstadoActual(new PagadoState());
        pedido.setEstado("PAGADO");
        System.out.println("[STATE] Pedido #" + pedido.getId() + ": PENDIENTE -> PAGADO");
    }

    @Override
    public boolean cancelar(Pedido pedido) {
        pedido.setEstado("CANCELADO");
        System.out.println("[STATE] Pedido #" + pedido.getId() + " CANCELADO");
        return true;
    }

    @Override
    public String getNombre() {
        return "PENDIENTE";
    }
    
    @Override
    public boolean puedeModificarse() {
        return true;
    }
}
