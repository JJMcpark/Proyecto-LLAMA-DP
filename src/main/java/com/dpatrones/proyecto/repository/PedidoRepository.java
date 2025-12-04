package com.dpatrones.proyecto.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dpatrones.proyecto.model.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    List<Pedido> findByUsuarioId(Long usuarioId);
    
    List<Pedido> findByEstado(String estado);
    
    List<Pedido> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
    
    // Cargar pedidos con usuario (evita LazyInitializationException)
    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.usuario ORDER BY p.fecha DESC")
    List<Pedido> findAllConUsuario();
    
    // Para el dashboard de logística
    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.estado = ?1")
    Long contarPorEstado(String estado);
    
    // Pedidos del día
    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.usuario WHERE DATE(p.fecha) = CURRENT_DATE ORDER BY p.fecha DESC")
    List<Pedido> findPedidosHoy();
    
    // Total de ventas del día
    @Query("SELECT COALESCE(SUM(p.total), 0) FROM Pedido p WHERE DATE(p.fecha) = CURRENT_DATE AND p.estado != 'CANCELADO'")
    Double getTotalVentasHoy();
}
