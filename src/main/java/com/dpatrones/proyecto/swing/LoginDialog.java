package com.dpatrones.proyecto.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import com.dpatrones.proyecto.model.Admin;
import com.dpatrones.proyecto.patterns.singleton.AdminSession;

/**
 * Diálogo de Login para el Panel de Administración
 * 
 * Credenciales por defecto:
 * - Usuario: admin
 * - Contraseña: admin123
 * 
 * Demuestra el uso del patrón SINGLETON (AdminSession) para mantener la sesión.
 */
public class LoginDialog extends JDialog {
    
    // Credenciales base del administrador
    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "admin123";
    
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnCancelar;
    private JLabel lblMensaje;
    
    private boolean loginExitoso = false;
    
    public LoginDialog(Frame parent) {
        super(parent, "🦙 LLAMA - Iniciar Sesión", true);
        initComponents();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setSize(400, 320);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        // Panel principal con gradiente
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(63, 81, 181), 0, getHeight(), new Color(33, 150, 243));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel lblTitulo = new JLabel("🦙 LLAMA Admin", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        
        JLabel lblSubtitulo = new JLabel("Sistema de Administración", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitulo.setForeground(new Color(200, 200, 255));
        
        headerPanel.add(lblTitulo, BorderLayout.CENTER);
        headerPanel.add(lblSubtitulo, BorderLayout.SOUTH);
        
        // Panel del formulario
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(20, 0, 10, 0));
        
        // Usuario
        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setForeground(Color.WHITE);
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtUsuario = new JTextField(20);
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsuario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        // Password
        JLabel lblPassword = new JLabel("Contraseña");
        lblPassword.setForeground(Color.WHITE);
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        // Mensaje de error/éxito
        lblMensaje = new JLabel(" ");
        lblMensaje.setForeground(new Color(255, 200, 200));
        lblMensaje.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblMensaje.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        formPanel.add(lblUsuario);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(txtUsuario);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(lblPassword);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(txtPassword);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(lblMensaje);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);
        
        btnLogin = crearBoton("Ingresar", new Color(76, 175, 80));
        btnCancelar = crearBoton("Cancelar", new Color(244, 67, 54));
        
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnCancelar);
        
        // Hint de credenciales
        JLabel lblHint = new JLabel("Credenciales: admin / admin123", SwingConstants.CENTER);
        lblHint.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        lblHint.setForeground(new Color(180, 180, 220));
        
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        footerPanel.add(buttonPanel, BorderLayout.CENTER);
        footerPanel.add(lblHint, BorderLayout.SOUTH);
        
        // Agregar todo al panel principal
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
        
        // Eventos
        btnLogin.addActionListener(e -> intentarLogin());
        btnCancelar.addActionListener(e -> {
            loginExitoso = false;
            dispose();
        });
        
        // Enter para login
        KeyAdapter enterListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    intentarLogin();
                }
            }
        };
        txtUsuario.addKeyListener(enterListener);
        txtPassword.addKeyListener(enterListener);
        
        // Foco inicial
        SwingUtilities.invokeLater(() -> txtUsuario.requestFocusInWindow());
    }
    
    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(100, 35));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            Color original = color;
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(original.brighter());
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(original);
            }
        });
        
        return btn;
    }
    
    private void intentarLogin() {
        String usuario = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword());
        
        if (usuario.isEmpty() || password.isEmpty()) {
            mostrarError("Por favor ingrese usuario y contraseña");
            return;
        }
        
        // Validar credenciales
        if (ADMIN_USER.equals(usuario) && ADMIN_PASS.equals(password)) {
            // Login exitoso - crear admin y usar Singleton
            Admin admin = new Admin();
            admin.setId(1L);
            admin.setNombre("Administrador LLAMA");
            admin.setEmail("admin@llama.com");
            admin.setArea("SUPERVISOR");
            
            AdminSession.getInstance().iniciarSesion(admin);
            
            loginExitoso = true;
            lblMensaje.setForeground(new Color(200, 255, 200));
            lblMensaje.setText("✓ Acceso concedido. Cargando...");
            
            // Pequeña pausa visual antes de cerrar
            Timer timer = new Timer(500, e -> dispose());
            timer.setRepeats(false);
            timer.start();
        } else {
            mostrarError("Usuario o contraseña incorrectos");
            txtPassword.setText("");
            txtPassword.requestFocusInWindow();
        }
    }
    
    private void mostrarError(String mensaje) {
        lblMensaje.setForeground(new Color(255, 150, 150));
        lblMensaje.setText("✗ " + mensaje);
        
        // Efecto de shake
        Point original = getLocation();
        Timer shake = new Timer(50, null);
        int[] count = {0};
        shake.addActionListener(e -> {
            if (count[0] < 6) {
                int offset = (count[0] % 2 == 0) ? 5 : -5;
                setLocation(original.x + offset, original.y);
                count[0]++;
            } else {
                setLocation(original);
                shake.stop();
            }
        });
        shake.start();
    }
    
    public boolean isLoginExitoso() {
        return loginExitoso;
    }
    
    /**
     * Muestra el diálogo de login y retorna true si el login fue exitoso
     */
    public static boolean mostrarLogin(Frame parent) {
        LoginDialog dialog = new LoginDialog(parent);
        dialog.setVisible(true);
        return dialog.isLoginExitoso();
    }
}
