package com.dpatrones.proyecto.swing;

import com.dpatrones.proyecto.model.Pedido;
import com.dpatrones.proyecto.patterns.observer.VentasObserver;
import com.dpatrones.proyecto.patterns.observer.VentasSubject;
import com.dpatrones.proyecto.service.PedidoService;
import org.springframework.context.ApplicationContext;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Panel de Dashboard (Swing)
 *
 * - Muestra KPIs de pedidos por estado usando PedidoService
 * - Muestra tabla de pedidos de hoy
 * - Se actualiza automáticamente con el patrón Observer ante nuevas ventas o cambios de estado
 */
public class DashboardPanel extends JPanel implements VentasObserver {

    private final ApplicationContext applicationContext;
    private PedidoService pedidoService;

    private final JLabel lblVentasHoy = new JLabel("S/. 0.00");
    private final JLabel lblPendientes = new JLabel("0");
    private final JLabel lblPagados = new JLabel("0");
    private final JLabel lblEnviados = new JLabel("0");
    private final JLabel lblEntregados = new JLabel("0");
    private final JLabel lblCancelados = new JLabel("0");

    private final DefaultTableModel modeloTabla = new DefaultTableModel(new String[]{"ID", "Usuario", "Total", "Estado", "Fecha"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tablaHoy = new JTable(modeloTabla);

    private static final DateTimeFormatter FECHA_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public DashboardPanel(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        if (this.applicationContext != null) {
            this.pedidoService = this.applicationContext.getBean(PedidoService.class);
        }
        construirUI();
        cargarDatos();
    }

    private void construirUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // KPIs
        JPanel panelKPIs = new JPanel(new GridLayout(2, 3, 10, 10));
        panelKPIs.setBorder(new TitledBorder("Indicadores"));
        panelKPIs.add(kpi("Ventas hoy", lblVentasHoy));
        panelKPIs.add(kpi("Pendientes", lblPendientes));
        panelKPIs.add(kpi("Pagados", lblPagados));
        panelKPIs.add(kpi("Enviados", lblEnviados));
        panelKPIs.add(kpi("Entregados", lblEntregados));
        panelKPIs.add(kpi("Cancelados", lblCancelados));

        add(panelKPIs, BorderLayout.NORTH);

        // Tabla de pedidos de hoy
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(new TitledBorder("Pedidos de hoy"));
        tablaHoy.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelTabla.add(new JScrollPane(tablaHoy), BorderLayout.CENTER);

        add(panelTabla, BorderLayout.CENTER);

        // Botonera
        JButton btnRefrescar = new JButton("Refrescar");
        btnRefrescar.addActionListener(e -> cargarDatos());
        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBtn.add(btnRefrescar);
        add(panelBtn, BorderLayout.SOUTH);
    }

    private JPanel kpi(String titulo, JLabel valor) {
        JPanel p = new JPanel(new BorderLayout());
        JLabel t = new JLabel(titulo, SwingConstants.CENTER);
        t.setFont(t.getFont().deriveFont(Font.BOLD));
        valor.setHorizontalAlignment(SwingConstants.CENTER);
        valor.setFont(valor.getFont().deriveFont(Font.BOLD, 18f));
        p.add(t, BorderLayout.NORTH);
        p.add(valor, BorderLayout.CENTER);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        return p;
    }

    public void cargarDatos() {
        // Limpiar tabla
        modeloTabla.setRowCount(0);

        if (pedidoService != null) {
            try {
                Double ventasHoy = pedidoService.getTotalVentasHoy();
                ventasHoy = ventasHoy == null ? 0.0 : ventasHoy;
                lblVentasHoy.setText(String.format("S/. %.2f", ventasHoy));

                Map<String, Long> stats = pedidoService.getEstadisticasPorEstado();
                lblPendientes.setText(String.valueOf(stats.getOrDefault("PENDIENTE", 0L)));
                lblPagados.setText(String.valueOf(stats.getOrDefault("PAGADO", 0L)));
                lblEnviados.setText(String.valueOf(stats.getOrDefault("ENVIADO", 0L)));
                lblEntregados.setText(String.valueOf(stats.getOrDefault("ENTREGADO", 0L)));
                lblCancelados.setText(String.valueOf(stats.getOrDefault("CANCELADO", 0L)));

                List<Pedido> hoy = pedidoService.getPedidosHoy();
                for (Pedido p : hoy) {
                    String usuarioNombre = (p.getUsuario() != null) ? p.getUsuario().getNombre() : "-";
                    String fechaStr = (p.getFecha() != null) ? FECHA_FMT.format(p.getFecha()) : "-";
                    modeloTabla.addRow(new Object[]{p.getId(), usuarioNombre, String.format("S/. %.2f", p.getTotal()), p.getEstado(), fechaStr});
                }
                return;
            } catch (Exception ex) {
                // Caer a demo si hay error
            }
        }

        // DEMO sin contexto o en error
        lblVentasHoy.setText("S/. 239.90");
        lblPendientes.setText("3");
        lblPagados.setText("5");
        lblEnviados.setText("2");
        lblEntregados.setText("7");
        lblCancelados.setText("1");

        modeloTabla.addRow(new Object[]{101, "Juan Pérez", "S/. 150.00", "PAGADO", "2024-11-29 09:10"});
        modeloTabla.addRow(new Object[]{102, "María García", "S/. 89.90", "ENVIADO", "2024-11-29 10:40"});
    }

    @Override
    public void addNotify() {
        super.addNotify();
        VentasSubject.getInstance().agregarObservador(this);
    }

    @Override
    public void removeNotify() {
        try {
            VentasSubject.getInstance().eliminarObservador(this);
        } catch (Exception ignored) {}
        super.removeNotify();
    }

    @Override
    public void actualizar(String mensaje) {
        // Ante NUEVA_VENTA o CAMBIO_ESTADO refrescar KPIs y tabla
        if (mensaje.startsWith("NUEVA_VENTA") || mensaje.startsWith("CAMBIO_ESTADO")) {
            SwingUtilities.invokeLater(this::cargarDatos);
        }
    }

    @Override
    public String getNombre() {
        return "Dashboard Swing";
    }
}
