package com.dpatrones.proyecto.patterns.singleton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AdminDatabaseConnection {
    
    private static final String URL = "jdbc:mysql://localhost:3306/proyecto_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "Sopa123Do234Macaco345";
    
    private Connection connection;
    
    private AdminDatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("[SINGLETON] Conexión a BD establecida.");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("[SINGLETON] Error al conectar: " + e.getMessage());
        }
    }
    
    private static class Holder {
        private static final AdminDatabaseConnection INSTANCE = new AdminDatabaseConnection();
    }
    
    public static AdminDatabaseConnection getInstance() {
        return Holder.INSTANCE;
    }
    
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            System.err.println("[SINGLETON] Error: " + e.getMessage());
        }
        return connection;
    }
    
    public boolean isConexionActiva() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
    
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
