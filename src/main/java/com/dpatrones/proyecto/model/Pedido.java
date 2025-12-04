package com.dpatrones.proyecto.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.dpatrones.proyecto.patterns.state.EntregadoState;
import com.dpatrones.proyecto.patterns.state.EnviadoState;
import com.dpatrones.proyecto.patterns.state.EstadoPedido;
import com.dpatrones.proyecto.patterns.state.PagadoState;
import com.dpatrones.proyecto.patterns.state.PendienteState;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String estado;
    private String metodoPago;
    private String metodoEnvio;
    private String direccionEnvio;
    private String codigoSeguimiento;

    @ManyToOne(fetch = jakarta.persistence.FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = jakarta.persistence.FetchType.EAGER)
    @JoinColumn(name = "pedido_id")
    @Builder.Default
    private List<DetallePedido> detalles = new ArrayList<>();

    @Transient
    private EstadoPedido estadoActual;

    @PostLoad
    public void initEstado() {
        this.estadoActual = crearEstadoDesdeString(this.estado);
    }

    private EstadoPedido crearEstadoDesdeString(String estadoStr) {
        String v = (estadoStr == null) ? "PENDIENTE" : estadoStr.trim().toUpperCase();
        return switch (v) {
            case "PAGADO" -> new PagadoState();
            case "ENVIADO" -> new EnviadoState();
            case "ENTREGADO" -> new EntregadoState();
            default -> new PendienteState();
        };
    }

    public void avanzarEstado() {
        if (this.estadoActual == null) {
            initEstado();
        }
        this.estadoActual.siguienteEstado(this);
        this.estado = this.estadoActual.getNombre();
    }

    public boolean cancelarPedido() {
        if (this.estadoActual == null) {
            initEstado();
        }
        boolean ok = this.estadoActual.cancelar(this);
        this.estado = this.estadoActual.getNombre();
        return ok;
    }

    public boolean puedeModificarse() {
        String v = (this.estado == null) ? "PENDIENTE" : this.estado.toUpperCase();
        return !(v.equals("ENVIADO") || v.equals("ENTREGADO"));
    }

    @PrePersist
    public void prePersist() {
        if (this.fecha == null) {
            this.fecha = LocalDateTime.now();
        }
        if (this.estado == null || this.estado.isBlank()) {
            this.estado = "PENDIENTE";
        }
        if (this.estadoActual == null) {
            this.estadoActual = crearEstadoDesdeString(this.estado);
        }
    }
}
