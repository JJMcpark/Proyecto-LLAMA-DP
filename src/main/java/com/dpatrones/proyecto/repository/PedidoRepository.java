package com.dpatrones.proyecto.repository;

import com.dpatrones.proyecto.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    List<Pedido> findByUsuarioId(Long usuarioId);
    
    List<Pedido> findByEstado(String estado);
    
    List<Pedido> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
    
    // Para el dashboard de logística
    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.estado = ?1")
    Long contarPorEstado(String estado);
    
    // Pedidos del día
    @Query("SELECT p FROM Pedido p WHERE DATE(p.fecha) = CURRENT_DATE")
    List<Pedido> findPedidosHoy();
    
    // Total de ventas del día
    @Query("SELECT COALESCE(SUM(p.total), 0) FROM Pedido p WHERE DATE(p.fecha) = CURRENT_DATE AND p.estado != 'CANCELADO'")
    Double getTotalVentasHoy();
}
