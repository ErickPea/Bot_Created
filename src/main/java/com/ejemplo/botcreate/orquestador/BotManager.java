package com.ejemplo.botcreate.orquestador; // Define el paquete del orquestador (componente principal de control).

import com.ejemplo.services.FacebookBotService; // Importa el servicio para crear cuentas de Facebook.
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
    // Define el número de bots (hilos) que se ejecutarán concurrentemente.
    private static final int NUM_BOTS = 1; // Actualmente configurado para un solo bot.

    /**
     * Punto de entrada principal de la aplicación.
     * Inicia el proceso de creación y almacenamiento de perfiles.
     *
     * @param args Argumentos de la línea de comandos (no utilizados en este caso).
     */
    public static void main(String[] args) {
        // Crea un pool de hilos con un número fijo de hilos (NUM_BOTS).
        // Esto permite ejecutar múltiples bots en paralelo si NUM_BOTS > 1.
        ExecutorService executor = Executors.newFixedThreadPool(NUM_BOTS);
        // Instancia el servicio para interactuar con la base de datos.
        DatabaseService dbService = new DatabaseService();

        // Itera para lanzar la cantidad deseada de bots.
        for (int i = 0; i < NUM_BOTS; i++) {
            // Envía una tarea (Runnable) al pool de hilos para su ejecución.
            // Cada tarea representa la ejecución de un bot completo.
            executor.submit(() -> {
                FacebookBotService bot = new FacebookBotService(); // Crea una nueva instancia del bot de Facebook.
                Perfil perfil = bot.crearCuenta(); // Intenta crear una cuenta de Facebook.

                // Verifica si el perfil se creó exitosamente.
                if (perfil != null) {
                    System.out.println("Perfil creado: " + perfil); // Imprime los detalles del perfil creado.

                    // Guarda el perfil recién creado en la base de datos.
                    dbService.guardarPerfil(perfil);
                    System.out.println("Perfil guardado en la base de datos.");
                } else {
                    System.out.println("Error al crear perfil."); // Mensaje si la creación del perfil falla.
                }
            });
        }

        // Cierra el pool de hilos. Esto impide que se acepten nuevas tareas.
        executor.shutdown(); // Inicia el proceso de apagado, pero permite que las tareas en curso terminen.

        // Espera a que todas las tareas en el pool de hilos finalicen.
        // Si las tareas no terminan en 60 segundos, intenta detenerlas forzosamente.
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                // Si el tiempo de espera expira y las tareas no han terminado,
                // fuerza el cierre de todas las tareas activas.
                executor.shutdownNow();
                System.err.println("Advertencia: El pool de hilos no terminó en el tiempo esperado. Se forzó el apagado.");
            }
        } catch (InterruptedException e) {
            // Si el hilo actual es interrumpido mientras espera,
            // fuerza el cierre del pool de hilos y restaura el estado de interrupción.
            executor.shutdownNow();
            Thread.currentThread().interrupt(); // Restablece el estado de interrupción del hilo.
            System.err.println("Error: El proceso fue interrumpido mientras esperaba la finalización del pool de hilos.");
        }
    }
}