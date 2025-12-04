package com.dpatrones.proyecto.swing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
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
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.springframework.context.ApplicationContext;

import com.dpatrones.proyecto.model.Admin;
import com.dpatrones.proyecto.patterns.observer.VentasSubject;
import com.dpatrones.proyecto.patterns.singleton.AdminSession;

public class AdminFrame extends JFrame {

    private final ApplicationContext applicationContext;
    private JLabel lblStatus;

    public AdminFrame() {
        this(null);
    }

    public AdminFrame(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        
        setTitle("LLAMA - Panel de Administración");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 650);
        setLocationRelativeTo(null);
        
        initComponents();
        iniciarActualizadorObservadores();
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
        
        JMenu menuAyuda = new JMenu("Ayuda");
        JMenuItem itemPatrones = new JMenuItem("Ver Patrones");
        itemPatrones.addActionListener(e -> mostrarPatrones());
        menuAyuda.add(itemPatrones);
        
        menuBar.add(menuArchivo);
        menuBar.add(menuAyuda);
        setJMenuBar(menuBar);
        
        // Panel superior con info del admin (SINGLETON)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String nombreAdmin = AdminSession.getInstance().getNombreAdmin();
        JLabel lblAdmin = new JLabel("Usuario: " + nombreAdmin + " | Sesión activa (Singleton)");
        headerPanel.add(lblAdmin, BorderLayout.WEST);
        
        JButton btnCerrar = new JButton("Cerrar Sesión");
        btnCerrar.addActionListener(e -> {
            AdminSession.getInstance().cerrarSesion();
            dispose();
            System.exit(0);
        });
        headerPanel.add(btnCerrar, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Pestañas
        JTabbedPane tabbedPane = new JTabbedPane();
        
        tabbedPane.addTab("Dashboard", new DashboardPanel(applicationContext));
        tabbedPane.addTab("Logística", new LogisticaPanel(applicationContext));
        
        add(tabbedPane, BorderLayout.CENTER);
        
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBar.setBorder(BorderFactory.createLoweredBevelBorder());
        
        lblStatus = new JLabel("Observadores: 0 | Patrones: Singleton, Observer, State, Factory, Decorator, Facade");
        statusBar.add(lblStatus);
        
        add(statusBar, BorderLayout.SOUTH);
    }
    
    private void iniciarActualizadorObservadores() {
        Timer timer = new Timer(500, e -> {
            int obs = VentasSubject.getInstance().getCantidadObservadores();
            lblStatus.setText("Observadores: " + obs + " | Patrones: Singleton, Observer, State, Factory, Decorator, Facade");
        });
        timer.setRepeats(true);
        timer.start();
    }
    
    private void mostrarPatrones() {
        String info = """
            PATRONES DE DISEÑO EN ESTE PROYECTO
            ====================================
            
            SINGLETON:
            - AdminSession: Sesión única del administrador
            - VentasSubject: Un solo subject para notificaciones
            
            OBSERVER:
            - Dashboard y Logística se actualizan automáticamente
            - Usa VentasSubject.notificar() para avisar cambios
            
            STATE:
            - Estados del pedido: PENDIENTE -> PAGADO -> ENVIADO -> ENTREGADO
            - Cada estado tiene su comportamiento
            
            FACTORY:
            - PaymentFactory crea procesadores de pago
            
            DECORATOR:
            - Extras de productos: Bordado, Estampado, Empaque
            
            FACADE:
            - OrderFacade simplifica el checkout
            """;
        
        JTextArea textArea = new JTextArea(info);
        textArea.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Patrones", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void iniciarAplicacionAdmin() {
        Admin admin = Admin.builder()
            .id(1L)
            .nombre("Admin")
            .email("admin@llama.com")
            .area("SUPERVISOR")
            .build();
        
        AdminSession.getInstance().iniciarSesion(admin);
        
        SwingUtilities.invokeLater(() -> {
            AdminFrame frame = new AdminFrame();
            frame.setVisible(true);
        });
    }
}
