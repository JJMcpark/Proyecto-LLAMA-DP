package com.dpatrones.proyecto.swing;

import com.dpatrones.proyecto.ProyectoApplication;
import com.dpatrones.proyecto.model.Admin;
import com.dpatrones.proyecto.patterns.singleton.AdminSession;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;

public class AdminLauncher {
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("No se pudo aplicar LookAndFeel: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
        
        System.out.println(" Iniciando LLAMA Admin Panel...");
        System.out.println(" Cargando Spring Boot (puede tomar unos segundos)...");
        
        ConfigurableApplicationContext context = SpringApplication.run(ProyectoApplication.class, args);
        
        Admin adminPrueba = new Admin();
        adminPrueba.setId(1L);
        adminPrueba.setNombre("Admin Test");
        adminPrueba.setEmail("admin@llama.com");
        adminPrueba.setArea("SUPERVISOR");
        
        AdminSession.getInstance().iniciarSesion(adminPrueba);
        
        SwingUtilities.invokeLater(() -> {
            System.out.println("Abriendo ventana de administración...");
            AdminFrame frame = new AdminFrame(context);
            frame.setVisible(true);
        });
    }
}