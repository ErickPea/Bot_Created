package com.ejemplo.persistence; // Define el paquete donde se encuentra esta clase.

import java.sql.Connection;    // Clase para establecer y gestionar una conexión a la base de datos.
import java.sql.DriverManager; // Clase que gestiona un conjunto de drivers JDBC.
import java.sql.SQLException;  // Excepción específica para errores de base de datos.

/**
 * Clase utilitaria para gestionar la conexión a la base de datos.
 * Proporciona un método estático para obtener una conexión re utilizable.
 */
public class DatabaseConnection {

    // --- Configuración de la Conexión a la Base de Datos ---
    // Las configuraciones se cargan desde config.properties
    private static final String URL = com.ejemplo.config.AppConfig.getDatabaseUrl();
    private static final String USER = com.ejemplo.config.AppConfig.getDatabaseUser();
    private static final String PASSWORD = com.ejemplo.config.AppConfig.getDatabasePassword();

    /**
     * Establece y retorna una conexión a la base de datos.
     *
     * @return Objeto Connection si la conexión es exitosa, o null si ocurre un error.
     */
    public static Connection getConnection() {
        try {
            // Intenta establecer la conexión usando la URL, usuario y contraseña definidos.
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            // Captura y maneja cualquier error de SQL durante el intento de conexión.
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
            return null; // Retorna null para indicar que la conexión falló.
        }
    }
}