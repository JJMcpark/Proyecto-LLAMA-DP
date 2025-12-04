package com.dpatrones.proyecto.patterns.state;

import com.dpatrones.proyecto.model.Pedido;

public class EnviadoState implements EstadoPedido {
    
    @Override
    public void siguienteEstado(Pedido pedido) {
        pedido.setEstadoActual(new EntregadoState());
        pedido.setEstado("ENTREGADO");
        System.out.println("[STATE] Pedido #" + pedido.getId() + ": ENVIADO -> ENTREGADO");
    }

    @Override
    public boolean cancelar(Pedido pedido) {
        System.out.println("[STATE] Pedido #" + pedido.getId() + " en camino. No se puede cancelar.");
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
