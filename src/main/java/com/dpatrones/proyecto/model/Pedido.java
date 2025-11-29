package com.dpatrones.proyecto.model;

import com.dpatrones.proyecto.patterns.state.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pedidos")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fecha;
    private Double total;
    private String estado; // PENDIENTE, PAGADO, ENVIADO, ENTREGADO, CANCELADO
    
    private String metodoPago;     // TARJETA, PAYPAL, CONTRAENTREGA
    private String metodoEnvio;    // ESTANDAR, EXPRESS, TIENDA
    private String direccionEnvio;
    private String codigoSeguimiento;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "pedido_id")
    @Builder.Default
    private List<DetallePedido> detalles = new ArrayList<>();

    // ============ PATRÓN STATE ============
    // Este campo NO se guarda en BD, es transitorio
    @Transient
    private EstadoPedido estadoActual;

    /**
     * Inicializa el estado del pedido basado en el string guardado en BD
     */
    @PostLoad
    public void initEstado() {
        this.estadoActual = crearEstadoDesdeString(this.estado);
    }
    
    private EstadoPedido crearEstadoDesdeString(String estadoStr) {
        if (estadoStr == null) return new PendienteState();
        
        return switch (estadoStr) {
            case "PAGADO" -> new PagadoState();
            case "ENVIADO" -> new EnviadoState();
            case "ENTREGADO" -> new EntregadoState();
            default -> new PendienteState();
        };
    }

    /**
     * Avanza al siguiente estado en el flujo logístico
     */
    public void avanzarEstado() {
        if (this.estadoActual == null) initEstado();
        this.estadoActual.siguienteEstado(this);
    }

    /**
     * Intenta cancelar el pedido
     */
    public boolean cancelarPedido() {
        if (this.estadoActual == null) initEstado();
        return this.estadoActual.cancelar(this);
    }
    
    /**
     * Verifica si el pedido puede modificarse
     */
    public boolean puedeModificarse() {
        if (this.estadoActual == null) initEstado();
        return this.estadoActual.puedeModificarse();
    }
    
    /**
     * Prepara el pedido antes de guardarse por primera vez
     */
    @PrePersist
    public void prePersist() {
        if (this.fecha == null) {
            this.fecha = LocalDateTime.now();
        }
        if (this.estado == null) {
            this.estado = "PENDIENTE";
        }
    }
}
