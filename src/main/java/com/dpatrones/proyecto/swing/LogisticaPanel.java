package com.dpatrones.proyecto.swing;

import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.springframework.context.ApplicationContext;
import com.dpatrones.proyecto.model.DetallePedido;
import com.dpatrones.proyecto.model.Pedido;
import com.dpatrones.proyecto.patterns.observer.VentasObserver;
import com.dpatrones.proyecto.patterns.observer.VentasSubject;
import com.dpatrones.proyecto.service.PedidoService;
import com.dpatrones.proyecto.service.VentaService;

public class LogisticaPanel extends JPanel implements VentasObserver {

    private final PedidoService pedidoService;
    private final VentaService ventaService;

    private final DefaultTableModel modeloVentas = new DefaultTableModel(new String[] { "ID", "Total", "Usuario" }, 0);
    private final DefaultTableModel modeloPedidos = new DefaultTableModel(
            new String[] { "ID", "Usuario", "Total", "Estado", "Método Pago", "Extras", "Fecha" }, 0);
    private final JTable tablaPedidos = new JTable(modeloPedidos);
    private final JTextArea txtLog = new JTextArea(4, 60);

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public LogisticaPanel(ApplicationContext ctx) {
        this.pedidoService = ctx != null ? ctx.getBean(PedidoService.class) : null;
        this.ventaService = ctx != null ? ctx.getBean(VentaService.class) : null;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initUI();
        cargarDatos();
    }

    private void initUI() {
        JPanel tablas = new JPanel(new GridLayout(2, 1, 5, 5));
        tablas.add(crearTabla("VENTAS SIMPLES (Front)", new JTable(modeloVentas)));
        tablas.add(crearTabla("PEDIDOS CHECKOUT (Decorator)", tablaPedidos));
        add(tablas, BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout());
        addButton(botones, "🔄 Actualizar", e -> cargarDatos());
        addButton(botones, "▶ Avanzar Estado", e -> avanzarEstado());
        addButton(botones, "📋 Ver Detalles", e -> verDetalles());
        addButton(botones, "🔔 Simular Observer", e -> VentasSubject.getInstance().notificarNuevaVenta(999L, 123.45));

        txtLog.setEditable(false);
        JScrollPane scrollLog = new JScrollPane(txtLog);
        scrollLog.setBorder(BorderFactory.createTitledBorder("Log (Observer)"));

        JPanel inferior = new JPanel(new BorderLayout());
        inferior.add(botones, BorderLayout.NORTH);
        inferior.add(scrollLog, BorderLayout.CENTER);
        add(inferior, BorderLayout.SOUTH);
    }

    private JPanel crearTabla(String titulo, JTable tabla) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createTitledBorder(titulo));
        p.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return p;
    }

    private void addButton(JPanel panel, String texto, java.awt.event.ActionListener action) {
        JButton btn = new JButton(texto);
        btn.addActionListener(action);
        panel.add(btn);
    }

    public void cargarDatos() {
        modeloVentas.setRowCount(0);
        modeloPedidos.setRowCount(0);

        if (ventaService != null) {
            ventaService.listarTodas().forEach(v -> modeloVentas.addRow(new Object[] {
                    v.getId(), String.format("S/. %.2f", v.getTotal()),
                    v.getUsuarioId() != null ? v.getUsuarioId() : "-"
            }));
            log("Ventas cargadas");
        }

        if (pedidoService != null) {
            pedidoService.listarTodos().forEach(p -> {
                String extras = obtenerExtras(p);
                modeloPedidos.addRow(new Object[] {
                        p.getId(),
                        p.getUsuario() != null ? p.getUsuario().getNombre() : "-",
                        String.format("S/. %.2f", p.getTotal()),
                        p.getEstado(),
                        p.getMetodoPago() != null ? p.getMetodoPago() : "-",
                        extras,
                        p.getFecha() != null ? FMT.format(p.getFecha()) : "-"
                });
            });
            log("Pedidos cargados");
        }
    }

    private String obtenerExtras(Pedido p) {
        if (p.getDetalles() == null)
            return "Sin extras";
        StringBuilder sb = new StringBuilder();
        for (DetallePedido d : p.getDetalles()) {
            if (d.getExtrasAplicados() != null && !d.getExtrasAplicados().isEmpty()) {
                if (sb.length() > 0)
                    sb.append(", ");
                sb.append(d.getExtrasAplicados());
            }
        }
        return sb.length() > 0 ? sb.toString() : "Sin extras";
    }

    private void verDetalles() {
        int fila = tablaPedidos.getSelectedRow();
        if (fila < 0) {
            msg("Seleccione un pedido");
            return;
        }

        Long id = (Long) modeloPedidos.getValueAt(fila, 0);
        if (pedidoService == null)
            return;

        Pedido p = pedidoService.buscarPorId(id).orElse(null);
        if (p == null) {
            msg("Pedido no encontrado");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("PEDIDO #").append(p.getId()).append("\n");
        sb.append("Usuario: ").append(p.getUsuario() != null ? p.getUsuario().getNombre() : "-").append("\n");
        sb.append("Estado: ").append(p.getEstado()).append("\n");
        sb.append("Método Pago: ").append(p.getMetodoPago()).append("\n");
        sb.append("Dirección: ").append(p.getDireccionEnvio()).append("\n\n");
        sb.append("ITEMS (Decorator Pattern):\n");

        if (p.getDetalles() != null) {
            for (DetallePedido d : p.getDetalles()) {
                sb.append("• ").append(d.getProducto() != null ? d.getProducto().getNombre() : "Producto");
                sb.append(" x").append(d.getCantidad()).append("\n");
                sb.append("  Precio: S/. ").append(String.format("%.2f", d.getPrecioUnitario())).append("\n");
                if (d.getExtrasAplicados() != null && !d.getExtrasAplicados().isEmpty()) {
                    sb.append("  Extras: ").append(d.getExtrasAplicados());
                    sb.append(" (+S/. ").append(String.format("%.2f", d.getCostoExtras())).append(")\n");
                }
                sb.append("  Subtotal: S/. ").append(String.format("%.2f", d.getSubtotal())).append("\n\n");
            }
        }
        sb.append("TOTAL: S/. ").append(String.format("%.2f", p.getTotal()));

        JTextArea text = new JTextArea(sb.toString());
        text.setEditable(false);
        text.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(text);
        scroll.setPreferredSize(new Dimension(450, 350));
        JOptionPane.showMessageDialog(this, scroll, "Detalles - Patrón Decorator", JOptionPane.INFORMATION_MESSAGE);
    }

    private void avanzarEstado() {
        int fila = tablaPedidos.getSelectedRow();
        if (fila < 0) {
            msg("Seleccione un pedido");
            return;
        }

        Long id = (Long) modeloPedidos.getValueAt(fila, 0);
        if (pedidoService == null)
            return;

        Pedido actualizado = pedidoService.avanzarEstado(id);
        modeloPedidos.setValueAt(actualizado.getEstado(), fila, 3);
        VentasSubject.getInstance().notificarCambioEstado(id, actualizado.getEstado());
        log("STATE: Pedido #" + id + " → " + actualizado.getEstado());
    }

    private void log(String msg) {
        txtLog.append("[" + LocalTime.now().toString().substring(0, 8) + "] " + msg + "\n");
        txtLog.setCaretPosition(txtLog.getDocument().getLength());
    }

    private void msg(String texto) {
        JOptionPane.showMessageDialog(this, texto);
    }

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
            log("OBSERVER: " + mensaje);
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
