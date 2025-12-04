package com.dpatrones.proyecto.swing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import org.springframework.context.ApplicationContext;

import com.dpatrones.proyecto.model.Producto;
import com.dpatrones.proyecto.patterns.observer.VentasObserver;
import com.dpatrones.proyecto.patterns.observer.VentasSubject;
import com.dpatrones.proyecto.service.ProductoService;

public class InventarioPanel extends JPanel implements VentasObserver {
    
    private final ApplicationContext applicationContext;
    private ProductoService productoService;
    
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private JLabel lblInfo;
    
    private static final int STOCK_BAJO = 10;
    
    public InventarioPanel(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        if (this.applicationContext != null) {
            this.productoService = this.applicationContext.getBean(ProductoService.class);
        }
        
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        initUI();
        cargarDatos();
    }
    
    private void initUI() {
        // Info
        lblInfo = new JLabel("Productos en inventario:");
        add(lblInfo, BorderLayout.NORTH);
        
        // Tabla
        String[] columnas = {"ID", "Nombre", "Categoría", "Precio", "Stock", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaProductos = new JTable(modeloTabla);
        add(new JScrollPane(tablaProductos), BorderLayout.CENTER);
        
        // Botones
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton btnRefrescar = new JButton("Refrescar");
        btnRefrescar.addActionListener(e -> cargarDatos());
        
        JButton btnAgregar = new JButton("Agregar Stock");
        btnAgregar.addActionListener(e -> agregarStock());
        
        btnPanel.add(btnRefrescar);
        btnPanel.add(btnAgregar);
        
        add(btnPanel, BorderLayout.SOUTH);
    }
    
    public void cargarDatos() {
        modeloTabla.setRowCount(0);
        int stockBajo = 0;
        
        if (productoService != null) {
            try {
                List<Producto> productos = productoService.listarTodos();
                for (Producto p : productos) {
                    String estado = p.getStock() <= STOCK_BAJO ? "BAJO" : "OK";
                    if (p.getStock() <= STOCK_BAJO) stockBajo++;
                    
                    modeloTabla.addRow(new Object[]{
                        p.getId(),
                        p.getNombre(),
                        p.getCategoria(),
                        String.format("S/. %.2f", p.getPrecio()),
                        p.getStock(),
                        estado
                    });
                }
                lblInfo.setText("Total: " + productos.size() + " productos | Stock bajo: " + stockBajo);
                return;
            } catch (Exception ex) {
                // usar demo
            }
        }
        
        // Demo
        modeloTabla.addRow(new Object[]{1L, "Polo Básico", "Camisetas", "S/. 49.90", 25, "OK"});
        modeloTabla.addRow(new Object[]{2L, "Jean Clásico", "Pantalones", "S/. 89.90", 5, "BAJO"});
        modeloTabla.addRow(new Object[]{3L, "Casaca", "Chaquetas", "S/. 149.90", 15, "OK"});
        lblInfo.setText("Total: 3 productos (demo)");
    }
    
    private void agregarStock() {
        int row = tablaProductos.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto");
            return;
        }
        
        String input = JOptionPane.showInputDialog(this, "Cantidad a agregar:");
        if (input != null && !input.trim().isEmpty()) {
            try {
                int cantidad = Integer.parseInt(input.trim());
                int actual = (int) modeloTabla.getValueAt(row, 4);
                int nuevo = actual + cantidad;
                modeloTabla.setValueAt(nuevo, row, 4);
                modeloTabla.setValueAt(nuevo <= STOCK_BAJO ? "BAJO" : "OK", row, 5);
                JOptionPane.showMessageDialog(this, "Stock actualizado: " + nuevo);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Número inválido");
            }
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
        if (mensaje.startsWith("NUEVA_VENTA")) {
            SwingUtilities.invokeLater(this::cargarDatos);
        }
    }
    
    @Override
    public String getNombre() {
        return "Inventario";
    }
}
