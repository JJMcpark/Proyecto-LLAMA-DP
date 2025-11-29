package com.dpatrones.proyecto.swing;

import com.dpatrones.proyecto.ProyectoApplication;
import com.dpatrones.proyecto.model.Admin;
import com.dpatrones.proyecto.patterns.singleton.AdminSession;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;

/**
 * Launcher para iniciar la aplicación de administración Swing
 * 
 * Ejecutar esta clase para abrir el panel de administración.
 */
public class AdminLauncher {
    
    public static void main(String[] args) {
        // Configurar look and feel nativo del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        System.out.println("🚀 Iniciando LLAMA Admin Panel...");
        System.out.println("⏳ Cargando Spring Boot (puede tomar unos segundos)...");
        
        // Iniciar Spring Boot en segundo plano
        ConfigurableApplicationContext context = SpringApplication.run(ProyectoApplication.class, args);
        
        // Crear un admin de prueba para la sesión
        Admin adminPrueba = new Admin();
        adminPrueba.setId(1L);
        adminPrueba.setNombre("Admin Test");
        adminPrueba.setEmail("admin@llama.com");
        adminPrueba.setArea("SUPERVISOR");
        
        // Iniciar sesión
        AdminSession.getInstance().iniciarSesion(adminPrueba);
        
        // Abrir el JFrame en el hilo de Swing
        SwingUtilities.invokeLater(() -> {
            System.out.println("Abriendo ventana de administración...");
            AdminFrame frame = new AdminFrame();
            frame.setVisible(true);
        });
    }
}