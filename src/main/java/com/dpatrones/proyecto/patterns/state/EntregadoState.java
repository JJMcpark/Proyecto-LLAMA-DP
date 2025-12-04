package com.dpatrones.proyecto.patterns.state;

import com.dpatrones.proyecto.model.Pedido;

public class EntregadoState implements EstadoPedido {
    
    @Override
    public void siguienteEstado(Pedido pedido) {
        System.out.println("[STATE] Pedido #" + pedido.getId() + " ya entregado. Ciclo completado.");
    }

    @Override
    public boolean cancelar(Pedido pedido) {
        System.out.println("[STATE] Pedido #" + pedido.getId() + " ya entregado. No se puede cancelar.");
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
