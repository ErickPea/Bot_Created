package com.ejemplo.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.*;

/**
 * Servicio centralizado para el logging de la aplicación.
 * Proporciona métodos para registrar diferentes tipos de eventos.
 */
public class LoggerService {
    private static final Logger logger = Logger.getLogger(LoggerService.class.getName());
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    static {
        setupLogger();
    }
    
    /**
     * Configura el logger con formato personalizado y archivo de salida
     */
    private static void setupLogger() {
        try {
            // Crear directorio de logs si no existe
            java.nio.file.Files.createDirectories(java.nio.file.Paths.get("logs"));
            
            // Configurar handler para archivo
            FileHandler fileHandler = new FileHandler("logs/bot.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            
            // Configurar handler para consola
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new SimpleFormatter());
            
            // Configurar nivel de logging
            logger.setLevel(Level.INFO);
            logger.addHandler(fileHandler);
            logger.addHandler(consoleHandler);
            
        } catch (Exception e) {
            System.err.println("Error configurando logger: " + e.getMessage());
        }
    }
    
    /**
     * Registra un mensaje informativo
     */
    public static void info(String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.info("[" + timestamp + "] INFO: " + message);
    }
    
    /**
     * Registra un mensaje de advertencia
     */
    public static void warning(String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.warning("[" + timestamp + "] WARNING: " + message);
    }
    
    /**
     * Registra un mensaje de error
     */
    public static void error(String message, Throwable throwable) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.log(Level.SEVERE, "[" + timestamp + "] ERROR: " + message, throwable);
    }
    
    /**
     * Registra un mensaje de error sin excepción
     */
    public static void error(String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.severe("[" + timestamp + "] ERROR: " + message);
    }
    
    /**
     * Registra información de debug
     */
    public static void debug(String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.fine("[" + timestamp + "] DEBUG: " + message);
    }
    
    /**
     * Registra el inicio de una operación
     */
    public static void startOperation(String operation) {
        info("🚀 Iniciando operación: " + operation);
    }
    
    /**
     * Registra el fin de una operación
     */
    public static void endOperation(String operation) {
        info("✅ Operación completada: " + operation);
    }
    
    /**
     * Registra la creación de un perfil
     */
    public static void profileCreated(String email, String platform) {
        info("👤 Perfil creado - Email: " + email + " | Plataforma: " + platform);
    }
    
    /**
     * Registra un error en la creación de perfil
     */
    public static void profileCreationFailed(String platform, String reason) {
        error("❌ Error creando perfil en " + platform + ": " + reason);
    }
    
    /**
     * Registra información de base de datos
     */
    public static void databaseOperation(String operation, String details) {
        info("🗄️ DB " + operation + ": " + details);
    }
} 