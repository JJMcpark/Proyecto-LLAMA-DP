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

    private final ApplicationContext ctx;
    private final JLabel lblStatus = new JLabel();

    public AdminFrame(ApplicationContext ctx) {
        this.ctx = ctx;
        setTitle("LLAMA - Panel de Administración");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(950, 650);
        setLocationRelativeTo(null);
        initUI();
        startObserverUpdater();
    }

    private void initUI() {
        setJMenuBar(createMenuBar());
        add(createHeader(), BorderLayout.NORTH);
        add(createTabs(), BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);
    }

    private JMenuBar createMenuBar() {
        JMenuBar bar = new JMenuBar();
        JMenu archivo = new JMenu("Archivo");
        JMenuItem salir = new JMenuItem("Salir");
        salir.addActionListener(e -> salir());
        archivo.add(salir);
        JMenu ayuda = new JMenu("Ayuda");
        JMenuItem patrones = new JMenuItem("Ver Patrones");
        patrones.addActionListener(e -> mostrarPatrones());
        ayuda.add(patrones);
        bar.add(archivo);
        bar.add(ayuda);
        return bar;
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        header.add(new JLabel("Usuario: " + AdminSession.getInstance().getNombreAdmin() + " | Singleton"),
                BorderLayout.WEST);
        JButton btnCerrar = new JButton("Cerrar Sesión");
        btnCerrar.addActionListener(e -> salir());
        header.add(btnCerrar, BorderLayout.EAST);
        return header;
    }

    private JTabbedPane createTabs() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Dashboard", new DashboardPanel(ctx));
        tabs.addTab("Logística", new LogisticaPanel(ctx));
        return tabs;
    }

    private JPanel createStatusBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bar.setBorder(BorderFactory.createLoweredBevelBorder());
        bar.add(lblStatus);
        return bar;
    }

    private void startObserverUpdater() {
        new Timer(500, e -> {
            int obs = VentasSubject.getInstance().getCantidadObservadores();
            lblStatus.setText(
                    "Observadores: " + obs + " | Patrones: Singleton, Observer, State, Factory, Decorator, Facade");
        }).start();
    }

    private void salir() {
        AdminSession.getInstance().cerrarSesion();
        dispose();
        System.exit(0);
    }

    private void mostrarPatrones() {
        String info = """
                PATRONES: Singleton, Observer, State, Factory, Decorator, Facade

                SINGLETON: AdminSession, VentasSubject
                OBSERVER: Dashboard/Logística actualizan en tiempo real
                STATE: PENDIENTE → PAGADO → ENVIADO → ENTREGADO
                FACTORY: PaymentFactory crea procesadores de pago
                DECORATOR: Extras (Bordado, Estampado, Empaque)
                FACADE: OrderFacade simplifica checkout
                """;
        JTextArea text = new JTextArea(info);
        text.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(text), "Patrones", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void iniciar(ApplicationContext ctx) {
        Admin admin = Admin.builder().id(1L).nombre("Admin").email("admin@llama.com").area("SUPERVISOR").build();
        AdminSession.getInstance().iniciarSesion(admin);
        SwingUtilities.invokeLater(() -> new AdminFrame(ctx).setVisible(true));
    }
}
