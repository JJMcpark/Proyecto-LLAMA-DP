package com.dpatrones.proyecto.swing;

import com.dpatrones.proyecto.patterns.singleton.AdminSession;
import com.dpatrones.proyecto.model.Admin;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal de la aplicación Admin (Swing)
 * 
 * Esta es una implementación BÁSICA para demostrar los patrones
 * Singleton y Observer en una aplicación de escritorio.
 */
public class AdminFrame extends JFrame {
    
    private LogisticaPanel panelLogistica;
    
    public AdminFrame() {
        setTitle("Sistema de Administración - Tienda de Ropa");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        initComponents();
    }
    
    private void initComponents() {
        // Menú
        JMenuBar menuBar = new JMenuBar();
        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.addActionListener(e -> {
            AdminSession.getInstance().cerrarSesion();
            System.exit(0);
        });
        menuArchivo.add(itemSalir);
        menuBar.add(menuArchivo);
        setJMenuBar(menuBar);
        
        // Panel principal con pestañas
        JTabbedPane tabbedPane = new JTabbedPane();
        
        panelLogistica = new LogisticaPanel();
        tabbedPane.addTab("Logística", panelLogistica);
        
        // Más pestañas se pueden agregar aquí
        tabbedPane.addTab("Inventario", new JPanel());
        tabbedPane.addTab("Reportes", new JPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Barra de estado
        JLabel statusBar = new JLabel(" Sistema listo | Patrón Singleton y Observer activos");
        statusBar.setBorder(BorderFactory.createLoweredBevelBorder());
        add(statusBar, BorderLayout.SOUTH);
    }
    
    /**
     * Método estático para lanzar la ventana Admin
     */
    public static void iniciarAplicacionAdmin() {
        // Simular login de admin (en producción vendría del login real)
        Admin admin = Admin.builder()
            .id(1L)
            .nombre("Admin Sistema")
            .email("admin@tienda.com")
            .area("Logística")
            .build();
        
        // Usar SINGLETON para guardar la sesión
        AdminSession.getInstance().iniciarSesion(admin);
        
        // Lanzar ventana
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            AdminFrame frame = new AdminFrame();
            frame.setVisible(true);
            
            System.out.println("[SWING] Aplicación Admin iniciada");
        });
    }
}
