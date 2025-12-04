package com.dpatrones.proyecto.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dpatrones.proyecto.model.Pedido;
import com.dpatrones.proyecto.model.Venta;
import com.dpatrones.proyecto.patterns.observer.VentasSubject;
import com.dpatrones.proyecto.repository.PedidoRepository;
import com.dpatrones.proyecto.repository.VentaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final VentaRepository ventaRepository;

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAllConUsuario();
    }

    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    public Pedido guardar(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> buscarPorUsuario(Long usuarioId) {
        return pedidoRepository.findByUsuarioId(usuarioId);
    }

    public List<Pedido> buscarPorEstado(String estado) {
        return pedidoRepository.findByEstado(estado);
    }

    public Pedido avanzarEstado(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado: " + pedidoId));

        String estadoAnterior = pedido.getEstado();
        pedido.avanzarEstado();
        Pedido saved = pedidoRepository.save(pedido);

        // Si pasó a ENTREGADO, registrar como Venta concretada
        if ("ENTREGADO".equals(saved.getEstado()) && !"ENTREGADO".equals(estadoAnterior)) {
            registrarVenta(saved);
        }

        return saved;
    }

    private void registrarVenta(Pedido pedido) {
        Venta venta = new Venta();
        venta.setUsuarioId(pedido.getUsuario() != null ? pedido.getUsuario().getId() : null);
        venta.setTotal(BigDecimal.valueOf(pedido.getTotal()));
        ventaRepository.save(venta);

        // Notificar observers
        VentasSubject.getInstance().notificarNuevaVenta(venta.getId(), pedido.getTotal());
    }

    public Pedido cancelarPedido(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado: " + pedidoId));
        pedido.cancelarPedido();
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> getPedidosHoy() {
        return pedidoRepository.findPedidosHoy();
    }

    public Double getTotalVentasHoy() {
        return pedidoRepository.getTotalVentasHoy();
    }

    public Map<String, Long> getEstadisticasPorEstado() {
        Map<String, Long> stats = new HashMap<>();
        for (String estado : List.of("PENDIENTE", "PAGADO", "ENVIADO", "ENTREGADO", "CANCELADO")) {
            stats.put(estado, pedidoRepository.contarPorEstado(estado));
        }
        return stats;
    }

    public List<Pedido> buscarPorFechas(LocalDateTime inicio, LocalDateTime fin) {
        return pedidoRepository.findByFechaBetween(inicio, fin);
    }
}
