// FacebookBotService.java
package com.ejemplo.services; // Define el paquete del servicio.

import com.ejemplo.models.Perfil; // Objeto para almacenar datos del perfil.
import com.ejemplo.config.AppConfig; // Configuración de la aplicación.
import com.ejemplo.utils.SecurityUtils; // Utilidades de seguridad.
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
        LoggerService.startOperation("Creación de cuenta Facebook");
        
        try (Playwright playwright = Playwright.create()) {
            // Configura y lanza el navegador Chromium usando configuración.
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(AppConfig.isHeadless()));
            BrowserContext context = browser.newContext(); // Sesión de navegador aislada.
            Page page = context.newPage(); // Nueva pestaña.

            // Genera datos de usuario aleatorios y seguros.
            Faker faker = new Faker();
            String nombre = faker.name().firstName();
            String apellido = faker.name().lastName();
            String email = nombre.toLowerCase() + "." + apellido.toLowerCase() + "@gmail.com";
            String password = SecurityUtils.generateSecurePassword();
            
            LoggerService.info("Generando perfil para: " + email);

            // Navega al formulario de registro de Facebook usando configuración.
            page.navigate(AppConfig.getFacebookRegistrationUrl());
            LoggerService.info("Navegando a formulario de registro");

            // Rellena los campos del formulario.
            page.fill("input[name='firstname']", nombre);
            page.fill("input[name='lastname']", apellido);
            page.fill("input[name='reg_email__']", email);

            // Simula Tab y espera, usando timeout configurado.
            page.keyboard().press("Tab");
            page.waitForTimeout(AppConfig.getFacebookWaitTimeout());
            LoggerService.info("Formulario llenado correctamente");

            // Maneja el campo de confirmación de email, que puede aparecer condicionalmente.
            if (page.isVisible("input[name='reg_email_confirmation__']")) {
                page.fill("input[name='reg_email_confirmation__']", email);
                LoggerService.info("Campo de confirmación llenado");
            } else {
                LoggerService.info("No se mostró el campo de confirmación");
            }

            page.fill("input[name='reg_passwd__']", password);

            // URLs de ejemplo para futuras funcionalidades de perfil/portada.
            String fotoPerfil = "https://example.com/foto-perfil.jpg";
            String fotoPortada = "https://example.com/foto-portada.jpg";

            // Captura la URL actual (probablemente aún la de registro).
            String profileUrl = page.url();

            // Espera configurada para observación manual o manejo de CAPTCHAs.
            LoggerService.info("Esperando " + AppConfig.getBotTimeout() + " segundos para completar registro");
            page.waitForTimeout(AppConfig.getBotTimeout() * 1000);

            // Cierra recursos de Playwright.
            context.close();
            browser.close();

            // Retorna el objeto Perfil con los datos generados (contraseña encriptada).
            Perfil perfil = new Perfil(nombre, apellido, email, profileUrl, fotoPerfil, fotoPortada, password);
            LoggerService.profileCreated(email, "Facebook");
            LoggerService.endOperation("Creación de cuenta Facebook");
            return perfil;

        } catch (Exception e) {
            LoggerService.error("Error creando cuenta Facebook", e);
            LoggerService.profileCreationFailed("Facebook", e.getMessage());
            return null; // Retorna null si hay una falla.
        }
    }
}