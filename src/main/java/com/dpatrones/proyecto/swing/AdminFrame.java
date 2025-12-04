package com.dpatrones.proyecto.swing;

import com.dpatrones.proyecto.patterns.singleton.AdminSession;
import com.dpatrones.proyecto.model.Admin;
import org.springframework.context.ApplicationContext;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal de la aplicación Admin (Swing)
 * 
 * Esta es una implementación BÁSICA para demostrar los patrones
 * Singleton y Observer en una aplicación de escritorio.
 */
public class AdminFrame extends JFrame {

    private final ApplicationContext applicationContext;
    private LogisticaPanel panelLogistica;

    public AdminFrame() {
        this(null);
    }

    public AdminFrame(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        setTitle("LLAMA - Panel de Administración");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
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

        // Dashboard
        DashboardPanel panelDashboard = new DashboardPanel(applicationContext);
        tabbedPane.addTab("Dashboard", panelDashboard);

        panelLogistica = new LogisticaPanel(applicationContext);
        tabbedPane.addTab("Logística", panelLogistica);
        
        // Más pestañas se pueden agregar aquí
        tabbedPane.addTab("Inventario", new JPanel());
        tabbedPane.addTab("Reportes", new JPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Barra de estado
        JLabel statusBar = new JLabel(" Sistema listo | Patrón Singleton y Observer activos");
        statusBar.setBorder(BorderFactory.createLoweredBevelBorder());
        add(statusBar, BorderLayout.SOUTH);

        // Mostrar cantidad de observadores registrados (informativo)
        try {
            int obs = com.dpatrones.proyecto.patterns.observer.VentasSubject.getInstance().getCantidadObservadores();
            statusBar.setText(" Observadores registrados: " + obs + " | Sistema listo");
        } catch (Exception ignored) {}
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
                System.err.println("No se pudo aplicar LookAndFeel: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            }
            
            AdminFrame frame = new AdminFrame();
            frame.setVisible(true);
            
            System.out.println("[SWING] Aplicación Admin iniciada");
        });
    }
}
