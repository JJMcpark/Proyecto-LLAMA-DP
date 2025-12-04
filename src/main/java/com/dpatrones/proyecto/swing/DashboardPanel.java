package com.dpatrones.proyecto.swing;

import java.awt.*;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.springframework.context.ApplicationContext;
import com.dpatrones.proyecto.patterns.observer.VentasObserver;
import com.dpatrones.proyecto.patterns.observer.VentasSubject;
import com.dpatrones.proyecto.service.PedidoService;
import com.dpatrones.proyecto.service.VentaService;

public class DashboardPanel extends JPanel implements VentasObserver {

    private final PedidoService pedidoService;
    private final VentaService ventaService;

    private final JLabel lblVentasFront = new JLabel("S/. 0.00");
    private final JLabel lblTotalVentas = new JLabel("0");
    private final JLabel lblPendientes = new JLabel("0");
    private final JLabel lblPagados = new JLabel("0");
    private final JLabel lblEnviados = new JLabel("0");

    private final DefaultTableModel modeloVentas = new DefaultTableModel(new String[] { "ID", "Total", "Usuario" }, 0);
    private final DefaultTableModel modeloPedidos = new DefaultTableModel(
            new String[] { "ID", "Usuario", "Total", "Estado", "Fecha" }, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public DashboardPanel(ApplicationContext ctx) {
        this.pedidoService = ctx != null ? ctx.getBean(PedidoService.class) : null;
        this.ventaService = ctx != null ? ctx.getBean(VentaService.class) : null;
        initUI();
        cargarDatos();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel kpis = new JPanel(new GridLayout(1, 5, 8, 8));
        kpis.setBorder(BorderFactory.createTitledBorder("Indicadores"));
        kpis.add(crearKPI("Total Ventas", lblVentasFront));
        kpis.add(crearKPI("# Ventas", lblTotalVentas));
        kpis.add(crearKPI("Pendientes", lblPendientes));
        kpis.add(crearKPI("Pagados", lblPagados));
        kpis.add(crearKPI("Enviados", lblEnviados));
        add(kpis, BorderLayout.NORTH);

        JPanel tablas = new JPanel(new GridLayout(2, 1, 5, 5));
        tablas.add(crearPanelTabla("Ventas", new JTable(modeloVentas)));
        tablas.add(crearPanelTabla("Pedidos", new JTable(modeloPedidos)));
        add(tablas, BorderLayout.CENTER);

        JButton btn = new JButton("Refrescar");
        btn.addActionListener(e -> cargarDatos());
        JPanel sur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        sur.add(btn);
        add(sur, BorderLayout.SOUTH);
    }

    private JPanel crearKPI(String titulo, JLabel valor) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        p.add(new JLabel(titulo, SwingConstants.CENTER), BorderLayout.NORTH);
        valor.setHorizontalAlignment(SwingConstants.CENTER);
        p.add(valor, BorderLayout.CENTER);
        return p;
    }

    private JPanel crearPanelTabla(String titulo, JTable tabla) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createTitledBorder(titulo));
        p.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return p;
    }

    public void cargarDatos() {
        modeloVentas.setRowCount(0);
        modeloPedidos.setRowCount(0);

        if (ventaService != null) {
            var ventas = ventaService.listarTodas();
            BigDecimal total = ventaService.getTotalVentas();
            lblVentasFront.setText(String.format("S/. %.2f", total != null ? total : BigDecimal.ZERO));
            lblTotalVentas.setText(String.valueOf(ventas.size()));
            ventas.forEach(v -> modeloVentas.addRow(new Object[] {
                    v.getId(), String.format("S/. %.2f", v.getTotal()),
                    v.getUsuarioId() != null ? v.getUsuarioId() : "-"
            }));
        }

        if (pedidoService != null) {
            Map<String, Long> stats = pedidoService.getEstadisticasPorEstado();
            lblPendientes.setText(String.valueOf(stats.getOrDefault("PENDIENTE", 0L)));
            lblPagados.setText(String.valueOf(stats.getOrDefault("PAGADO", 0L)));
            lblEnviados.setText(String.valueOf(stats.getOrDefault("ENVIADO", 0L)));

            pedidoService.getPedidosHoy().forEach(p -> modeloPedidos.addRow(new Object[] {
                    p.getId(),
                    p.getUsuario() != null ? p.getUsuario().getNombre() : "-",
                    String.format("S/. %.2f", p.getTotal()),
                    p.getEstado(),
                    p.getFecha() != null ? FMT.format(p.getFecha()) : "-"
            }));
        }
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
        if (mensaje.startsWith("NUEVA_VENTA") || mensaje.startsWith("CAMBIO_ESTADO")) {
            SwingUtilities.invokeLater(this::cargarDatos);
        }
    }

    @Override
    public String getNombre() {
        return "Dashboard";
    }
}
