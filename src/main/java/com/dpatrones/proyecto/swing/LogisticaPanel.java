package com.dpatrones.proyecto.swing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import org.springframework.context.ApplicationContext;

import com.dpatrones.proyecto.model.DetallePedido;
import com.dpatrones.proyecto.model.Pedido;
import com.dpatrones.proyecto.model.Venta;
import com.dpatrones.proyecto.patterns.observer.VentasObserver;
import com.dpatrones.proyecto.patterns.observer.VentasSubject;
import com.dpatrones.proyecto.service.PedidoService;
import com.dpatrones.proyecto.service.VentaService;

public class LogisticaPanel extends JPanel implements VentasObserver {

    private final ApplicationContext applicationContext;
    private PedidoService pedidoService;
    private VentaService ventaService;

    private JTable tablaVentas;
    private DefaultTableModel modeloVentas;
    private JTable tablaPedidos;
    private DefaultTableModel modeloPedidos;
    private JTextArea txtLog;

    private static final DateTimeFormatter FECHA_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public LogisticaPanel(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        if (this.applicationContext != null) {
            this.pedidoService = this.applicationContext.getBean(PedidoService.class);
            this.ventaService = this.applicationContext.getBean(VentaService.class);
        }

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initUI();
        cargarDatos();
    }

    private void initUI() {
        // Panel central con dos tablas
        JPanel panelTablas = new JPanel(new GridLayout(2, 1, 5, 5));
        
        // Tabla de VENTAS (del front - sin extras)
        String[] colVentas = {"ID", "Total", "Usuario ID"};
        modeloVentas = new DefaultTableModel(colVentas, 0);
        tablaVentas = new JTable(modeloVentas);
        JScrollPane scrollVentas = new JScrollPane(tablaVentas);
        scrollVentas.setBorder(BorderFactory.createTitledBorder("VENTAS SIMPLES (Front Web)"));
        panelTablas.add(scrollVentas);
        
        // Tabla de PEDIDOS (del API checkout - con extras/Decorator)
        String[] colPedidos = {"ID", "Usuario", "Total", "Estado", "Método Pago", "Extras", "Fecha"};
        modeloPedidos = new DefaultTableModel(colPedidos, 0);
        tablaPedidos = new JTable(modeloPedidos);
        JScrollPane scrollPedidos = new JScrollPane(tablaPedidos);
        scrollPedidos.setBorder(BorderFactory.createTitledBorder("PEDIDOS CHECKOUT (con Decorator/Extras)"));
        panelTablas.add(scrollPedidos);
        
        add(panelTablas, BorderLayout.CENTER);
        
        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        
        JButton btnActualizar = new JButton("🔄 Actualizar");
        btnActualizar.addActionListener(e -> cargarDatos());
        
        JButton btnAvanzarEstado = new JButton("▶ Avanzar Estado (State)");
        btnAvanzarEstado.addActionListener(e -> avanzarEstadoSeleccionado());
        
        JButton btnVerDetalles = new JButton("📋 Ver Detalles (Decorator)");
        btnVerDetalles.addActionListener(e -> verDetallesPedido());

        JButton btnSimularVenta = new JButton("🔔 Simular Notificación (Observer)");
        btnSimularVenta.addActionListener(e -> VentasSubject.getInstance().notificarNuevaVenta(999L, 123.45));
        
        panelBotones.add(btnActualizar);
        panelBotones.add(btnAvanzarEstado);
        panelBotones.add(btnVerDetalles);
        panelBotones.add(btnSimularVenta);
        
        // Log de eventos
        txtLog = new JTextArea(4, 60);
        txtLog.setEditable(false);
        JScrollPane scrollLog = new JScrollPane(txtLog);
        scrollLog.setBorder(BorderFactory.createTitledBorder("Log de Eventos (Observer Pattern)"));
        
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(panelBotones, BorderLayout.NORTH);
        panelInferior.add(scrollLog, BorderLayout.CENTER);
        
        add(panelInferior, BorderLayout.SOUTH);
    }
    
    public void cargarDatos() {
        cargarVentas();
        cargarPedidos();
    }
    
    private void cargarVentas() {
        modeloVentas.setRowCount(0);
        
        if (ventaService != null) {
            try {
                List<Venta> ventas = ventaService.listarTodas();
                for (Venta v : ventas) {
                    modeloVentas.addRow(new Object[]{
                        v.getId(),
                        String.format("S/. %.2f", v.getTotal()),
                        v.getUsuarioId() != null ? v.getUsuarioId() : "-"
                    });
                }
                agregarLog("Ventas simples cargadas: " + ventas.size());
            } catch (Exception ex) {
                agregarLog("Error ventas: " + ex.getMessage());
            }
        } else {
            modeloVentas.addRow(new Object[]{1L, "S/. 139.70", "-"});
            agregarLog("Modo demo - sin conexión BD");
        }
    }
    
    private void cargarPedidos() {
        modeloPedidos.setRowCount(0);

        if (pedidoService != null) {
            try {
                List<Pedido> pedidos = pedidoService.listarTodos();
                for (Pedido p : pedidos) {
                    String usuario = p.getUsuario() != null ? p.getUsuario().getNombre() : "-";
                    String fecha = p.getFecha() != null ? FECHA_FMT.format(p.getFecha()) : "-";
                    String metodoPago = p.getMetodoPago() != null ? p.getMetodoPago() : "-";
                    
                    // Recopilar extras de todos los detalles (Decorator)
                    StringBuilder extrasStr = new StringBuilder();
                    if (p.getDetalles() != null) {
                        for (DetallePedido d : p.getDetalles()) {
                            if (d.getExtrasAplicados() != null && !d.getExtrasAplicados().isEmpty()) {
                                if (extrasStr.length() > 0) extrasStr.append(", ");
                                extrasStr.append(d.getExtrasAplicados());
                            }
                        }
                    }
                    String extras = extrasStr.length() > 0 ? extrasStr.toString() : "Sin extras";
                    
                    modeloPedidos.addRow(new Object[]{
                        p.getId(), usuario, String.format("S/. %.2f", p.getTotal()), 
                        p.getEstado(), metodoPago, extras, fecha
                    });
                }
                agregarLog("Pedidos checkout cargados: " + pedidos.size());
            } catch (Exception ex) {
                agregarLog("Error pedidos: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * Ver detalles completos del pedido incluyendo extras (Decorator)
     */
    private void verDetallesPedido() {
        int fila = tablaPedidos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un pedido de la tabla");
            return;
        }
        
        Long pedidoId = (Long) modeloPedidos.getValueAt(fila, 0);
        
        if (pedidoService != null) {
            try {
                Pedido p = pedidoService.buscarPorId(pedidoId).orElse(null);
                if (p == null) {
                    JOptionPane.showMessageDialog(this, "Pedido no encontrado");
                    return;
                }
                
                StringBuilder sb = new StringBuilder();
                sb.append("═══════════════════════════════════════\n");
                sb.append("           DETALLES DEL PEDIDO #").append(p.getId()).append("\n");
                sb.append("═══════════════════════════════════════\n\n");
                sb.append("Usuario: ").append(p.getUsuario() != null ? p.getUsuario().getNombre() : "-").append("\n");
                sb.append("Fecha: ").append(p.getFecha() != null ? FECHA_FMT.format(p.getFecha()) : "-").append("\n");
                sb.append("Estado: ").append(p.getEstado()).append("\n");
                sb.append("Método Pago: ").append(p.getMetodoPago()).append("\n");
                sb.append("Método Envío: ").append(p.getMetodoEnvio()).append("\n");
                sb.append("Dirección: ").append(p.getDireccionEnvio()).append("\n");
                sb.append("Código Seguimiento: ").append(p.getCodigoSeguimiento()).append("\n\n");
                
                sb.append("───────────────────────────────────────\n");
                sb.append("           ITEMS (DECORATOR PATTERN)\n");
                sb.append("───────────────────────────────────────\n");
                
                if (p.getDetalles() != null) {
                    for (DetallePedido d : p.getDetalles()) {
                        String prodNombre = d.getProducto() != null ? d.getProducto().getNombre() : "Producto";
                        sb.append("\n• ").append(prodNombre).append(" x").append(d.getCantidad()).append("\n");
                        sb.append("  Precio unitario: S/. ").append(String.format("%.2f", d.getPrecioUnitario())).append("\n");
                        
                        if (d.getExtrasAplicados() != null && !d.getExtrasAplicados().isEmpty()) {
                            sb.append("  ✨ EXTRAS (Decorator): ").append(d.getExtrasAplicados()).append("\n");
                            sb.append("  Costo extras: S/. ").append(String.format("%.2f", d.getCostoExtras())).append("\n");
                        } else {
                            sb.append("  (Sin extras)\n");
                        }
                        sb.append("  Subtotal: S/. ").append(String.format("%.2f", d.getSubtotal())).append("\n");
                    }
                }
                
                sb.append("\n═══════════════════════════════════════\n");
                sb.append("TOTAL PEDIDO: S/. ").append(String.format("%.2f", p.getTotal())).append("\n");
                sb.append("═══════════════════════════════════════\n");
                
                JTextArea textArea = new JTextArea(sb.toString());
                textArea.setEditable(false);
                textArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));
                JScrollPane scroll = new JScrollPane(textArea);
                scroll.setPreferredSize(new java.awt.Dimension(500, 400));
                
                JOptionPane.showMessageDialog(this, scroll, "Detalles del Pedido - Patrón Decorator", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }
    
    /**
     * Avanza el estado del pedido seleccionado (Patrón STATE)
     */
    private void avanzarEstadoSeleccionado() {
        int fila = tablaPedidos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un pedido de la tabla inferior");
            return;
        }
        
        Long pedidoId = (Long) modeloPedidos.getValueAt(fila, 0);
        String estadoActual = (String) modeloPedidos.getValueAt(fila, 3);
        
        if (pedidoService != null) {
            try {
                Pedido actualizado = pedidoService.avanzarEstado(pedidoId);
                modeloPedidos.setValueAt(actualizado.getEstado(), fila, 3);
                VentasSubject.getInstance().notificarCambioEstado(pedidoId, actualizado.getEstado());
                agregarLog("STATE: Pedido #" + pedidoId + " → " + actualizado.getEstado());
                return;
            } catch (Exception ex) {
                agregarLog("Error: " + ex.getMessage());
            }
        }
        
        // Simular si no hay conexión
        String nuevoEstado = switch (estadoActual) {
            case "PENDIENTE" -> "PAGADO";
            case "PAGADO" -> "ENVIADO";
            case "ENVIADO" -> "ENTREGADO";
            default -> estadoActual;
        };
        modeloPedidos.setValueAt(nuevoEstado, fila, 3);
        VentasSubject.getInstance().notificarCambioEstado(pedidoId, nuevoEstado);
        agregarLog("STATE (simulado): Pedido #" + pedidoId + " → " + nuevoEstado);
    }
    
    private void agregarLog(String mensaje) {
        txtLog.append("[" + java.time.LocalTime.now().toString().substring(0, 8) + "] " + mensaje + "\n");
        txtLog.setCaretPosition(txtLog.getDocument().getLength());
    }
    
    // ========== OBSERVER PATTERN ==========
    
    @Override
    public void addNotify() {
        super.addNotify();
        VentasSubject.getInstance().agregarObservador(this);
    }

    @Override
    public void removeNotify() {
        VentasSubject.getInstance().eliminarObservador(this);
        super.removeNotify();
    }
    
    @Override
    public void actualizar(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            agregarLog("OBSERVER: " + mensaje);
            if (mensaje.startsWith("NUEVA_VENTA") || mensaje.startsWith("CAMBIO_ESTADO")) {
                cargarDatos();
            }
        });
    }

    @Override
    public String getNombre() {
        return "Logistica";
    }
}
