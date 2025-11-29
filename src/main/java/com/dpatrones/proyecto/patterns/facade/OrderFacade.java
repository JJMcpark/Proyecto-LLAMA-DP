package com.dpatrones.proyecto.patterns.facade;

import com.dpatrones.proyecto.model.*;
import com.dpatrones.proyecto.patterns.decorator.*;
import com.dpatrones.proyecto.patterns.factory.IProcesadorPago;
import com.dpatrones.proyecto.patterns.factory.PaymentFactory;
import com.dpatrones.proyecto.patterns.observer.VentasSubject;
import com.dpatrones.proyecto.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * PATRÓN FACADE - Fachada para el proceso de Checkout
 * 
 * Esta clase simplifica el proceso de compra orquestando múltiples subsistemas:
 * - Verificación de inventario
 * - Procesamiento de pago (Factory)
 * - Aplicación de extras (Decorator)
 * - Creación del pedido
 * - Notificaciones
 * 
 * El controlador solo necesita llamar a realizarCompra() sin conocer los detalles internos.
 */
@Service
@RequiredArgsConstructor
public class OrderFacade {
    
    private final ProductoService productoService;
    private final PedidoService pedidoService;
    private final UsuarioService usuarioService;
    private final NotificacionService notificacionService;
    
    /**
     * DTO simple para los items del carrito
     */
    public record ItemCarrito(Long productoId, int cantidad, List<String> extras) {}
    
    /**
     * Realiza todo el proceso de compra en un solo método
     */
    @Transactional
    public Pedido realizarCompra(Long usuarioId, List<ItemCarrito> carrito, 
                                  String metodoPago, String metodoEnvio, 
                                  String direccionEnvio) {
        
        System.out.println("\n========== INICIANDO CHECKOUT ==========");
        
        // 1. Verificar que el usuario existe
        Usuario usuario = usuarioService.buscarPorId(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        System.out.println("[CHECKOUT] Usuario: " + usuario.getNombre());
        
        // 2. Verificar stock de todos los productos
        System.out.println("[CHECKOUT] Verificando inventario...");
        for (ItemCarrito item : carrito) {
            if (!productoService.hayStock(item.productoId(), item.cantidad())) {
                throw new RuntimeException("Stock insuficiente para producto ID: " + item.productoId());
            }
        }
        System.out.println("[CHECKOUT] Inventario OK");
        
        // 3. Crear los detalles del pedido aplicando Decorators
        List<DetallePedido> detalles = new ArrayList<>();
        double totalPedido = 0.0;
        
        for (ItemCarrito item : carrito) {
            Producto producto = productoService.buscarPorId(item.productoId()).get();
            
            // Aplicar patrón DECORATOR según los extras seleccionados
            IProductoComponente productoDecorado = new ProductoBase(producto);
            
            if (item.extras() != null) {
                for (String extra : item.extras()) {
                    productoDecorado = aplicarDecorator(productoDecorado, extra);
                }
            }
            
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
            
            System.out.println("[CHECKOUT] " + productoDecorado.getDescripcion() + " x" + item.cantidad() + " = S/." + subtotal);
        }
        
        // 4. Procesar el pago usando FACTORY
        System.out.println("[CHECKOUT] Procesando pago...");
        IProcesadorPago procesador = PaymentFactory.crearProcesador(metodoPago);
        
        if (!procesador.validarDatos()) {
            throw new RuntimeException("Datos de pago inválidos");
        }
        
        if (!procesador.procesarPago(totalPedido)) {
            throw new RuntimeException("El pago fue rechazado");
        }
        
        // 5. Descontar stock
        System.out.println("[CHECKOUT] Actualizando inventario...");
        for (ItemCarrito item : carrito) {
            productoService.descontarStock(item.productoId(), item.cantidad());
        }
        
        // 6. Crear el pedido
        String codigoSeguimiento = generarCodigoSeguimiento();
        
        Pedido pedido = Pedido.builder()
            .usuario(usuario)
            .fecha(LocalDateTime.now())
            .detalles(detalles)
            .total(totalPedido)
            .estado("PENDIENTE")
            .metodoPago(metodoPago)
            .metodoEnvio(metodoEnvio)
            .direccionEnvio(direccionEnvio)
            .codigoSeguimiento(codigoSeguimiento)
            .build();
        
        pedido = pedidoService.guardar(pedido);
        
        // 7. El pago ya fue procesado, avanzamos el estado a PAGADO
        pedido.initEstado();
        pedido.avanzarEstado(); // PENDIENTE -> PAGADO
        pedido = pedidoService.guardar(pedido);
        
        // 8. Enviar notificaciones
        notificacionService.enviarConfirmacionPedido(usuario.getEmail(), pedido.getId());
        notificacionService.enviarCodigoSeguimiento(usuario.getEmail(), pedido.getId(), codigoSeguimiento);
        
        // 9. Notificar a los observadores (PATRÓN OBSERVER)
        VentasSubject.getInstance().notificarNuevaVenta(pedido.getId(), totalPedido);
        
        System.out.println("========== CHECKOUT COMPLETADO ==========");
        System.out.println("[RESULTADO] Pedido #" + pedido.getId() + " creado exitosamente");
        System.out.println("[RESULTADO] Total: S/." + totalPedido);
        System.out.println("[RESULTADO] Código seguimiento: " + codigoSeguimiento);
        
        return pedido;
    }
    
    /**
     * Aplica el decorador correspondiente según el tipo de extra
     */
    private IProductoComponente aplicarDecorator(IProductoComponente producto, String extra) {
        return switch (extra.toUpperCase()) {
            case "ESTAMPADO" -> new EstampadoDecorator(producto);
            case "BORDADO" -> new BordadoDecorator(producto);
            case "EMPAQUE_REGALO", "REGALO" -> new EmpaqueRegaloDecorator(producto);
            default -> producto; // Si no reconoce el extra, no hace nada
        };
    }
    
    /**
     * Genera un código de seguimiento único
     */
    private String generarCodigoSeguimiento() {
        return "TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
