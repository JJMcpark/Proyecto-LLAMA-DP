package com.dpatrones.proyecto.patterns.singleton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * PATRÓN SINGLETON - Conexión a Base de Datos para Admin (Swing)
 * 
 * Garantiza que solo exista UNA instancia de conexión a la BD.
 * Esto evita saturar la base de datos con múltiples conexiones.
 * 
 * Uso: AdminDatabaseConnection.getInstance().getConnection()
 * 
 * Implementación thread-safe usando el patrón Holder (Bill Pugh Singleton).
 */
public class AdminDatabaseConnection {
    
    // Configuración de la BD (misma que usa Spring Boot)
    private static final String URL = "jdbc:mysql://localhost:3306/proyecto_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "Sopa123Do234Macaco345";
    
    private Connection connection;
    
    // Constructor PRIVADO - nadie puede crear instancias con "new"
    private AdminDatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("[SINGLETON] Conexión a BD establecida.");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("[SINGLETON] Error al conectar: " + e.getMessage());
        }
    }
    
    /**
     * Holder interno - se carga solo cuando se llama getInstance()
     * Es thread-safe sin necesidad de synchronized
     */
    private static class Holder {
        private static final AdminDatabaseConnection INSTANCE = new AdminDatabaseConnection();
    }
    
    /**
     * Obtiene la única instancia de la conexión
     */
    public static AdminDatabaseConnection getInstance() {
        return Holder.INSTANCE;
    }
    
    /**
     * Retorna la conexión activa
     */
    public Connection getConnection() {
        try {
            // Si la conexión se cerró, la reabrimos
            if (connection == null || connection.isClosed()) {
                System.out.println("[SINGLETON] Reconectando a la BD...");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("[SINGLETON] Reconexión exitosa.");
            }
        } catch (SQLException e) {
            System.err.println("[SINGLETON] Error al reconectar: " + e.getMessage());
        }
        return connection;
    }
    
    /**
     * Verifica si la conexión está activa
     */
    public boolean isConexionActiva() {
        try {
            return connection != null && !connection.isClosed() && connection.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }
    
    /**
     * Cierra la conexión
     */
    public void cerrarConexion() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("[SINGLETON] Conexión cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("[SINGLETON] Error al cerrar: " + e.getMessage());
        }
    }
    
    /**
     * Retorna información de la conexión para debug
     */
    public String getInfoConexion() {
        try {
            if (connection != null && !connection.isClosed()) {
                return "Conectado a: " + connection.getMetaData().getURL();
            }
        } catch (SQLException e) {
            return "Error al obtener info: " + e.getMessage();
        }
        return "No conectado";
    }
}
