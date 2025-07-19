package com.ejemplo.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Clase para manejar la configuración de la aplicación.
 * Carga las propiedades desde config.properties
 */
public class AppConfig {
    private static final Properties properties = new Properties();
    private static final String CONFIG_FILE = "config.properties";
    
    static {
        loadConfig();
    }
    
    /**
     * Carga la configuración desde el archivo properties
     */
    private static void loadConfig() {
        try (InputStream input = AppConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new RuntimeException("No se pudo encontrar " + CONFIG_FILE);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar la configuración", e);
        }
    }
    
    /**
     * Obtiene una propiedad como String
     */
    public static String getString(String key) {
        return properties.getProperty(key);
    }
    
    /**
     * Obtiene una propiedad como int
     */
    public static int getInt(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }
    
    /**
     * Obtiene una propiedad como boolean
     */
    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(properties.getProperty(key));
    }
    
    // Métodos específicos para cada configuración
    public static String getDatabaseUrl() {
        return getString("database.url");
    }
    
    public static String getDatabaseUser() {
        return getString("database.user");
    }
    
    public static String getDatabasePassword() {
        return getString("database.password");
    }
    
    public static int getNumBots() {
        return getInt("bot.num_instances");
    }
    
    public static int getBotTimeout() {
        return getInt("bot.timeout.seconds");
    }
    
    public static boolean isHeadless() {
        return getBoolean("bot.headless");
    }
    
    public static String getFacebookRegistrationUrl() {
        return getString("facebook.registration.url");
    }
    
    public static int getFacebookWaitTimeout() {
        return getInt("facebook.wait.timeout");
    }
    
    public static boolean isEncryptPasswords() {
        return getBoolean("security.encrypt.passwords");
    }
    
    public static int getMaxRetries() {
        return getInt("security.max.retries");
    }
} 