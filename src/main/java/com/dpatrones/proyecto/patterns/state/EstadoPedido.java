package com.dpatrones.proyecto.patterns.state;

import com.dpatrones.proyecto.model.Pedido;

public interface EstadoPedido {
    void siguienteEstado(Pedido pedido);
    boolean cancelar(Pedido pedido);
    String getNombre();
    boolean puedeModificarse();
}
