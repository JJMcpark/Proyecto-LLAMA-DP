package com.dpatrones.proyecto.swing;

import com.dpatrones.proyecto.patterns.observer.VentasObserver;
import com.dpatrones.proyecto.patterns.observer.VentasSubject;
import com.dpatrones.proyecto.patterns.singleton.AdminSession;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Panel de Logística - Aplicación Admin (Swing)
 * 
 * Implementa el patrón OBSERVER para actualizarse automáticamente
 * cuando hay cambios en las ventas.
 * 
 * Esta es una implementación BÁSICA para demostrar el patrón.
 */
public class LogisticaPanel extends JPanel implements VentasObserver {
    
    private JTable tablaPedidos;
    private DefaultTableModel modeloTabla;
    private JLabel lblEstado;
    private JTextArea txtLog;
    
    public LogisticaPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Registrar como observador
        VentasSubject.getInstance().agregarObservador(this);
        
        initComponents();
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
        
        panelBotones.add(btnActualizar);
        panelBotones.add(btnAvanzarEstado);
        
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
     * Carga los datos de pedidos (simulación)
     */
    public void cargarDatos() {
        modeloTabla.setRowCount(0);
        
        // En una implementación real, aquí se consultaría la BD
        // Usando el Singleton de conexión:
        // Connection conn = AdminDatabaseConnection.getInstance().getConnection();
        
        // Datos de ejemplo
        modeloTabla.addRow(new Object[]{1, "Juan Pérez", "S/.150.00", "PAGADO", "2024-11-29"});
        modeloTabla.addRow(new Object[]{2, "María García", "S/.89.90", "ENVIADO", "2024-11-29"});
        
        agregarLog("Datos actualizados manualmente");
    }
    
    private void avanzarEstadoSeleccionado() {
        int filaSeleccionada = tablaPedidos.getSelectedRow();
        if (filaSeleccionada >= 0) {
            Long pedidoId = (Long) modeloTabla.getValueAt(filaSeleccionada, 0);
            String estadoActual = (String) modeloTabla.getValueAt(filaSeleccionada, 3);
            
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
            // En una app real, aquí recargaríamos los datos
            // cargarDatos();
        });
    }

    @Override
    public String getNombre() {
        return "Panel Logística Swing";
    }
}
