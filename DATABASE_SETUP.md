# Configuración de Base de Datos - Bot de Registro Masivo

## 📋 Requisitos Previos

1. **MySQL Server** instalado y ejecutándose en el puerto 3306
2. **Java JDK** 8 o superior
3. **Maven** instalado
4. **Usuario MySQL** con permisos para crear bases de datos

## 🚀 Opciones de Inicialización

### Opción 1: Script SQL Manual (Recomendado para primera vez)

1. Abre MySQL Workbench o tu cliente MySQL preferido
2. Ejecuta el archivo `database/database_setup_mariadb.sql` completo
3. Verifica que la base de datos `proyecto_bots` se haya creado

```sql
-- Ejecutar en MySQL:
source database/database_setup_mariadb.sql;
```

### Opción 2: Automatización desde Java (Recomendado para desarrollo)

#### En Windows (PowerShell):
```powershell
.\init_database.ps1
```

#### En Windows (Command Prompt):
```cmd
init_database.bat
```

#### En Linux/Mac:
```bash
./init_database.sh
```

### Opción 3: Desde la línea de comandos con Maven

```bash
mvn exec:java -Dexec.mainClass="com.ejemplo.persistence.DatabaseInitializer"
```

### Opción 4: Integración automática en el código

Agrega esta línea al inicio de tu `Main.java`:

```java
// Inicializar base de datos automáticamente
DatabaseInitializer.initializeDatabase();
```

## 🔧 Configuración de Conexión

### Credenciales por defecto:
- **Host**: localhost
- **Puerto**: 3307
- **Base de datos**: proyecto_bots
- **Usuario**: root
- **Contraseña**: (vacía)

### Para cambiar las credenciales:

Edita el archivo `src/main/java/com/ejemplo/persistence/DatabaseConnection.java`:

```java
private static final String USER = "tu_usuario";
private static final String PASSWORD = "tu_contraseña";
```

## 📊 Estructura de la Base de Datos

### Tablas creadas:

1. **estados** - Estados de las cuentas (Activa, Inactiva, Pendiente)
2. **cuentas** - Información de las cuentas creadas
3. **cookies** - Cookies de sesión de cada cuenta
4. **comentarios** - Comentarios predefinidos para usar
5. **acciones** - Registro de acciones realizadas por las cuentas
6. **publicaciones** - Publicaciones realizadas por las cuentas

### Índices creados:
- `idx_cuentas_email` - Búsqueda por email
- `idx_cuentas_plataforma` - Filtrado por plataforma
- `idx_cuentas_estado` - Filtrado por estado
- `idx_cookies_cuenta` - Relación cookies-cuenta
- `idx_acciones_cuenta` - Relación acciones-cuenta
- `idx_acciones_fecha` - Filtrado por fecha de acción
- `idx_publicaciones_cuenta` - Relación publicaciones-cuenta

## 🔍 Verificación

Para verificar que todo funciona correctamente:

```sql
USE proyecto_bots;
SHOW TABLES;
SELECT * FROM estados;
```

Deberías ver 6 tablas y 3 estados (Activa, Inactiva, Pendiente).

## ❗ Solución de Problemas

### Error: "Access denied for user 'root'@'localhost'"
- Verifica que MySQL esté ejecutándose
- Confirma las credenciales en `DatabaseConnection.java`
- Asegúrate de que el usuario tenga permisos para crear bases de datos

### Error: "Communications link failure"
- Verifica que MySQL esté ejecutándose en el puerto 3307
- Confirma que no haya firewall bloqueando la conexión

### Error: "Unknown database 'proyecto_bots'"
- Ejecuta la inicialización automática o el script SQL manual
- Verifica que el usuario tenga permisos para crear bases de datos

## 📝 Notas Importantes

- La base de datos usa `utf8mb4` para soporte completo de emojis
- Las claves foráneas tienen `ON DELETE CASCADE` para mantener integridad
- Los índices mejoran significativamente el rendimiento de las consultas
- El script es idempotente (se puede ejecutar múltiples veces sin problemas)

## 🔒 Seguridad

⚠️ **IMPORTANTE**: En producción, cambia las credenciales por defecto y habilita SSL:

```java
private static final String URL = "jdbc:mysql://localhost:3306/proyecto_bots?useSSL=true&serverTimezone=UTC";
private static final String USER = "usuario_seguro";
private static final String PASSWORD = "contraseña_segura";
``` 