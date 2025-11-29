package com.dpatrones.proyecto.patterns.singleton;

import com.dpatrones.proyecto.model.Admin;

/**
 * PATRÓN SINGLETON - Sesión del Administrador
 * 
 * Mantiene los datos del admin logueado durante toda la sesión de la app Swing.
 * Solo puede haber UN administrador logueado a la vez.
 */
public class AdminSession {
    
    private static volatile AdminSession instance;
    
    private Admin adminLogueado;
    private boolean sesionActiva;
    
    private AdminSession() {
        this.sesionActiva = false;
    }
    
    public static AdminSession getInstance() {
        if (instance == null) {
            synchronized (AdminSession.class) {
                if (instance == null) {
                    instance = new AdminSession();
                }
            }
        }
        return instance;
    }
    
    /**
     * Inicia sesión del administrador
     */
    public void iniciarSesion(Admin admin) {
        this.adminLogueado = admin;
        this.sesionActiva = true;
        System.out.println("[SESIÓN] Admin logueado: " + admin.getNombre() + " (" + admin.getArea() + ")");
    }
    
    /**
     * Cierra la sesión actual
     */
    public void cerrarSesion() {
        if (adminLogueado != null) {
            System.out.println("[SESIÓN] Cerrando sesión de: " + adminLogueado.getNombre());
        }
        this.adminLogueado = null;
        this.sesionActiva = false;
    }
    
    public Admin getAdminLogueado() {
        return adminLogueado;
    }
    
    public boolean isSesionActiva() {
        return sesionActiva;
    }
    
    public String getNombreAdmin() {
        return adminLogueado != null ? adminLogueado.getNombre() : "Sin sesión";
    }
}
