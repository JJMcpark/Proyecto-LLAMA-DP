package com.dpatrones.proyecto.swing;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.dpatrones.proyecto.ProyectoApplication;
import com.dpatrones.proyecto.patterns.singleton.AdminSession;

/**
 * Lanzador del Panel de Administración LLAMA
 * 
 * 1. Configura el Look & Feel del sistema
 * 2. Arranca Spring Boot (backend)
 * 3. Muestra el diálogo de Login
 * 4. Si el login es exitoso, abre el AdminFrame
 */
public class AdminLauncher {
    
    public static void main(String[] args) {
        // Configurar Look & Feel nativo del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("No se pudo aplicar LookAndFeel: " + e.getMessage());
        }
        
        System.out.println("🦙 Iniciando LLAMA Admin Panel...");
        System.out.println("⏳ Cargando Spring Boot (puede tomar unos segundos)...");

        // Asegurar modo no headless para mostrar Swing
        SpringApplication app = new SpringApplication(ProyectoApplication.class);
        app.setHeadless(false);
        ConfigurableApplicationContext context = app.run(args);
        
        System.out.println("✅ Spring Boot iniciado correctamente");
        
        // Mostrar login en el hilo de Swing
        SwingUtilities.invokeLater(() -> {
            // Mostrar diálogo de login
            boolean loginOk = LoginDialog.mostrarLogin(null);
            
            if (loginOk && AdminSession.getInstance().isSesionActiva()) {
                System.out.println("🔓 Login exitoso: " + AdminSession.getInstance().getNombreAdmin());
                System.out.println("📊 Abriendo panel de administración...");
                
                AdminFrame frame = new AdminFrame(context);
                frame.setVisible(true);
            } else {
                System.out.println("❌ Login cancelado o fallido. Cerrando aplicación...");
                context.close();
                System.exit(0);
            }
        });
    }
}