package com.ejemplo.models; // Define el paquete donde se encuentra este modelo.

/**
 * Clase de modelo que representa un perfil de usuario.
 * Contiene los atributos básicos de una cuenta, como nombre, email y URL del perfil.
 */
public class Perfil {
    // --- Atributos del Perfil ---
    private String nombre;       // Nombre de pila del usuario.
    private String apellido;     // Apellido del usuario.
    private String email;        // Dirección de correo electrónico asociada al perfil.
    private String urlPerfil;    // URL directa al perfil del usuario.
    private String FotoPerfil;   // URL o ruta a la foto de perfil del usuario.
    private String FotoPortada;  // URL o ruta a la foto de portada del usuario.
    private String contrasenaHash;   // Hash de la contraseña del perfil (seguro).
    private String salt;             // Salt para la encriptación de la contraseña.

    /**
     * Constructor para crear una nueva instancia de Perfil.
     *
     * @param nombre      Nombre de pila del usuario.
     * @param apellido    Apellido del usuario.
     * @param email       Correo electrónico del perfil.
     * @param urlPerfil   URL del perfil del usuario.
     * @param FotoPerfil  URL o ruta de la foto de perfil.
     * @param FotoPortada URL o ruta de la foto de portada.
     * @param contrasena  Contraseña del perfil (será encriptada).
     */
    public Perfil(String nombre, String apellido, String email, String urlPerfil, String FotoPerfil, String FotoPortada, String contrasena) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.urlPerfil = urlPerfil;
        this.FotoPerfil = FotoPerfil;
        this.FotoPortada = FotoPortada;
        
        // Encriptar la contraseña
        this.salt = com.ejemplo.utils.SecurityUtils.generateSalt();
        this.contrasenaHash = com.ejemplo.utils.SecurityUtils.encryptPassword(contrasena, this.salt);
    }

    // --- Métodos Getters ---
    // Proporcionan acceso de solo lectura a los atributos del perfil.

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }

    public String getUrlPerfil() {
        return urlPerfil;
    }

    public String getFotoPerfil() {
        return FotoPerfil;
    }

    public String getFotoPortada() {
        return FotoPortada;
    }

    public String getContrasenaHash() {
        return contrasenaHash;
    }
    
    public String getSalt() {
        return salt;
    }
    
    /**
     * Verifica si una contraseña coincide con el hash almacenado
     */
    public boolean verificarContrasena(String contrasena) {
        return com.ejemplo.utils.SecurityUtils.verifyPassword(contrasena, this.contrasenaHash, this.salt);
    }

    /**
     * Sobreescribe el método toString() para proporcionar una representación
     * legible del objeto Perfil.
     *
     * @return Una cadena que contiene el nombre, apellido, email, URL del perfil,
     * y datos de foto/contraseña (aunque estas últimas suelen ser omitidas por seguridad en logs).
     */
    @Override
    public String toString() {
        // Nota: Incluir la contraseña en toString() no es una buena práctica de seguridad
        // para entornos de producción, ya que podría terminar en logs.
        return nombre + " " + apellido + " | Email: " + email + " | URL: " + urlPerfil +
               " | Foto Perfil: " + FotoPerfil + " | Foto Portada: " + FotoPortada +
               " | Contraseña: [ENCRIPTADA]"; // La contraseña está encriptada de forma segura.
    }
}