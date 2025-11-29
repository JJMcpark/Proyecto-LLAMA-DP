package com.dpatrones.proyecto.patterns.state;

import com.dpatrones.proyecto.model.Pedido;

/**
 * PATRÓN STATE - Estado PAGADO
 * El pedido fue pagado. Ya no puede cancelarse, solo avanzar a ENVIADO.
 */
public class PagadoState implements EstadoPedido {
    
    @Override
    public void siguienteEstado(Pedido pedido) {
        pedido.setEstadoActual(new EnviadoState());
        pedido.setEstado("ENVIADO");
        System.out.println("[LOGÍSTICA] Pedido #" + pedido.getId() + " -> ENVIADO. Salió del almacén.");
    }

    @Override
    public boolean cancelar(Pedido pedido) {
        System.out.println("[ERROR] Pedido #" + pedido.getId() + " ya está pagado. Solicite devolución.");
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
