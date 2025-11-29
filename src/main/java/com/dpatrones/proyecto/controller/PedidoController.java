package com.dpatrones.proyecto.controller;

import com.dpatrones.proyecto.model.Pedido;
import com.dpatrones.proyecto.patterns.facade.OrderFacade;
import com.dpatrones.proyecto.service.NotificacionService;
import com.dpatrones.proyecto.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PedidoController {
    
    private final PedidoService pedidoService;
    private final OrderFacade orderFacade;
    private final NotificacionService notificacionService;
    
    @GetMapping
    public List<Pedido> listarTodos() {
        return pedidoService.listarTodos();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
        return pedidoService.buscarPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/usuario/{usuarioId}")
    public List<Pedido> buscarPorUsuario(@PathVariable Long usuarioId) {
        return pedidoService.buscarPorUsuario(usuarioId);
    }
    
    @GetMapping("/estado/{estado}")
    public List<Pedido> buscarPorEstado(@PathVariable String estado) {
        return pedidoService.buscarPorEstado(estado.toUpperCase());
    }
    
    /**
     * Endpoint para realizar una compra usando el patrón FACADE
     * 
     * Body esperado:
     * {
     *   "usuarioId": 1,
     *   "items": [
     *     { "productoId": 1, "cantidad": 2, "extras": ["ESTAMPADO", "BORDADO"] },
     *     { "productoId": 3, "cantidad": 1, "extras": [] }
     *   ],
     *   "metodoPago": "TARJETA",
     *   "metodoEnvio": "EXPRESS",
     *   "direccionEnvio": "Av. Ejemplo 123"
     * }
     */
    @PostMapping("/checkout")
    public ResponseEntity<?> realizarCompra(@RequestBody CheckoutRequest request) {
        try {
            List<OrderFacade.ItemCarrito> carrito = request.items().stream()
                .map(item -> new OrderFacade.ItemCarrito(
                    item.productoId(), 
                    item.cantidad(), 
                    item.extras()
                ))
                .toList();
            
            Pedido pedido = orderFacade.realizarCompra(
                request.usuarioId(),
                carrito,
                request.metodoPago(),
                request.metodoEnvio(),
                request.direccionEnvio()
            );
            
            return ResponseEntity.ok(Map.of(
                "mensaje", "Compra realizada exitosamente",
                "pedido", pedido
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Avanza el estado del pedido (PATRÓN STATE)
     * PENDIENTE -> PAGADO -> ENVIADO -> ENTREGADO
     */
    @PostMapping("/{id}/avanzar-estado")
    public ResponseEntity<?> avanzarEstado(@PathVariable Long id) {
        try {
            Pedido pedido = pedidoService.avanzarEstado(id);
            
            // Notificar al usuario
            if (pedido.getUsuario() != null) {
                notificacionService.enviarActualizacionEstado(
                    pedido.getUsuario().getEmail(),
                    pedido.getId(),
                    pedido.getEstado()
                );
            }
            
            return ResponseEntity.ok(Map.of(
                "mensaje", "Estado actualizado",
                "nuevoEstado", pedido.getEstado(),
                "pedido", pedido
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Cancela el pedido si es posible según su estado actual
     */
    @PostMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelar(@PathVariable Long id) {
        try {
            Pedido pedido = pedidoService.cancelarPedido(id);
            return ResponseEntity.ok(Map.of(
                "mensaje", "Pedido cancelado",
                "pedido", pedido
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    // DTO para el request de checkout
    public record CheckoutRequest(
        Long usuarioId,
        List<ItemRequest> items,
        String metodoPago,
        String metodoEnvio,
        String direccionEnvio
    ) {}
    
    public record ItemRequest(
        Long productoId,
        int cantidad,
        List<String> extras
    ) {}
}
