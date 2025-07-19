package com.ejemplo.botcreate; // Define el paquete principal de la aplicación.

import com.ejemplo.botcreate.orquestador.BotManager; // Importa la clase BotManager, que orquesta los bots.
import com.ejemplo.services.LoggerService; // Importa el servicio de logging.


/**
 * Clase principal que sirve como el punto de entrada de la aplicación.
 * Su única responsabilidad es iniciar el gestor de bots.
 */
public class Main {
    /**
     * El método 'main' es el punto de inicio de la ejecución de cualquier aplicación Java.
     *@param args Argumentos de la línea de comandos (no se usan directamente aquí, pero se pasan al BotManager).
     */
    public static void main(String[] args) {
        LoggerService.startOperation("Bot de Registro Masivo");
        LoggerService.info("🚀 Iniciando Bot de Registro Masivo...");
        LoggerService.info("========================================");
        
        LoggerService.info("🤖 Iniciando gestor de bots...");
        // Llama al método 'main' de la clase BotManager.
        // Esto delega la lógica de inicio y gestión de los bots al BotManager.
        BotManager.main(args);
        
        LoggerService.endOperation("Bot de Registro Masivo");
    }
}