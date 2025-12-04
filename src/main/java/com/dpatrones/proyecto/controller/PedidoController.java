package com.dpatrones.proyecto.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dpatrones.proyecto.model.Pedido;
import com.dpatrones.proyecto.patterns.facade.OrderFacade;
import com.dpatrones.proyecto.service.PedidoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PedidoController {

    private final PedidoService pedidoService;
    private final OrderFacade orderFacade;

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

    @PostMapping("/checkout")
    public ResponseEntity<?> realizarCompra(@RequestBody CheckoutRequest request) {
        try {
            List<OrderFacade.ItemCarrito> carrito = request.items().stream()
                    .map(item -> new OrderFacade.ItemCarrito(item.productoId(), item.cantidad(), item.extras()))
                    .toList();

            Pedido pedido = orderFacade.realizarCompra(
                    request.usuarioId(), carrito, request.metodoPago(),
                    request.metodoEnvio(), request.direccionEnvio());

            return ResponseEntity.ok(Map.of("mensaje", "Compra realizada", "pedido", pedido));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    public record CheckoutRequest(
            Long usuarioId, List<ItemRequest> items, String metodoPago,
            String metodoEnvio, String direccionEnvio) {
    }

    public record ItemRequest(Long productoId, int cantidad, List<String> extras) {
    }
}
