package com.ejemplo.persistence; // Define el paquete donde se encuentra esta clase.

import com.ejemplo.models.CookieData; // Importa la clase de modelo para los datos de una cookie.
import com.microsoft.playwright.BrowserContext; // Importa BrowserContext de Playwright (aunque no se usa directamente aquí, podría ser para futuras interacciones).

import java.util.ArrayList; // Para implementar una lista dinámica.
import java.util.List;      // Interfaz para colecciones de listas.
import java.util.Map;       // Interfaz para colecciones de tipo clave-valor.

/**
 * Gestiona el almacenamiento y recuperación de cookies.
 * Actualmente, las cookies se guardan en memoria durante la ejecución de la aplicación.
 * Para persistencia real, se necesitaría integrar con una base de datos o un archivo.
 */
public class CookieManager {

    // Almacena las cookies en una lista en memoria.
    // Cada elemento es un objeto CookieData.
    private List<CookieData> cookieStore = new ArrayList<>();

    /**
     * Guarda una lista de cookies extraídas del navegador en el almacenamiento interno.
     *
     * @param cookies Una lista de mapas, donde cada mapa representa una cookie
     * con sus propiedades (ej. "name", "value").
     */
    public void guardarCookies(List<Map<String, Object>> cookies) {
        // Itera sobre cada cookie en la lista de entrada.
        for (Map<String, Object> cookie : cookies) {
            // Extrae el nombre y el valor de la cookie del mapa.
            String name = (String) cookie.get("name");
            String value = (String) cookie.get("value");

            // Crea un nuevo objeto CookieData y lo añade al almacenamiento.
            CookieData data = new CookieData(name, value);
            cookieStore.add(data);
            System.out.println("Cookie guardada: " + data); // Imprime la cookie guardada para verificación.
        }
    }

    /**
     * Carga y retorna todas las cookies que han sido almacenadas.
     *
     * @return Una lista de objetos CookieData que representan las cookies guardadas.
     */
    public List<CookieData> cargarCookies() {
        return cookieStore; // Retorna la lista de cookies almacenadas.
    }
}