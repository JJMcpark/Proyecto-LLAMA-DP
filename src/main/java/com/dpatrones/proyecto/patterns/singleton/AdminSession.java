package com.dpatrones.proyecto.patterns.singleton;

import com.dpatrones.proyecto.model.Admin;

public class AdminSession {
    
    private Admin adminLogueado;
    private boolean sesionActiva;
    
    private AdminSession() {
        this.sesionActiva = false;
    }
    
    private static class Holder {
        private static final AdminSession INSTANCE = new AdminSession();
    }
    
    public static AdminSession getInstance() {
        return Holder.INSTANCE;
    }
    
    public void iniciarSesion(Admin admin) {
        this.adminLogueado = admin;
        this.sesionActiva = true;
        System.out.println("[SESIÓN] Admin logueado: " + admin.getNombre() + " (" + admin.getArea() + ")");
    }
    
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
    
    public String getAreaAdmin() {
        return adminLogueado != null ? adminLogueado.getArea() : "N/A";
    }
    
    public boolean tienePermisoArea(String area) {
        if (!sesionActiva || adminLogueado == null) {
            return false;
        }
        if ("SUPERVISOR".equalsIgnoreCase(adminLogueado.getArea())) {
            return true;
        }
        return area.equalsIgnoreCase(adminLogueado.getArea());
    }
    
    public void validarSesion() {
        if (!sesionActiva || adminLogueado == null) {
            throw new IllegalStateException("No hay sesión de administrador activa");
        }
    }
}
