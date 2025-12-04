package com.dpatrones.proyecto.swing;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.springframework.context.ApplicationContext;
import com.dpatrones.proyecto.patterns.observer.VentasObserver;
import com.dpatrones.proyecto.patterns.observer.VentasSubject;
import com.dpatrones.proyecto.service.ProductoService;

public class InventarioPanel extends JPanel implements VentasObserver {

    private final ProductoService productoService;
    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[] { "ID", "Nombre", "Categoría", "Precio", "Stock", "Estado" }, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modelo);
    private final JLabel lblInfo = new JLabel("Productos:");

    private static final int STOCK_BAJO = 10;

    public InventarioPanel(ApplicationContext ctx) {
        this.productoService = ctx != null ? ctx.getBean(ProductoService.class) : null;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initUI();
        cargarDatos();
    }

    private void initUI() {
        add(lblInfo, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnRefrescar = new JButton("Refrescar");
        btnRefrescar.addActionListener(e -> cargarDatos());
        JButton btnAgregar = new JButton("Agregar Stock");
        btnAgregar.addActionListener(e -> agregarStock());
        btns.add(btnRefrescar);
        btns.add(btnAgregar);
        add(btns, BorderLayout.SOUTH);
    }

    public void cargarDatos() {
        modelo.setRowCount(0);
        int bajo = 0;

        if (productoService != null) {
            var productos = productoService.listarTodos();
            for (var p : productos) {
                String estado = p.getStock() <= STOCK_BAJO ? "BAJO" : "OK";
                if (p.getStock() <= STOCK_BAJO)
                    bajo++;
                modelo.addRow(new Object[] {
                        p.getId(), p.getNombre(), p.getCategoria(),
                        String.format("S/. %.2f", p.getPrecio()), p.getStock(), estado
                });
            }
            lblInfo.setText("Total: " + productos.size() + " | Stock bajo: " + bajo);
        }
    }

    private void agregarStock() {
        int row = tabla.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto");
            return;
        }

        String input = JOptionPane.showInputDialog(this, "Cantidad a agregar:");
        if (input != null && !input.trim().isEmpty()) {
            try {
                int cantidad = Integer.parseInt(input.trim());
                int actual = (int) modelo.getValueAt(row, 4);
                int nuevo = actual + cantidad;
                modelo.setValueAt(nuevo, row, 4);
                modelo.setValueAt(nuevo <= STOCK_BAJO ? "BAJO" : "OK", row, 5);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Número inválido");
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
