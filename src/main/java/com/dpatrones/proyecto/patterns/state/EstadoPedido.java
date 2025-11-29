package com.dpatrones.proyecto.patterns.state;

import com.dpatrones.proyecto.model.Pedido;

/**
 * PATRÓN STATE - Interface EstadoPedido
 * Define el comportamiento que cada estado del pedido debe implementar.
 * Esto permite cambiar el comportamiento del pedido según su estado actual
 * sin usar cadenas de if-else.
 */
public interface EstadoPedido {
    
    /**
     * Avanza al siguiente estado en el flujo de logística
     */
    void siguienteEstado(Pedido pedido);
    
    /**
     * Intenta cancelar el pedido (depende del estado actual)
     */
    boolean cancelar(Pedido pedido);
    
    /**
     * Retorna el nombre del estado actual
     */
    String getNombre();
    
    /**
     * Indica si el pedido puede ser modificado en este estado
     */
    boolean puedeModificarse();
}
