package com.dpatrones.proyecto.patterns.facade;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dpatrones.proyecto.model.DetallePedido;
import com.dpatrones.proyecto.model.Pedido;
import com.dpatrones.proyecto.model.Producto;
import com.dpatrones.proyecto.model.Usuario;
import com.dpatrones.proyecto.patterns.decorator.*;
import com.dpatrones.proyecto.patterns.factory.IProcesadorPago;
import com.dpatrones.proyecto.patterns.factory.PaymentFactory;
import com.dpatrones.proyecto.patterns.observer.VentasSubject;
import com.dpatrones.proyecto.service.NotificacionService;
import com.dpatrones.proyecto.service.PedidoService;
import com.dpatrones.proyecto.service.ProductoService;
import com.dpatrones.proyecto.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderFacade {

    private final ProductoService productoService;
    private final PedidoService pedidoService;
    private final UsuarioService usuarioService;
    private final NotificacionService notificacionService;

    public record ItemCarrito(Long productoId, int cantidad, List<String> extras) {
    }

    @Transactional
    public Pedido realizarCompra(Long usuarioId, List<ItemCarrito> carrito,
            String metodoPago, String metodoEnvio, String direccionEnvio) {

        Usuario usuario = usuarioService.buscarPorId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        verificarStock(carrito);

        List<DetallePedido> detalles = new ArrayList<>();
        double totalPedido = 0.0;

        for (ItemCarrito item : carrito) {
            Producto producto = productoService.buscarPorId(item.productoId()).get();
            IProductoComponente productoDecorado = aplicarExtras(producto, item.extras());

            double precioConExtras = productoDecorado.getPrecio();
            double subtotal = precioConExtras * item.cantidad();
            double costoExtras = precioConExtras - producto.getPrecio();

            DetallePedido detalle = DetallePedido.builder()
                    .producto(producto)
                    .cantidad(item.cantidad())
                    .precioUnitario(precioConExtras)
                    .subtotal(subtotal)
                    .extrasAplicados(productoDecorado.getExtras())
                    .costoExtras(costoExtras * item.cantidad())
                    .build();

            detalles.add(detalle);
            totalPedido += subtotal;
        }

        procesarPago(metodoPago, totalPedido);
        descontarStock(carrito);

        Pedido pedido = crearPedido(usuario, detalles, totalPedido, metodoPago, metodoEnvio, direccionEnvio);
        pedido = pedidoService.guardar(pedido);

        pedido.initEstado();
        pedido.avanzarEstado();
        pedido = pedidoService.guardar(pedido);

        notificacionService.enviarConfirmacionPedido(usuario.getEmail(), pedido.getId());
        notificacionService.enviarCodigoSeguimiento(usuario.getEmail(), pedido.getId(), pedido.getCodigoSeguimiento());

        VentasSubject.getInstance().notificarNuevaVenta(pedido.getId(), totalPedido);

        return pedido;
    }

    private void verificarStock(List<ItemCarrito> carrito) {
        for (ItemCarrito item : carrito) {
            if (!productoService.hayStock(item.productoId(), item.cantidad())) {
                throw new RuntimeException("Stock insuficiente para producto ID: " + item.productoId());
            }
        }
    }

    private IProductoComponente aplicarExtras(Producto producto, List<String> extras) {
        IProductoComponente resultado = new ProductoBase(producto);
        if (extras != null) {
            for (String extra : extras) {
                resultado = switch (extra.toUpperCase()) {
                    case "ESTAMPADO" -> new EstampadoDecorator(resultado);
                    case "BORDADO" -> new BordadoDecorator(resultado);
                    case "EMPAQUE_REGALO", "REGALO" -> new EmpaqueRegaloDecorator(resultado);
                    default -> resultado;
                };
            }
        }
        return resultado;
    }

    private void procesarPago(String metodoPago, double total) {
        IProcesadorPago procesador = PaymentFactory.crearProcesador(metodoPago);
        if (!procesador.validarDatos()) {
            throw new RuntimeException("Datos de pago inválidos");
        }
        if (!procesador.procesarPago(total)) {
            throw new RuntimeException("El pago fue rechazado");
        }
    }

    private void descontarStock(List<ItemCarrito> carrito) {
        for (ItemCarrito item : carrito) {
            productoService.descontarStock(item.productoId(), item.cantidad());
        }
    }

    private Pedido crearPedido(Usuario usuario, List<DetallePedido> detalles, double total,
            String metodoPago, String metodoEnvio, String direccionEnvio) {
        return Pedido.builder()
                .usuario(usuario)
                .fecha(LocalDateTime.now())
                .detalles(detalles)
                .total(total)
                .estado("PENDIENTE")
                .metodoPago(metodoPago)
                .metodoEnvio(metodoEnvio)
                .direccionEnvio(direccionEnvio)
                .codigoSeguimiento("TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .build();
    }
}
