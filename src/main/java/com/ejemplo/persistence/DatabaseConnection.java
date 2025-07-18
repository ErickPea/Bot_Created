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
    // URL de conexión JDBC para MySQL.
    // Incluye el host (localhost), puerto (3306), nombre de la DB (proyecto_bots).
    // 'useSSL=false' para deshabilitar SSL (¡cambiar a true en producción!).
    // 'serverTimezone=UTC' para configurar la zona horaria del servidor.
    private static final String URL = "jdbc:mysql://localhost:3306/proyecto_bots?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";  // ¡IMPORTANTE! Cambia esto por tu usuario de la base de datos.
    private static final String PASSWORD = "";  // ¡IMPORTANTE! Cambia esto por tu contraseña de la base de datos.

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