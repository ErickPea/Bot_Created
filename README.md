# Documentación del Proyecto: Bot de Registro Masivo

## Estructura del Proyecto

```
Bot_Created/
├── src/main/java/com/ejemplo/
│   ├── botcreate/
│   │   ├── Main.java                 # Punto de entrada de la aplicación
│   │   └── orquestador/
│   │       └── BotManager.java       # Controla concurrencia y coordinación
│   ├── config/
│   │   └── AppConfig.java            # Configuración centralizada de la aplicación
│   ├── services/
│   │   ├── FacebookBotService.java   # Lógica de registro en Facebook (mejorado)
│   │   ├── TwitterBotService.java    # Placeholder para otro bot
│   │   ├── DatabaseService.java      # Persistencia en MySQL (mejorado)
│   │   └── LoggerService.java        # Sistema de logging profesional
│   ├── utils/
│   │   └── SecurityUtils.java        # Utilidades de seguridad y encriptación
│   ├── persistence/
│   │   ├── DatabaseConnection.java   # Conexión a MySQL vía JDBC (configurable)
│   │   └── CookieManager.java        # Guardado/carga de cookies
│   └── models/
│       ├── Perfil.java               # Datos del perfil (con seguridad)
│       ├── EstadoCuenta.java         # Enum: Activa, Inactiva, Pendiente
│       └── CookieData.java           # Estructura simple de cookie
├── src/main/resources/
│   └── config.properties             # Configuración centralizada
├── database/                         # Scripts de base de datos
│   ├── README.md                     # Documentación de scripts SQL
│   ├── database_setup.sql            # Script para MySQL estándar
│   └── database_setup_mariadb.sql    # Script para MariaDB (recomendado)
├── logs/                             # Archivos de log (se crea automáticamente)
├── DATABASE_SETUP.md                 # Guía de configuración de BD
└── README.md                         # Documentación principal
```

## 🚀 Mejoras Implementadas

### ✅ **Nuevas Funcionalidades:**

1. **🔧 Sistema de Configuración Centralizada**
   - Archivo `config.properties` para todos los parámetros
   - Clase `AppConfig` para acceso seguro a configuraciones
   - Fácil modificación sin recompilar

2. **📊 Sistema de Logging Profesional**
   - Logs con timestamps y niveles
   - Archivo de log automático (`logs/bot.log`)
   - Métodos específicos para cada tipo de evento

3. **🔒 Seguridad Mejorada**
   - Encriptación de contraseñas con SHA-256 + salt
   - Generación de contraseñas seguras
   - Validación de contraseñas

4. **⚙️ Configuración Dinámica**
   - Timeouts configurables
   - Número de bots ajustable
   - URLs configurables
   - Modo headless configurable

5. **🐛 Manejo de Errores Robusto**
   - Try-catch apropiados
   - Logging detallado de errores
   - Recuperación de errores

## Descripción de Archivos

### Main.java

Clase principal que inicia la ejecución del bot con logging profesional. Llama al BotManager para empezar el proceso de creación de cuentas.

### config/AppConfig.java

Clase que centraliza toda la configuración de la aplicación. Carga parámetros desde `config.properties` y proporciona métodos seguros para acceder a configuraciones como URLs, timeouts, credenciales de BD, etc.

### orquestador/BotManager.java

Gestiona la concurrencia utilizando un `ExecutorService` para correr múltiples bots en paralelo. Ahora usa configuración dinámica para el número de bots y timeouts. Incluye logging detallado de operaciones.

### services/FacebookBotService.java

Contiene la lógica específica para crear cuentas en Facebook (mejorado):

* Llena el formulario de registro usando configuración dinámica.
* Genera datos falsos con Faker y contraseñas seguras.
* Simula la carga de fotos de perfil y portada.
* Incluye logging detallado de cada paso.
* Manejo robusto de errores con recuperación.
* Devuelve un objeto `Perfil` con contraseña encriptada.

### services/TwitterBotService.java

Placeholder para la lógica de registro en Twitter.

### services/DatabaseService.java

Se encarga de (mejorado):

* Insertar perfiles generados en la base de datos con contraseñas encriptadas.
* Guardar cookies asociadas a cada cuenta.
* Logging detallado de operaciones de BD.
* Manejo robusto de errores SQL.
* Uso de configuración centralizada para conexión.

### services/LoggerService.java

Sistema de logging profesional que gestiona:

* Logs con timestamps y niveles (INFO, WARNING, ERROR, DEBUG).
* Archivo de log automático (`logs/bot.log`).
* Métodos específicos para diferentes tipos de eventos.
* Logs de consola y archivo simultáneos.
* Formato estructurado para fácil análisis.

### utils/SecurityUtils.java

Utilidades de seguridad que proporcionan:

* Encriptación de contraseñas con SHA-256 + salt.
* Generación de contraseñas seguras aleatorias.
* Validación de requisitos de contraseñas.
* Enmascaramiento de contraseñas para logging.
* Verificación de contraseñas encriptadas.

### persistence/DatabaseConnection.java

Establece la conexión con MySQL mediante JDBC usando configuración centralizada desde `AppConfig`.

### persistence/CookieManager.java

Permite guardar y cargar cookies generadas durante el registro.

### models/Perfil.java

Clase que representa la estructura de un perfil (con seguridad):

* Nombre
* Apellido
* Email
* URL del perfil
* Foto de perfil
* Foto de portada
* Contraseña encriptada (hash + salt)
* Método para verificar contraseñas

### models/EstadoCuenta.java

Enumeración que define el estado de una cuenta:

* ACTIVA
* INACTIVA
* PENDIENTE

### models/CookieData.java

Modelo simple que almacena:

* Nombre de la cookie
* Valor de la cookie

### src/main/resources/config.properties

Archivo de configuración centralizada que contiene:

* Configuración de base de datos (URL, usuario, contraseña).
* Configuración de bots (número, timeouts, modo headless).
* URLs de plataformas sociales.
* Configuración de logging y seguridad.

---

## Estructura de la Base de Datos

```sql
CREATE DATABASE proyecto_bots;
USE proyecto_bots;

CREATE TABLE cuentas (
    id CHAR(36) PRIMARY KEY,
    plataforma VARCHAR(50),
    username VARCHAR(100),
    email VARCHAR(100),
    contrasena VARCHAR(255),
    estado_id INT,
    enlace_perfil TEXT,
    foto_perfil TEXT,
    foto_portada TEXT,
    nombre VARCHAR(100),
    apellidos VARCHAR(100),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE cookies (
    id CHAR(36) PRIMARY KEY,
    cuenta_id CHAR(36) NOT NULL,
    data JSON,
    fecha_guardado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cuenta_id) REFERENCES cuentas(id)
);

CREATE TABLE estados (
    id INT PRIMARY KEY,
    descripcion VARCHAR(50)
);

INSERT INTO estados (id, descripcion) VALUES
(1, 'Activa'),
(2, 'Inactiva'),
(3, 'Pendiente');

CREATE TABLE comentarios (
    id CHAR(36) PRIMARY KEY,
    tema VARCHAR(255) NOT NULL,
    comentario TEXT NOT NULL,
    estado VARCHAR(20) CHECK (estado IN ('Activo', 'Inactivo')),
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE acciones (
    id CHAR(36) PRIMARY KEY,
    cuenta_id CHAR(36) NOT NULL,
    tipo_accion VARCHAR(20) CHECK (tipo_accion IN ('Reaccion', 'Comentario', 'Compartir')),
    contenido TEXT,
    enlace_objetivo TEXT,
    comentario_id CHAR(36),
    texto_comentario TEXT,
    fecha_accion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cuenta_id) REFERENCES cuentas(id),
    FOREIGN KEY (comentario_id) REFERENCES comentarios(id)
);

CREATE TABLE publicaciones (
    id CHAR(36) PRIMARY KEY,
    cuenta_id CHAR(36) NOT NULL,
    contenido_publicado TEXT,
    enlace_publicacion TEXT,
    fecha_publicacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cuenta_id) REFERENCES cuentas(id)
);
```

---

## Flujo General (Mejorado)

1. `Main.java` inicia el proceso con logging profesional.
2. `AppConfig` carga la configuración desde `config.properties`.
3. `BotManager` gestiona la creación concurrente de bots usando configuración dinámica.
4. Cada bot ejecuta la lógica de `FacebookBotService` con logging detallado.
5. `SecurityUtils` genera contraseñas seguras y las encripta.
6. El perfil generado se persiste en MySQL con `DatabaseService` (contraseña encriptada).
7. `LoggerService` registra todas las operaciones en archivo y consola.
8. Las cookies son gestionadas por `CookieManager` y guardadas en la tabla correspondiente.
9. El estado de la cuenta se registra en la tabla `estados`.

---

## 🛠️ Configuración y Uso

### Configuración Inicial
1. **Base de Datos**: Ejecutar `database/database_setup_mariadb.sql`
2. **Configuración**: Editar `src/main/resources/config.properties`
3. **Logs**: Se crean automáticamente en `logs/bot.log`

### Ejecución
```bash
mvn exec:java -Dexec.mainClass="com.ejemplo.botcreate.Main"
```

### Monitoreo
- **Logs en tiempo real**: `tail -f logs/bot.log`
- **Configuración**: Editar `config.properties` sin recompilar
- **Base de datos**: Verificar en `proyecto_bots.cuentas`

## 📊 Beneficios de las Mejoras

* **🔒 Seguridad**: Contraseñas encriptadas, validación robusta
* **📈 Escalabilidad**: Configuración dinámica, múltiples bots
* **🐛 Debugging**: Logs detallados, manejo de errores
* **⚙️ Flexibilidad**: Configuración externa, fácil mantenimiento
* **📋 Monitoreo**: Logs estructurados, seguimiento de operaciones

## Recomendaciones

* Asegurar que las tablas de la base de datos estén sincronizadas con los modelos.
* Configurar adecuadamente la conexión en `config.properties`.
* Implementar `TwitterBotService` y otros servicios según se necesite.
* Revisar logs regularmente para monitorear el funcionamiento.
* Cambiar credenciales por defecto en producción.

---
