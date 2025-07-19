-- Script de configuración de la base de datos para el proyecto Bot de Registro Masivo
-- Versión compatible con MariaDB (puerto 3307)
-- Ejecutar este script en MariaDB para crear la base de datos y todas las tablas

-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS proyecto_bots
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- Usar la base de datos
USE proyecto_bots;

-- Crear tabla de estados
CREATE TABLE IF NOT EXISTS estados (
    id INT PRIMARY KEY,
    descripcion VARCHAR(50) NOT NULL
);

-- Insertar estados básicos
INSERT IGNORE INTO estados (id, descripcion) VALUES
(1, 'Activa'),
(2, 'Inactiva'),
(3, 'Pendiente');

-- Crear tabla de cuentas
CREATE TABLE IF NOT EXISTS cuentas (
    id CHAR(36) PRIMARY KEY,
    plataforma VARCHAR(50) NOT NULL,
    username VARCHAR(100),
    email VARCHAR(100) NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    estado_id INT NOT NULL DEFAULT 3,
    enlace_perfil TEXT,
    foto_perfil TEXT,
    foto_portada TEXT,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (estado_id) REFERENCES estados(id)
);

-- Crear tabla de cookies
CREATE TABLE IF NOT EXISTS cookies (
    id CHAR(36) PRIMARY KEY,
    cuenta_id CHAR(36) NOT NULL,
    data JSON,
    fecha_guardado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cuenta_id) REFERENCES cuentas(id) ON DELETE CASCADE
);

-- Crear tabla de comentarios (versión compatible con MariaDB)
CREATE TABLE IF NOT EXISTS comentarios (
    id CHAR(36) PRIMARY KEY,
    tema VARCHAR(255) NOT NULL,
    comentario TEXT NOT NULL,
    estado VARCHAR(20) DEFAULT 'Activo',
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Crear tabla de acciones (versión compatible con MariaDB)
CREATE TABLE IF NOT EXISTS acciones (
    id CHAR(36) PRIMARY KEY,
    cuenta_id CHAR(36) NOT NULL,
    tipo_accion VARCHAR(20) NOT NULL,
    contenido TEXT,
    enlace_objetivo TEXT,
    comentario_id CHAR(36),
    texto_comentario TEXT,
    fecha_accion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cuenta_id) REFERENCES cuentas(id) ON DELETE CASCADE,
    FOREIGN KEY (comentario_id) REFERENCES comentarios(id) ON DELETE SET NULL
);

-- Crear tabla de publicaciones
CREATE TABLE IF NOT EXISTS publicaciones (
    id CHAR(36) PRIMARY KEY,
    cuenta_id CHAR(36) NOT NULL,
    contenido_publicado TEXT,
    enlace_publicacion TEXT,
    fecha_publicacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cuenta_id) REFERENCES cuentas(id) ON DELETE CASCADE
);

-- Crear índices para mejorar el rendimiento
CREATE INDEX IF NOT EXISTS idx_cuentas_email ON cuentas(email);
CREATE INDEX IF NOT EXISTS idx_cuentas_plataforma ON cuentas(plataforma);
CREATE INDEX IF NOT EXISTS idx_cuentas_estado ON cuentas(estado_id);
CREATE INDEX IF NOT EXISTS idx_cookies_cuenta ON cookies(cuenta_id);
CREATE INDEX IF NOT EXISTS idx_acciones_cuenta ON acciones(cuenta_id);
CREATE INDEX IF NOT EXISTS idx_acciones_fecha ON acciones(fecha_accion);
CREATE INDEX IF NOT EXISTS idx_publicaciones_cuenta ON publicaciones(cuenta_id);

-- Agregar triggers para validar datos (alternativa a CHECK constraints)
DELIMITER //

-- Trigger para validar estado en comentarios
CREATE TRIGGER IF NOT EXISTS validar_estado_comentarios
BEFORE INSERT ON comentarios
FOR EACH ROW
BEGIN
    IF NEW.estado NOT IN ('Activo', 'Inactivo') THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'El estado debe ser Activo o Inactivo';
    END IF;
END//

-- Trigger para validar tipo_accion en acciones
CREATE TRIGGER IF NOT EXISTS validar_tipo_accion
BEFORE INSERT ON acciones
FOR EACH ROW
BEGIN
    IF NEW.tipo_accion NOT IN ('Reaccion', 'Comentario', 'Compartir') THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'El tipo de acción debe ser Reaccion, Comentario o Compartir';
    END IF;
END//

DELIMITER ;

-- Mostrar mensaje de confirmación
SELECT 'Base de datos proyecto_bots creada exitosamente!' AS mensaje;
SELECT 'Tablas creadas: estados, cuentas, cookies, comentarios, acciones, publicaciones' AS tablas_creadas;
SELECT 'Triggers creados para validación de datos' AS triggers_creados; 