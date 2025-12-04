package com.dpatrones.proyecto.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.dpatrones.proyecto.model.Admin;
import com.dpatrones.proyecto.patterns.singleton.AdminSession;

/**
 * Diálogo de Login simple para el Panel de Administración
 * 
 * Credenciales: admin / admin123
 * 
 * Usa el patrón SINGLETON (AdminSession) para mantener la sesión.
 */
public class LoginDialog extends JDialog {
    
    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "admin123";
    
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JLabel lblMensaje;
    private boolean loginExitoso = false;
    
    public LoginDialog(Frame parent) {
        super(parent, "LLAMA - Iniciar Sesión", true);
        setSize(300, 200);
        setResizable(false);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Título
        JLabel lblTitulo = new JLabel("Panel de Administración", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        add(lblTitulo, BorderLayout.NORTH);
        
        // Formulario
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 10));
        
        formPanel.add(new JLabel("Usuario:"));
        txtUsuario = new JTextField();
        formPanel.add(txtUsuario);
        
        formPanel.add(new JLabel("Contraseña:"));
        txtPassword = new JPasswordField();
        formPanel.add(txtPassword);
        
        lblMensaje = new JLabel(" ");
        lblMensaje.setForeground(Color.RED);
        formPanel.add(lblMensaje);
        formPanel.add(new JLabel("(admin / admin123)"));
        
        add(formPanel, BorderLayout.CENTER);
        
        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton btnLogin = new JButton("Ingresar");
        btnLogin.addActionListener(e -> intentarLogin());
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnCancelar);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Enter para login
        txtPassword.addActionListener(e -> intentarLogin());
        getRootPane().setDefaultButton(btnLogin);
    }
    
    private void intentarLogin() {
        String usuario = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword());
        
        if (usuario.isEmpty() || password.isEmpty()) {
            lblMensaje.setText("Complete todos los campos");
            return;
        }
        
        if (ADMIN_USER.equals(usuario) && ADMIN_PASS.equals(password)) {
            // Login exitoso - usar SINGLETON AdminSession
            Admin admin = new Admin();
            admin.setId(1L);
            admin.setNombre("Administrador");
            admin.setEmail("admin@llama.com");
            admin.setArea("SUPERVISOR");
            
            AdminSession.getInstance().iniciarSesion(admin);
            loginExitoso = true;
            dispose();
        } else {
            lblMensaje.setText("Credenciales incorrectas");
            txtPassword.setText("");
        }
    }
    
    public boolean isLoginExitoso() {
        return loginExitoso;
    }
    
    public static boolean mostrarLogin(Frame parent) {
        LoginDialog dialog = new LoginDialog(parent);
        dialog.setVisible(true);
        return dialog.isLoginExitoso();
    }
}
