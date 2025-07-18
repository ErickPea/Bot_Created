// FacebookBotService.java
package com.ejemplo.services; // Define el paquete del servicio.

import com.ejemplo.models.Perfil; // Objeto para almacenar datos del perfil.
import com.microsoft.playwright.*; // Framework de automatización web.
import com.github.javafaker.Faker; // Generador de datos de prueba.

/**
 * Servicio para automatizar la creación de cuentas en Facebook
 * usando Playwright para interacción web y Faker para datos.
 */
public class FacebookBotService {

    /**
     * Automatiza la creación de una cuenta de Facebook.
     * Genera datos aleatorios y los ingresa en el formulario de registro.
     *
     * @return Perfil con datos de la cuenta creada, o null si falla.
     */
    public Perfil crearCuenta() {
        try (Playwright playwright = Playwright.create()) {
            // Configura y lanza el navegador Chromium (visible para depuración).
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            BrowserContext context = browser.newContext(); // Sesión de navegador aislada.
            Page page = context.newPage(); // Nueva pestaña.

            // Genera datos de usuario aleatorios.
            Faker faker = new Faker();
            String nombre = faker.name().firstName();
            String apellido = faker.name().lastName();
            String email = nombre.toLowerCase() + "." + apellido.toLowerCase() + "@gmail.com";
            String password = faker.internet().password(8, 12);

            // Navega al formulario de registro de Facebook.
            page.navigate("https://www.facebook.com/reg/");

            // Rellena los campos del formulario.
            page.fill("input[name='firstname']", nombre);
            page.fill("input[name='lastname']", apellido);
            page.fill("input[name='reg_email__']", email);

            // Simula Tab y espera, a veces necesario para activar lógica de la página.
            page.keyboard().press("Tab");
            page.waitForTimeout(2000);

            // Maneja el campo de confirmación de email, que puede aparecer condicionalmente.
            if (page.isVisible("input[name='reg_email_confirmation__']")) {
                page.fill("input[name='reg_email_confirmation__']", email);
                System.out.println("Campo de confirmación llenado.");
            } else {
                System.out.println("No se mostró el campo de confirmación.");
            }

            page.fill("input[name='reg_passwd__']", password);

            // URLs de ejemplo para futuras funcionalidades de perfil/portada.
            String fotoPerfil = "https://example.com/foto-perfil.jpg";
            String fotoPortada = "https://example.com/foto-portada.jpg";

            // Captura la URL actual (probablemente aún la de registro).
            String profileUrl = page.url();

            // Espera extensa para observación manual o manejo de CAPTCHAs.
            // ¡ADVERTENCIA! En producción, reemplazar con esperas explícitas (ej. waitForSelector).
            page.waitForTimeout(70000);

            // Cierra recursos de Playwright.
            context.close();
            browser.close();

            // Retorna el objeto Perfil con los datos generados.
            return new Perfil(nombre, apellido, email, profileUrl, fotoPerfil, fotoPortada, password);

        } catch (Exception e) {
            e.printStackTrace(); // Imprime el error.
            return null; // Retorna null si hay una falla.
        }
    }
}