package com.dpatrones.proyecto.swing;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.dpatrones.proyecto.ProyectoApplication;
import com.dpatrones.proyecto.patterns.singleton.AdminSession;

public class AdminLauncher {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        System.out.println("🦙 Iniciando LLAMA Admin...");

        SpringApplication app = new SpringApplication(ProyectoApplication.class);
        app.setHeadless(false);

        ConfigurableApplicationContext ctx;
        try {
            ctx = app.run(args);
        } catch (Exception e) {
            System.err.println("Error al iniciar Spring Boot: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error al conectar con la BD:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        System.out.println("✅ Spring Boot iniciado");

        SwingUtilities.invokeLater(() -> {
            boolean loginOk = LoginDialog.mostrarLogin(null);

            if (loginOk && AdminSession.getInstance().isSesionActiva()) {
                System.out.println("✅ Login exitoso: " + AdminSession.getInstance().getNombreAdmin());
                AdminFrame frame = new AdminFrame(ctx);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            } else {
                System.out.println("❌ Login cancelado");
                ctx.close();
                System.exit(0);
            }
        });
    }
}