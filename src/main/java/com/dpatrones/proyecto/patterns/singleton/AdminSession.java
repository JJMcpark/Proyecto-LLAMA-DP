package com.dpatrones.proyecto.patterns.singleton;

import com.dpatrones.proyecto.model.Admin;

/**
 * PATRÓN SINGLETON - Sesión del Administrador
 * 
 * Mantiene los datos del admin logueado durante toda la sesión de la app Swing.
 * Solo puede haber UN administrador logueado a la vez.
 * 
 * Implementación thread-safe usando el patrón Holder (Bill Pugh Singleton).
 */
public class AdminSession {
    
    private Admin adminLogueado;
    private boolean sesionActiva;
    
    private AdminSession() {
        this.sesionActiva = false;
    }
    
    /**
     * Holder interno - se carga solo cuando se llama getInstance()
     * Es thread-safe sin necesidad de synchronized
     */
    private static class Holder {
        private static final AdminSession INSTANCE = new AdminSession();
    }
    
    public static AdminSession getInstance() {
        return Holder.INSTANCE;
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
    
    /**
     * Obtiene el área del administrador logueado
     */
    public String getAreaAdmin() {
        return adminLogueado != null ? adminLogueado.getArea() : "N/A";
    }
    
    /**
     * Verifica si el admin tiene permisos para un área específica
     */
    public boolean tienePermisoArea(String area) {
        if (!sesionActiva || adminLogueado == null) {
            return false;
        }
        // SUPERVISOR tiene acceso a todo
        if ("SUPERVISOR".equalsIgnoreCase(adminLogueado.getArea())) {
            return true;
        }
        return area.equalsIgnoreCase(adminLogueado.getArea());
    }
    
    /**
     * Valida que hay una sesión activa, lanza excepción si no
     */
    public void validarSesion() {
        if (!sesionActiva || adminLogueado == null) {
            throw new IllegalStateException("No hay sesión de administrador activa");
        }
    }
}
