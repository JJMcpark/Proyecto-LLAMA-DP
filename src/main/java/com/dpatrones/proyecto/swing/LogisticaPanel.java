package com.dpatrones.proyecto.swing;

import com.dpatrones.proyecto.model.Pedido;
import com.dpatrones.proyecto.patterns.observer.VentasObserver;
import com.dpatrones.proyecto.patterns.observer.VentasSubject;
import com.dpatrones.proyecto.patterns.singleton.AdminSession;
import com.dpatrones.proyecto.service.PedidoService;
import org.springframework.context.ApplicationContext;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Panel de Logística - Aplicación Admin (Swing)
 * 
 * Implementa el patrón OBSERVER para actualizarse automáticamente
 * cuando hay cambios en las ventas.
 * 
 * Esta es una implementación BÁSICA para demostrar el patrón.
 */
public class LogisticaPanel extends JPanel implements VentasObserver {

    private final ApplicationContext applicationContext;
    private PedidoService pedidoService;

    private JTable tablaPedidos;
    private DefaultTableModel modeloTabla;
    private JLabel lblEstado;
    private JTextArea txtLog;

    private static final DateTimeFormatter FECHA_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public LogisticaPanel(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        if (this.applicationContext != null) {
            this.pedidoService = this.applicationContext.getBean(PedidoService.class);
        }

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initComponents();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        // Registrar como observador cuando el componente está agregado al árbol
        VentasSubject.getInstance().agregarObservador(this);
    }

    @Override
    public void removeNotify() {
        // Eliminar registro para evitar fugas de memoria
        try {
            VentasSubject.getInstance().eliminarObservador(this);
        } catch (Exception ignored) {}
        super.removeNotify();
    }
    
    private void initComponents() {
        // Panel superior con título y estado
        JPanel panelSuperior = new JPanel(new BorderLayout());
        JLabel titulo = new JLabel("Panel de Logística - Admin", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panelSuperior.add(titulo, BorderLayout.CENTER);
        
        lblEstado = new JLabel("Admin: " + AdminSession.getInstance().getNombreAdmin());
        panelSuperior.add(lblEstado, BorderLayout.EAST);
        
        add(panelSuperior, BorderLayout.NORTH);
        
        // Tabla de pedidos
        String[] columnas = {"ID", "Usuario", "Total", "Estado", "Fecha"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaPedidos = new JTable(modeloTabla);
        tablaPedidos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollTabla = new JScrollPane(tablaPedidos);
        scrollTabla.setPreferredSize(new Dimension(600, 200));
        add(scrollTabla, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> cargarDatos());
        
        JButton btnAvanzarEstado = new JButton("Avanzar Estado");
        btnAvanzarEstado.addActionListener(e -> avanzarEstadoSeleccionado());

        JButton btnSimularVenta = new JButton("Simular Venta");
        btnSimularVenta.addActionListener(e -> VentasSubject.getInstance().notificarNuevaVenta(999L, 123.45));
        
        panelBotones.add(btnActualizar);
        panelBotones.add(btnAvanzarEstado);
        panelBotones.add(btnSimularVenta);
        
        // Log de eventos
        txtLog = new JTextArea(5, 50);
        txtLog.setEditable(false);
        JScrollPane scrollLog = new JScrollPane(txtLog);
        scrollLog.setBorder(BorderFactory.createTitledBorder("Log de Eventos (Observer)"));
        
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(panelBotones, BorderLayout.NORTH);
        panelInferior.add(scrollLog, BorderLayout.CENTER);
        
        add(panelInferior, BorderLayout.SOUTH);
    }
    
    /**
     * Carga los datos de pedidos desde la BD si hay contexto; si no, simula.
     */
    public void cargarDatos() {
        modeloTabla.setRowCount(0);

        if (pedidoService != null) {
            try {
                List<Pedido> pedidos = pedidoService.listarTodos();
                for (Pedido p : pedidos) {
                    String usuarioNombre = (p.getUsuario() != null) ? p.getUsuario().getNombre() : "-";
                    String fechaStr = (p.getFecha() != null) ? FECHA_FMT.format(p.getFecha()) : "-";
                    modeloTabla.addRow(new Object[]{p.getId(), usuarioNombre, String.format("S/. %.2f", p.getTotal()), p.getEstado(), fechaStr});
                }
                agregarLog("Datos cargados desde BD: " + pedidos.size() + " pedidos");
                return;
            } catch (Exception ex) {
                agregarLog("Error cargando pedidos de BD: " + ex.getMessage());
            }
        }

        // Fallback: datos de ejemplo
        modeloTabla.addRow(new Object[]{1L, "Juan Pérez", "S/. 150.00", "PAGADO", "2024-11-29 10:00"});
        modeloTabla.addRow(new Object[]{2L, "María García", "S/. 89.90", "ENVIADO", "2024-11-29 11:30"});
        agregarLog("Datos de ejemplo cargados (sin contexto)");
    }
    
    private void avanzarEstadoSeleccionado() {
        int filaSeleccionada = tablaPedidos.getSelectedRow();
        if (filaSeleccionada >= 0) {
            Long pedidoId = (Long) modeloTabla.getValueAt(filaSeleccionada, 0);
            String estadoActual = (String) modeloTabla.getValueAt(filaSeleccionada, 3);
            
            if (pedidoService != null) {
                try {
                    Pedido actualizado = pedidoService.avanzarEstado(pedidoId);
                    String nuevoEstado = actualizado.getEstado();
                    modeloTabla.setValueAt(nuevoEstado, filaSeleccionada, 3);
                    VentasSubject.getInstance().notificarCambioEstado(pedidoId, nuevoEstado);
                    agregarLog("Pedido #" + pedidoId + " avanzado a " + nuevoEstado + " (BD)");
                } catch (Exception ex) {
                    agregarLog("Error al avanzar estado en BD: " + ex.getMessage());
                }
            } else {
                // Simular cambio de estado
                String nuevoEstado = switch (estadoActual) {
                    case "PENDIENTE" -> "PAGADO";
                    case "PAGADO" -> "ENVIADO";
                    case "ENVIADO" -> "ENTREGADO";
                    default -> estadoActual;
                };

                modeloTabla.setValueAt(nuevoEstado, filaSeleccionada, 3);

                // Notificar a otros observadores
                VentasSubject.getInstance().notificarCambioEstado(pedidoId, nuevoEstado);
                agregarLog("Pedido #" + pedidoId + " avanzado a " + nuevoEstado + " (simulado)");
            }
            
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un pedido primero");
        }
    }
    
    private void agregarLog(String mensaje) {
        txtLog.append("[" + java.time.LocalTime.now().toString().substring(0, 8) + "] " + mensaje + "\n");
        txtLog.setCaretPosition(txtLog.getDocument().getLength());
    }
    
    // ============ PATRÓN OBSERVER ============
    
    @Override
    public void actualizar(String mensaje) {
        // Este método es llamado automáticamente cuando hay cambios
        SwingUtilities.invokeLater(() -> {
            agregarLog("OBSERVER: " + mensaje);
            // Refrescar datos cuando hay eventos relevantes
            if (mensaje.startsWith("NUEVA_VENTA") || mensaje.startsWith("CAMBIO_ESTADO")) {
                cargarDatos();
            }
        });
    }

    @Override
    public String getNombre() {
        return "Panel Logística Swing";
    }
}
