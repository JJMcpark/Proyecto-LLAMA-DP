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
 */
public class AdminDatabaseConnection {
    
    // La única instancia (volatile para thread-safety)
    private static volatile AdminDatabaseConnection instance;
    
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
     * Obtiene la única instancia de la conexión (Double-Checked Locking)
     */
    public static AdminDatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (AdminDatabaseConnection.class) {
                if (instance == null) {
                    instance = new AdminDatabaseConnection();
                }
            }
        }
        return instance;
    }
    
    /**
     * Retorna la conexión activa
     */
    public Connection getConnection() {
        try {
            // Si la conexión se cerró, la reabrimos
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            System.err.println("[SINGLETON] Error al reconectar: " + e.getMessage());
        }
        return connection;
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
}
