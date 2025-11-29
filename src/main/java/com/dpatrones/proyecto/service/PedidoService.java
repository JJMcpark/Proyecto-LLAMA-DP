package com.dpatrones.proyecto.service;

import com.dpatrones.proyecto.model.DetallePedido;
import com.dpatrones.proyecto.model.Pedido;
import com.dpatrones.proyecto.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PedidoService {
    
    private final PedidoRepository pedidoRepository;
    
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
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
    
    /**
     * Avanza el estado del pedido usando el patrón State
     */
    public Pedido avanzarEstado(Long pedidoId) {
        Optional<Pedido> opt = pedidoRepository.findById(pedidoId);
        if (opt.isPresent()) {
            Pedido pedido = opt.get();
            pedido.avanzarEstado();
            return pedidoRepository.save(pedido);
        }
        throw new RuntimeException("Pedido no encontrado: " + pedidoId);
    }
    
    /**
     * Cancela el pedido si es posible según su estado
     */
    public Pedido cancelarPedido(Long pedidoId) {
        Optional<Pedido> opt = pedidoRepository.findById(pedidoId);
        if (opt.isPresent()) {
            Pedido pedido = opt.get();
            pedido.cancelarPedido();
            return pedidoRepository.save(pedido);
        }
        throw new RuntimeException("Pedido no encontrado: " + pedidoId);
    }
    
    /**
     * Calcula el total del pedido sumando los subtotales de sus detalles
     */
    public Double calcularTotal(Pedido pedido) {
        double total = 0.0;
        for (DetallePedido detalle : pedido.getDetalles()) {
            total += detalle.getSubtotal();
        }
        return total;
    }
    
    // ============ MÉTODOS PARA DASHBOARD ============
    
    public List<Pedido> getPedidosHoy() {
        return pedidoRepository.findPedidosHoy();
    }
    
    public Double getTotalVentasHoy() {
        return pedidoRepository.getTotalVentasHoy();
    }
    
    public Map<String, Long> getEstadisticasPorEstado() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("PENDIENTE", pedidoRepository.contarPorEstado("PENDIENTE"));
        stats.put("PAGADO", pedidoRepository.contarPorEstado("PAGADO"));
        stats.put("ENVIADO", pedidoRepository.contarPorEstado("ENVIADO"));
        stats.put("ENTREGADO", pedidoRepository.contarPorEstado("ENTREGADO"));
        stats.put("CANCELADO", pedidoRepository.contarPorEstado("CANCELADO"));
        return stats;
    }
    
    public List<Pedido> buscarPorFechas(LocalDateTime inicio, LocalDateTime fin) {
        return pedidoRepository.findByFechaBetween(inicio, fin);
    }
}
