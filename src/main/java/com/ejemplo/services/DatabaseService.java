// DatabaseService.java
package com.ejemplo.services; // Define el paquete del servicio.

import com.ejemplo.models.Perfil; // Importa la clase Perfil, que representa los datos de un usuario.
import com.ejemplo.persistence.DatabaseConnection; // Importa la clase para gestionar la conexión a la base de datos.

import java.sql.Connection; // Clase para establecer una conexión con la DB.
import java.sql.PreparedStatement; // Clase para ejecutar consultas SQL pre-compiladas.
import java.sql.SQLException; // Excepción para manejar errores de SQL.
import java.util.UUID; // Clase para generar identificadores únicos universales.

/**
 * Servicio encargado de interactuar con la base de datos para operaciones
 * relacionadas con la gestión de perfiles.
 */
public class DatabaseService {

    /**
     * Guarda la información de un perfil en la tabla 'cuentas' de la base de datos.
     * Utiliza un PreparedStatement para prevenir inyecciones SQL y asegurar la integridad de los datos.
     *
     * @param perfil El objeto Perfil que contiene los datos a guardar.
     */
    public void guardarPerfil(Perfil perfil) {
        // Define la consulta SQL INSERT para insertar un nuevo registro en la tabla 'cuentas'.
        // Los '?' son placeholders para los valores que se añadirán de forma segura.
        // NOW() se usa para que la base de datos ponga la fecha y hora actual de creación.
        String sql = "INSERT INTO cuentas (id, plataforma, username, email, estado_id, enlace_perfil, fecha_creacion, foto_perfil, foto_portada, contrasena, nombre, apellidos) "
                   + "VALUES (?, ?, ?, ?, ?, ?, NOW(), ?, ?, ?, ?, ?)";

        // Usa un bloque try-with-resources para asegurar que Connection y PreparedStatement se cierren automáticamente.
        try (Connection conn = DatabaseConnection.getConnection(); // Obtiene una conexión a la base de datos.
             PreparedStatement stmt = conn.prepareStatement(sql)) { // Prepara la consulta SQL.

            // Asigna los valores del objeto Perfil a cada placeholder en la consulta SQL.
            // Se usa setString, setInt, etc., según el tipo de dato.
            stmt.setString(1, UUID.randomUUID().toString()); // Genera un ID único para el registro.
            stmt.setString(2, "Facebook"); // Define la plataforma (ej. "Facebook").
            stmt.setString(3, perfil.getNombre() + " " + perfil.getApellido()); // Concatena nombre y apellido para el username.
            stmt.setString(4, perfil.getEmail());
            stmt.setInt(5, 1); // Establece el estado_id (ej. 1 podría significar "activo" o "creado").
            stmt.setString(6, perfil.getUrlPerfil());
            stmt.setString(7, perfil.getFotoPerfil());
            stmt.setString(8, perfil.getFotoPortada());
            stmt.setString(9, perfil.getContrasenaHash());
            stmt.setString(10, perfil.getNombre());
            stmt.setString(11, perfil.getApellido());

            // Ejecuta la consulta de actualización (INSERT, UPDATE, DELETE).
            stmt.executeUpdate();
            LoggerService.databaseOperation("INSERT", "Perfil guardado: " + perfil.getEmail());

        } catch (SQLException e) {
            // Captura y registra cualquier excepción SQL que ocurra durante la operación de la base de datos.
            LoggerService.error("Error al guardar perfil en base de datos: " + perfil.getEmail(), e);
            throw new RuntimeException("Error al guardar perfil", e);
        }
    }
}