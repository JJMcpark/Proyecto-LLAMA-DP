package com.dpatrones.proyecto.controller;

import com.dpatrones.proyecto.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para el Dashboard de Logística
 * Proporciona estadísticas y métricas para el panel de administración
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DashboardController {
    
    private final PedidoService pedidoService;
    
    /**
     * Obtiene todas las estadísticas del dashboard
     */
    @GetMapping
    public Map<String, Object> obtenerDashboard() {
        Map<String, Object> dashboard = new HashMap<>();
        
        // Estadísticas por estado
        dashboard.put("estadisticasPorEstado", pedidoService.getEstadisticasPorEstado());
        
        // Ventas del día
        dashboard.put("ventasHoy", pedidoService.getTotalVentasHoy());
        
        // Pedidos del día
        dashboard.put("pedidosHoy", pedidoService.getPedidosHoy().size());
        
        // Total de pedidos
        dashboard.put("totalPedidos", pedidoService.listarTodos().size());
        
        return dashboard;
    }
    
    /**
     * Estadísticas por estado de pedido
     */
    @GetMapping("/estados")
    public Map<String, Long> getEstadisticasEstados() {
        return pedidoService.getEstadisticasPorEstado();
    }
    
    /**
     * Ventas totales del día
     */
    @GetMapping("/ventas-hoy")
    public Map<String, Object> getVentasHoy() {
        return Map.of(
            "total", pedidoService.getTotalVentasHoy(),
            "cantidadPedidos", pedidoService.getPedidosHoy().size()
        );
    }
    
    /**
     * Lista de pedidos pendientes de gestión logística
     */
    @GetMapping("/logistica/pendientes")
    public Map<String, Object> getPedidosPendientesLogistica() {
        return Map.of(
            "pendientes", pedidoService.buscarPorEstado("PENDIENTE"),
            "pagados", pedidoService.buscarPorEstado("PAGADO"),
            "enviados", pedidoService.buscarPorEstado("ENVIADO")
        );
    }
}
