package com.dpatrones.proyecto.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import org.springframework.context.ApplicationContext;

import com.dpatrones.proyecto.model.Admin;
import com.dpatrones.proyecto.patterns.observer.VentasSubject;
import com.dpatrones.proyecto.patterns.singleton.AdminSession;

/**
 * Ventana principal de la aplicación Admin (Swing)
 * 
 * Panel de administración con:
 * - Dashboard: KPIs y estadísticas
 * - Logística: Gestión de pedidos y estados
 * - Inventario: Control de stock
 * 
 * Demuestra los patrones: Singleton, Observer, State
 */
public class AdminFrame extends JFrame {

    private final ApplicationContext applicationContext;
    private JLabel lblUsuario;
    private JLabel lblObservadores;

    public AdminFrame() {
        this(null);
    }

    public AdminFrame(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        
        setTitle("🦙 LLAMA - Panel de Administración");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1000, 700);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        
        // Icono de la ventana (emoji como texto en título)
        
        initComponents();
        initMenuBar();
        initStatusBar();
        
        // Confirmar antes de cerrar
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmarSalida();
            }
        });
    }

    private void initComponents() {
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));
        
        // Header con bienvenida
        JPanel headerPanel = crearHeader();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Panel de pestañas con iconos
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabbedPane.setBackground(Color.WHITE);
        
        // Dashboard
        DashboardPanel panelDashboard = new DashboardPanel(applicationContext);
        tabbedPane.addTab("📊 Dashboard", panelDashboard);
        
        // Logística
        LogisticaPanel panelLogistica = new LogisticaPanel(applicationContext);
        tabbedPane.addTab("🚚 Logística", panelLogistica);
        
        // Inventario
        InventarioPanel panelInventario = new InventarioPanel(applicationContext);
        tabbedPane.addTab("📦 Inventario", panelInventario);
        
        // Reportes (placeholder)
        JPanel panelReportes = crearPanelReportes();
        tabbedPane.addTab("📈 Reportes", panelReportes);
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
    }
    
    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(63, 81, 181));
        header.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        // Logo y título
        JLabel lblTitulo = new JLabel("🦙 LLAMA Admin");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        
        // Info del usuario
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);
        
        String nombreAdmin = AdminSession.getInstance().getNombreAdmin();
        String areaAdmin = AdminSession.getInstance().getAreaAdmin();
        
        lblUsuario = new JLabel("👤 " + nombreAdmin + " (" + areaAdmin + ")");
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblUsuario.setForeground(new Color(200, 200, 255));
        
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setBackground(new Color(244, 67, 54));
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setBorderPainted(false);
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrarSesion.addActionListener(e -> confirmarSalida());
        
        userPanel.add(lblUsuario);
        userPanel.add(Box.createHorizontalStrut(15));
        userPanel.add(btnCerrarSesion);
        
        header.add(lblTitulo, BorderLayout.WEST);
        header.add(userPanel, BorderLayout.EAST);
        
        return header;
    }
    
    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE);
        
        // Menú Archivo
        JMenu menuArchivo = new JMenu("Archivo");
        
        JMenuItem itemRefrescar = new JMenuItem("🔄 Refrescar Todo");
        itemRefrescar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Datos refrescados", "Info", JOptionPane.INFORMATION_MESSAGE);
        });
        
        JMenuItem itemSalir = new JMenuItem("🚪 Salir");
        itemSalir.addActionListener(e -> confirmarSalida());
        
        menuArchivo.add(itemRefrescar);
        menuArchivo.addSeparator();
        menuArchivo.add(itemSalir);
        
        // Menú Ayuda
        JMenu menuAyuda = new JMenu("Ayuda");
        
        JMenuItem itemPatrones = new JMenuItem("📚 Patrones Implementados");
        itemPatrones.addActionListener(e -> mostrarInfoPatrones());
        
        JMenuItem itemAcerca = new JMenuItem("ℹ️ Acerca de");
        itemAcerca.addActionListener(e -> mostrarAcercaDe());
        
        menuAyuda.add(itemPatrones);
        menuAyuda.add(itemAcerca);
        
        menuBar.add(menuArchivo);
        menuBar.add(menuAyuda);
        
        setJMenuBar(menuBar);
    }
    
    private void initStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(new Color(240, 240, 240));
        statusBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY),
            new EmptyBorder(5, 10, 5, 10)
        ));
        
        lblObservadores = new JLabel("👁 Observadores: 0");
        lblObservadores.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        JLabel lblEstado = new JLabel("✅ Sistema operativo | Patrones: Singleton, Observer, State, Factory, Decorator, Facade");
        lblEstado.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblEstado.setForeground(Color.GRAY);
        
        statusBar.add(lblObservadores, BorderLayout.WEST);
        statusBar.add(lblEstado, BorderLayout.EAST);
        
        add(statusBar, BorderLayout.SOUTH);
        
        // Actualizar contador de observadores
        Timer timer = new Timer(2000, e -> actualizarContadorObservadores());
        timer.start();
    }
    
    private void actualizarContadorObservadores() {
        try {
            int count = VentasSubject.getInstance().getCantidadObservadores();
            lblObservadores.setText("👁 Observadores activos: " + count);
        } catch (Exception ignored) {}
    }
    
    private JPanel crearPanelReportes() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitulo = new JLabel("📈 Módulo de Reportes", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        
        JLabel lblProximamente = new JLabel("🚧 Próximamente: Reportes de ventas, gráficos y exportación", SwingConstants.CENTER);
        lblProximamente.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblProximamente.setForeground(Color.GRAY);
        
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        centerPanel.setOpaque(false);
        centerPanel.add(lblTitulo);
        centerPanel.add(lblProximamente);
        
        panel.add(centerPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void mostrarInfoPatrones() {
        String info = """
            🎨 PATRONES DE DISEÑO IMPLEMENTADOS
            ═══════════════════════════════════════
            
            🔒 SINGLETON
            • AdminSession: Sesión única del administrador
            • VentasSubject: Subject único para notificaciones
            
            🏭 FACTORY METHOD
            • PaymentFactory: Crea procesadores de pago
            
            🎨 DECORATOR
            • Extras de productos: Bordado, Estampado, Empaque
            
            👁 OBSERVER
            • Dashboard, Logística e Inventario se actualizan
              automáticamente ante cambios
            
            📦 STATE
            • Estados del pedido: Pendiente → Pagado → Enviado → Entregado
            
            🎭 FACADE
            • OrderFacade: Simplifica el proceso de checkout
            """;
        
        JTextArea textArea = new JTextArea(info);
        textArea.setEditable(false);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        
        JOptionPane.showMessageDialog(this, 
            new JScrollPane(textArea), 
            "Patrones de Diseño", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void mostrarAcercaDe() {
        String acerca = """
            🦙 LLAMA - Sistema de Administración
            ─────────────────────────────────────
            
            Proyecto de Diseño de Patrones
            Universidad Tecnológica del Perú
            
            Versión: 1.0.0
            Año: 2025
            
            Desarrollado con:
            • Java 21
            • Spring Boot 3.4.1
            • Swing
            
            Equipo LLAMA 🦙
            """;
        
        JOptionPane.showMessageDialog(this, 
            acerca, 
            "Acerca de LLAMA", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void confirmarSalida() {
        int option = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea cerrar sesión y salir?",
            "Confirmar salida",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (option == JOptionPane.YES_OPTION) {
            AdminSession.getInstance().cerrarSesion();
            System.out.println("🚪 Sesión cerrada. Hasta pronto!");
            dispose();
            System.exit(0);
        }
    }
    
    /**
     * Método estático para lanzar la ventana Admin (sin login)
     * Útil para pruebas rápidas
     */
    public static void iniciarAplicacionAdmin() {
        Admin admin = Admin.builder()
            .id(1L)
            .nombre("Admin Sistema")
            .email("admin@tienda.com")
            .area("SUPERVISOR")
            .build();
        
        AdminSession.getInstance().iniciarSesion(admin);
        
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("No se pudo aplicar LookAndFeel: " + e.getMessage());
            }
            
            AdminFrame frame = new AdminFrame();
            frame.setVisible(true);
            
            System.out.println("[SWING] Aplicación Admin iniciada");
        });
    }
}
