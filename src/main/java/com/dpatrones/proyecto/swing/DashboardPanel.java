package com.dpatrones.proyecto.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import org.springframework.context.ApplicationContext;

import com.dpatrones.proyecto.model.Pedido;
import com.dpatrones.proyecto.model.Venta;
import com.dpatrones.proyecto.patterns.observer.VentasObserver;
import com.dpatrones.proyecto.patterns.observer.VentasSubject;
import com.dpatrones.proyecto.service.PedidoService;
import com.dpatrones.proyecto.service.VentaService;

public class DashboardPanel extends JPanel implements VentasObserver {

    private final ApplicationContext applicationContext;
    private PedidoService pedidoService;
    private VentaService ventaService;

    private final JLabel lblVentasFront = new JLabel("S/. 0.00");
    private final JLabel lblTotalVentas = new JLabel("0");
    private final JLabel lblPendientes = new JLabel("0");
    private final JLabel lblPagados = new JLabel("0");
    private final JLabel lblEnviados = new JLabel("0");

    private final DefaultTableModel modeloTabla = new DefaultTableModel(
        new String[]{"ID", "Usuario", "Total", "Estado", "Fecha"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tablaHoy = new JTable(modeloTabla);
    
    private final DefaultTableModel modeloVentas = new DefaultTableModel(
        new String[]{"ID", "Total", "Usuario ID"}, 0);
    private final JTable tablaVentas = new JTable(modeloVentas);

    private static final DateTimeFormatter FECHA_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public DashboardPanel(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        if (this.applicationContext != null) {
            this.pedidoService = this.applicationContext.getBean(PedidoService.class);
            this.ventaService = this.applicationContext.getBean(VentaService.class);
        }
        initUI();
        cargarDatos();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // KPIs - ahora 6 indicadores
        JPanel panelKPIs = new JPanel(new GridLayout(1, 6, 8, 8));
        panelKPIs.setBorder(BorderFactory.createTitledBorder("Indicadores"));
        panelKPIs.add(crearKPI("Ventas Front", lblVentasFront));
        panelKPIs.add(crearKPI("# Ventas", lblTotalVentas));
        panelKPIs.add(crearKPI("Pendientes", lblPendientes));
        panelKPIs.add(crearKPI("Pagados", lblPagados));
        panelKPIs.add(crearKPI("Enviados", lblEnviados));

        add(panelKPIs, BorderLayout.NORTH);

        // Tablas en el centro
        JPanel panelTablas = new JPanel(new GridLayout(2, 1, 5, 5));
        
        JPanel panelVentas = new JPanel(new BorderLayout());
        panelVentas.setBorder(BorderFactory.createTitledBorder("Ventas (Front Web)"));
        panelVentas.add(new JScrollPane(tablaVentas), BorderLayout.CENTER);
        panelTablas.add(panelVentas);
        
        JPanel panelPedidos = new JPanel(new BorderLayout());
        panelPedidos.setBorder(BorderFactory.createTitledBorder("Pedidos (API Checkout)"));
        panelPedidos.add(new JScrollPane(tablaHoy), BorderLayout.CENTER);
        panelTablas.add(panelPedidos);
        
        add(panelTablas, BorderLayout.CENTER);

        // Botón
        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnRefrescar = new JButton("Refrescar");
        btnRefrescar.addActionListener(e -> cargarDatos());
        panelBtn.add(btnRefrescar);
        add(panelBtn, BorderLayout.SOUTH);
    }

    private JPanel crearKPI(String titulo, JLabel valor) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JLabel t = new JLabel(titulo, SwingConstants.CENTER);
        valor.setHorizontalAlignment(SwingConstants.CENTER);
        p.add(t, BorderLayout.NORTH);
        p.add(valor, BorderLayout.CENTER);
        return p;
    }

    public void cargarDatos() {
        modeloTabla.setRowCount(0);
        modeloVentas.setRowCount(0);
        
        // Cargar VENTAS del front
        if (ventaService != null) {
            try {
                List<Venta> ventas = ventaService.listarTodas();
                BigDecimal totalVentas = ventaService.getTotalVentas();
                
                lblVentasFront.setText(String.format("S/. %.2f", totalVentas != null ? totalVentas.doubleValue() : 0.0));
                lblTotalVentas.setText(String.valueOf(ventas.size()));
                
                for (Venta v : ventas) {
                    modeloVentas.addRow(new Object[]{
                        v.getId(),
                        String.format("S/. %.2f", v.getTotal()),
                        v.getUsuarioId() != null ? v.getUsuarioId() : "-"
                    });
                }
            } catch (Exception ex) {
                lblVentasFront.setText("Error");
            }
        } else {
            // Demo ventas
            lblVentasFront.setText("S/. 1,008.20");
            lblTotalVentas.setText("6");
            modeloVentas.addRow(new Object[]{1L, "S/. 139.70", "-"});
            modeloVentas.addRow(new Object[]{2L, "S/. 29.90", "-"});
        }
        
        // Cargar PEDIDOS del API
        if (pedidoService != null) {
            try {
                Map<String, Long> stats = pedidoService.getEstadisticasPorEstado();
                lblPendientes.setText(String.valueOf(stats.getOrDefault("PENDIENTE", 0L)));
                lblPagados.setText(String.valueOf(stats.getOrDefault("PAGADO", 0L)));
                lblEnviados.setText(String.valueOf(stats.getOrDefault("ENVIADO", 0L)));

                List<Pedido> hoy = pedidoService.getPedidosHoy();
                for (Pedido p : hoy) {
                    String usuario = p.getUsuario() != null ? p.getUsuario().getNombre() : "-";
                    String fecha = p.getFecha() != null ? FECHA_FMT.format(p.getFecha()) : "-";
                    modeloTabla.addRow(new Object[]{
                        p.getId(), usuario, String.format("S/. %.2f", p.getTotal()), p.getEstado(), fecha
                    });
                }
            } catch (Exception ex) {
                // demo
                lblPendientes.setText("0");
                lblPagados.setText("0");
                lblEnviados.setText("0");
            }
        } else {
            // Demo pedidos
            lblPendientes.setText("3");
            lblPagados.setText("5");
            lblEnviados.setText("2");
            modeloTabla.addRow(new Object[]{101, "Juan Pérez", "S/. 150.00", "PAGADO", "2024-11-29 09:10"});
        }
    }

    // Observer pattern
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
