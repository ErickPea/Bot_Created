package com.ejemplo.botcreate.orquestador; // Define el paquete del orquestador (componente principal de control).

import com.ejemplo.services.FacebookBotService; // Importa el servicio para crear cuentas de Facebook.
import com.ejemplo.services.LoggerService;     // Importa el servicio de logging.
import com.ejemplo.config.AppConfig;           // Importa la configuración de la aplicación.
import com.ejemplo.models.Perfil;             // Importa el modelo de datos Perfil.
import com.ejemplo.services.DatabaseService; // Importa el servicio para interactuar con la base de datos.

import java.util.concurrent.ExecutorService; // Para gestionar hilos y ejecutar tareas concurrentemente.
import java.util.concurrent.Executors;     // Fábrica para crear diferentes tipos de ExecutorService.
import java.util.concurrent.TimeUnit;      // Para especificar unidades de tiempo para esperas.

/**
 * Clase principal que orquesta la creación automatizada de perfiles.
 * Utiliza un pool de hilos para ejecutar bots de creación de cuentas
 * y luego guarda los perfiles generados en la base de datos.
 */
public class BotManager {
    // El número de bots se configura desde AppConfig

    /**
     * Punto de entrada principal de la aplicación.
     * Inicia el proceso de creación y almacenamiento de perfiles.
     *
     * @param args Argumentos de la línea de comandos (no utilizados en este caso).
     */
    public static void main(String[] args) {
        // Obtiene la configuración de bots desde AppConfig
        int numBots = AppConfig.getNumBots();
        LoggerService.info("Iniciando " + numBots + " bot(s) de Facebook");
        
        // Crea un pool de hilos con el número configurado de bots.
        // Esto permite ejecutar múltiples bots en paralelo si numBots > 1.
        ExecutorService executor = Executors.newFixedThreadPool(numBots);
        // Instancia el servicio para interactuar con la base de datos.
        DatabaseService dbService = new DatabaseService();

        // Itera para lanzar la cantidad deseada de bots.
        for (int i = 0; i < numBots; i++) {
            // Envía una tarea (Runnable) al pool de hilos para su ejecución.
            // Cada tarea representa la ejecución de un bot completo.
            executor.submit(() -> {
                FacebookBotService bot = new FacebookBotService(); // Crea una nueva instancia del bot de Facebook.
                Perfil perfil = bot.crearCuenta(); // Intenta crear una cuenta de Facebook.

                // Verifica si el perfil se creó exitosamente.
                if (perfil != null) {
                    LoggerService.info("Perfil creado exitosamente: " + perfil.getEmail());

                    // Guarda el perfil recién creado en la base de datos.
                    try {
                        dbService.guardarPerfil(perfil);
                        LoggerService.databaseOperation("INSERT", "Perfil guardado: " + perfil.getEmail());
                    } catch (Exception e) {
                        LoggerService.error("Error al guardar perfil en base de datos: " + perfil.getEmail(), e);
                    }
                } else {
                    LoggerService.error("Error al crear perfil");
                }
            });
        }

        // Cierra el pool de hilos. Esto impide que se acepten nuevas tareas.
        executor.shutdown(); // Inicia el proceso de apagado, pero permite que las tareas en curso terminen.

        // Espera a que todas las tareas en el pool de hilos finalicen.
        // Usa el timeout configurado en AppConfig.
        try {
            int timeout = AppConfig.getBotTimeout();
            LoggerService.info("Esperando " + timeout + " segundos para que terminen los bots");
            
            if (!executor.awaitTermination(timeout, TimeUnit.SECONDS)) {
                // Si el tiempo de espera expira y las tareas no han terminado,
                // fuerza el cierre de todas las tareas activas.
                executor.shutdownNow();
                LoggerService.warning("El pool de hilos no terminó en el tiempo esperado. Se forzó el apagado.");
            } else {
                LoggerService.info("Todos los bots han terminado correctamente");
            }
        } catch (InterruptedException e) {
            // Si el hilo actual es interrumpido mientras espera,
            // fuerza el cierre del pool de hilos y restaura el estado de interrupción.
            executor.shutdownNow();
            Thread.currentThread().interrupt(); // Restablece el estado de interrupción del hilo.
            LoggerService.error("El proceso fue interrumpido mientras esperaba la finalización del pool de hilos", e);
        }
    }
}