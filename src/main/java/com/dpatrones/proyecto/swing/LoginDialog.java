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

public class LoginDialog extends JDialog {

    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "admin123";

    private final JTextField txtUsuario = new JTextField();
    private final JPasswordField txtPassword = new JPasswordField();
    private final JLabel lblMensaje = new JLabel(" ");
    private boolean loginExitoso = false;

    public LoginDialog(Frame parent) {
        super(parent, "LLAMA - Iniciar Sesión", true);
        setSize(300, 200);
        setResizable(false);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initUI();
    }

    private void initUI() {
        JPanel content = (JPanel) getContentPane();
        content.setLayout(new BorderLayout(10, 10));
        content.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Panel de Administración", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        add(titulo, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(3, 2, 5, 10));
        form.add(new JLabel("Usuario:"));
        form.add(txtUsuario);
        form.add(new JLabel("Contraseña:"));
        form.add(txtPassword);
        lblMensaje.setForeground(Color.RED);
        form.add(lblMensaje);
        form.add(new JLabel("(admin / admin123)"));
        add(form, BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout());
        JButton btnLogin = new JButton("Ingresar");
        btnLogin.addActionListener(e -> login());
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        botones.add(btnLogin);
        botones.add(btnCancelar);
        add(botones, BorderLayout.SOUTH);

        txtPassword.addActionListener(e -> login());
        getRootPane().setDefaultButton(btnLogin);
    }

    private void login() {
        String usuario = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (usuario.isEmpty() || password.isEmpty()) {
            lblMensaje.setText("Complete todos los campos");
            return;
        }

        if (ADMIN_USER.equals(usuario) && ADMIN_PASS.equals(password)) {
            Admin admin = Admin.builder()
                    .id(1L)
                    .nombre("Administrador")
                    .email("admin@llama.com")
                    .area("SUPERVISOR")
                    .build();
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
