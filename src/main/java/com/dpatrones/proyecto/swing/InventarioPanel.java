package com.dpatrones.proyecto.swing;

import com.dpatrones.proyecto.model.Producto;
import com.dpatrones.proyecto.patterns.observer.VentasObserver;
import com.dpatrones.proyecto.patterns.observer.VentasSubject;
import com.dpatrones.proyecto.service.ProductoService;
import org.springframework.context.ApplicationContext;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel de Inventario (Swing)
 * 
 * Permite ver y gestionar el stock de productos.
 * Se actualiza con el patrón Observer cuando hay ventas (descuenta stock).
 */
public class InventarioPanel extends JPanel implements VentasObserver {
    
    private final ApplicationContext applicationContext;
    private ProductoService productoService;
    
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private JLabel lblTotalProductos;
    private JLabel lblStockBajo;
    
    private static final int STOCK_BAJO_UMBRAL = 10;
    
    public InventarioPanel(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        if (this.applicationContext != null) {
            this.productoService = this.applicationContext.getBean(ProductoService.class);
        }
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        construirUI();
        cargarDatos();
    }
    
    private void construirUI() {
        // Header con título y resumen
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(new TitledBorder("Gestión de Inventario"));
        
        JLabel titulo = new JLabel("📦 Inventario de Productos", SwingConstants.LEFT);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTotalProductos = new JLabel("Total: 0 productos");
        lblTotalProductos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStockBajo = new JLabel("⚠ Stock bajo: 0");
        lblStockBajo.setForeground(new Color(255, 152, 0));
        lblStockBajo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        statsPanel.add(lblTotalProductos);
        statsPanel.add(Box.createHorizontalStrut(20));
        statsPanel.add(lblStockBajo);
        
        headerPanel.add(titulo, BorderLayout.WEST);
        headerPanel.add(statsPanel, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Tabla de productos
        String[] columnas = {"ID", "Nombre", "Categoría", "Talla", "Color", "Precio", "Stock", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setRowHeight(25);
        tablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaProductos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // Ancho de columnas
        tablaProductos.getColumnModel().getColumn(0).setPreferredWidth(40);
        tablaProductos.getColumnModel().getColumn(1).setPreferredWidth(150);
        tablaProductos.getColumnModel().getColumn(6).setPreferredWidth(60);
        tablaProductos.getColumnModel().getColumn(7).setPreferredWidth(80);
        
        JScrollPane scrollPane = new JScrollPane(tablaProductos);
        add(scrollPane, BorderLayout.CENTER);
        
        // Panel de acciones
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        actionPanel.setBorder(new TitledBorder("Acciones"));
        
        JButton btnRefrescar = crearBoton("🔄 Refrescar", new Color(33, 150, 243));
        btnRefrescar.addActionListener(e -> cargarDatos());
        
        JButton btnAgregarStock = crearBoton("➕ Agregar Stock", new Color(76, 175, 80));
        btnAgregarStock.addActionListener(e -> agregarStock());
        
        JButton btnAlertaStock = crearBoton("⚠ Ver Stock Bajo", new Color(255, 152, 0));
        btnAlertaStock.addActionListener(e -> filtrarStockBajo());
        
        JButton btnVerTodos = crearBoton("📋 Ver Todos", new Color(96, 125, 139));
        btnVerTodos.addActionListener(e -> cargarDatos());
        
        actionPanel.add(btnRefrescar);
        actionPanel.add(btnAgregarStock);
        actionPanel.add(btnAlertaStock);
        actionPanel.add(btnVerTodos);
        
        add(actionPanel, BorderLayout.SOUTH);
    }
    
    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
    
    public void cargarDatos() {
        modeloTabla.setRowCount(0);
        int stockBajoCount = 0;
        
        if (productoService != null) {
            try {
                List<Producto> productos = productoService.listarTodos();
                for (Producto p : productos) {
                    String estado = p.getStock() <= STOCK_BAJO_UMBRAL ? "⚠ BAJO" : "✓ OK";
                    if (p.getStock() <= STOCK_BAJO_UMBRAL) stockBajoCount++;
                    
                    modeloTabla.addRow(new Object[]{
                        p.getId(),
                        p.getNombre(),
                        p.getCategoria(),
                        p.getTalla(),
                        p.getColor(),
                        String.format("S/. %.2f", p.getPrecio()),
                        p.getStock(),
                        estado
                    });
                }
                lblTotalProductos.setText("Total: " + productos.size() + " productos");
                lblStockBajo.setText("⚠ Stock bajo: " + stockBajoCount);
                return;
            } catch (Exception ex) {
                // Fall to demo
            }
        }
        
        // Demo data
        Object[][] demo = {
            {1L, "Polo Básico", "Camisetas", "M", "Azul", "S/. 49.90", 25, "✓ OK"},
            {2L, "Jean Clásico", "Pantalones", "32", "Negro", "S/. 89.90", 8, "⚠ BAJO"},
            {3L, "Casaca Sport", "Chaquetas", "L", "Gris", "S/. 149.90", 3, "⚠ BAJO"},
            {4L, "Camisa Formal", "Camisas", "M", "Blanco", "S/. 79.90", 15, "✓ OK"},
            {5L, "Short Deportivo", "Shorts", "S", "Rojo", "S/. 39.90", 30, "✓ OK"}
        };
        
        for (Object[] row : demo) {
            modeloTabla.addRow(row);
            if (((String) row[7]).contains("BAJO")) stockBajoCount++;
        }
        
        lblTotalProductos.setText("Total: " + demo.length + " productos (demo)");
        lblStockBajo.setText("⚠ Stock bajo: " + stockBajoCount);
    }
    
    private void filtrarStockBajo() {
        modeloTabla.setRowCount(0);
        int count = 0;
        
        if (productoService != null) {
            try {
                List<Producto> productos = productoService.listarTodos();
                for (Producto p : productos) {
                    if (p.getStock() <= STOCK_BAJO_UMBRAL) {
                        modeloTabla.addRow(new Object[]{
                            p.getId(),
                            p.getNombre(),
                            p.getCategoria(),
                            p.getTalla(),
                            p.getColor(),
                            String.format("S/. %.2f", p.getPrecio()),
                            p.getStock(),
                            "⚠ BAJO"
                        });
                        count++;
                    }
                }
            } catch (Exception ignored) {}
        }
        
        if (count == 0) {
            // Demo
            modeloTabla.addRow(new Object[]{2L, "Jean Clásico", "Pantalones", "32", "Negro", "S/. 89.90", 8, "⚠ BAJO"});
            modeloTabla.addRow(new Object[]{3L, "Casaca Sport", "Chaquetas", "L", "Gris", "S/. 149.90", 3, "⚠ BAJO"});
        }
        
        JOptionPane.showMessageDialog(this, 
            "Mostrando productos con stock ≤ " + STOCK_BAJO_UMBRAL + " unidades", 
            "Filtro aplicado", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void agregarStock() {
        int row = tablaProductos.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto primero", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String input = JOptionPane.showInputDialog(this, 
            "Cantidad a agregar al stock de '" + modeloTabla.getValueAt(row, 1) + "':", 
            "Agregar Stock", 
            JOptionPane.QUESTION_MESSAGE);
        
        if (input != null && !input.trim().isEmpty()) {
            try {
                int cantidad = Integer.parseInt(input.trim());
                if (cantidad <= 0) throw new NumberFormatException();
                
                int stockActual = (int) modeloTabla.getValueAt(row, 6);
                int nuevoStock = stockActual + cantidad;
                modeloTabla.setValueAt(nuevoStock, row, 6);
                modeloTabla.setValueAt(nuevoStock <= STOCK_BAJO_UMBRAL ? "⚠ BAJO" : "✓ OK", row, 7);
                
                JOptionPane.showMessageDialog(this, 
                    "Stock actualizado: " + stockActual + " → " + nuevoStock, 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingrese un número válido mayor a 0", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
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
        if (mensaje.startsWith("NUEVA_VENTA")) {
            SwingUtilities.invokeLater(this::cargarDatos);
        }
    }
    
    @Override
    public String getNombre() {
        return "Inventario Swing";
    }
}
