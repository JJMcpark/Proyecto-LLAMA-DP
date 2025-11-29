package com.dpatrones.proyecto.patterns.state;

import com.dpatrones.proyecto.model.Pedido;

/**
 * PATRÓN STATE - Estado ENTREGADO
 * Estado final del pedido. No puede avanzar ni cancelarse.
 * Permite solicitar devolución dentro de los 7 días.
 */
public class EntregadoState implements EstadoPedido {
    
    @Override
    public void siguienteEstado(Pedido pedido) {
        System.out.println("[LOGÍSTICA] Pedido #" + pedido.getId() + " ya fue entregado. Ciclo completado.");
        System.out.println("[INFO] Si desea devolver el producto, use solicitarDevolucion().");
    }

    @Override
    public boolean cancelar(Pedido pedido) {
        System.out.println("[ERROR] Pedido #" + pedido.getId() + " ya fue entregado.");
        System.out.println("[INFO] Para devoluciones, contacte a soporte dentro de 7 días.");
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
    
    /**
     * Permite solicitar una devolución del pedido
     * @param pedido el pedido a devolver
     * @param motivo razón de la devolución
     * @return true si la solicitud fue aceptada
     */
    public boolean solicitarDevolucion(Pedido pedido, String motivo) {
        System.out.println("[DEVOLUCIÓN] Solicitud de devolución para Pedido #" + pedido.getId());
        System.out.println("[DEVOLUCIÓN] Motivo: " + motivo);
        System.out.println("[DEVOLUCIÓN] Solicitud registrada. Será procesada en 24-48 horas.");
        return true;
    }
}
