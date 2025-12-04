package com.dpatrones.proyecto.swing;

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
        ConfigurableApplicationContext ctx = app.run(args);

        SwingUtilities.invokeLater(() -> {
            if (LoginDialog.mostrarLogin(null) && AdminSession.getInstance().isSesionActiva()) {
                new AdminFrame(ctx).setVisible(true);
            } else {
                ctx.close();
                System.exit(0);
            }
        });
    }
}