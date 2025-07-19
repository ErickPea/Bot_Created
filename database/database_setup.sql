-- Script de configuración de la base de datos para el proyecto Bot de Registro Masivo
-- Ejecutar este script en MySQL (puerto 3307) para crear la base de datos y todas las tablas

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

-- Crear tabla de comentarios
CREATE TABLE IF NOT EXISTS comentarios (
    id CHAR(36) PRIMARY KEY,
    tema VARCHAR(255) NOT NULL,
    comentario TEXT NOT NULL,
    estado VARCHAR(20) CHECK (estado IN ('Activo', 'Inactivo')) DEFAULT 'Activo',
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Crear tabla de acciones
CREATE TABLE IF NOT EXISTS acciones (
    id CHAR(36) PRIMARY KEY,
    cuenta_id CHAR(36) NOT NULL,
    tipo_accion VARCHAR(20) CHECK (tipo_accion IN ('Reaccion', 'Comentario', 'Compartir')) NOT NULL,
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
CREATE INDEX idx_cuentas_email ON cuentas(email);
CREATE INDEX idx_cuentas_plataforma ON cuentas(plataforma);
CREATE INDEX idx_cuentas_estado ON cuentas(estado_id);
CREATE INDEX idx_cookies_cuenta ON cookies(cuenta_id);
CREATE INDEX idx_acciones_cuenta ON acciones(cuenta_id);
CREATE INDEX idx_acciones_fecha ON acciones(fecha_accion);
CREATE INDEX idx_publicaciones_cuenta ON publicaciones(cuenta_id);

-- Mostrar mensaje de confirmación
SELECT 'Base de datos proyecto_bots creada exitosamente!' AS mensaje;
SELECT 'Tablas creadas: estados, cuentas, cookies, comentarios, acciones, publicaciones' AS tablas_creadas; 