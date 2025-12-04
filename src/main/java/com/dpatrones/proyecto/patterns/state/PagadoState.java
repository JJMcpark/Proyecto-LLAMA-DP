package com.dpatrones.proyecto.patterns.state;

import com.dpatrones.proyecto.model.Pedido;

public class PagadoState implements EstadoPedido {
    
    @Override
    public void siguienteEstado(Pedido pedido) {
        pedido.setEstadoActual(new EnviadoState());
        pedido.setEstado("ENVIADO");
        System.out.println("[STATE] Pedido #" + pedido.getId() + ": PAGADO -> ENVIADO");
    }

    @Override
    public boolean cancelar(Pedido pedido) {
        System.out.println("[STATE] Pedido #" + pedido.getId() + " ya pagado. No se puede cancelar.");
        return false;
    }

    @Override
    public String getNombre() {
        return "PAGADO";
    }
    
    @Override
    public boolean puedeModificarse() {
        return false;
    }
}
