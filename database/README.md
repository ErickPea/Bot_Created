# Scripts de Base de Datos - Bot de Registro Masivo

Esta carpeta contiene todos los scripts SQL necesarios para configurar la base de datos del proyecto.

## 📁 Archivos Disponibles

### `database_setup.sql`
- **Descripción**: Script original para MySQL estándar
- **Uso**: Para MySQL 8.0+ en puerto 3306
- **Características**: Incluye CHECK constraints

### `database_setup_mariadb.sql`
- **Descripción**: Script optimizado para MariaDB
- **Uso**: Para MariaDB en puerto 3307 (tu configuración actual)
- **Características**: 
  - Sin CHECK constraints problemáticos
  - Incluye triggers para validación
  - Compatible con MariaDB

## 🚀 Cómo Usar

### Opción 1: MySQL Workbench (Recomendada)
1. Abre MySQL Workbench
2. Conéctate a `localhost:3307` con usuario `root` (sin contraseña)
3. Abre el archivo `database_setup_mariadb.sql`
4. Ejecuta todo el script

### Opción 2: Línea de comandos
```bash
mysql -h localhost -P 3307 -u root < database_setup_mariadb.sql
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

## ⚠️ Notas Importantes

- **Puerto**: 3307 (configurado para tu MariaDB)
- **Usuario**: root (sin contraseña)
- **Base de datos**: proyecto_bots
- **Charset**: utf8mb4 (soporte completo de emojis)

## 🔒 Seguridad

En producción, cambia las credenciales por defecto y habilita SSL en `DatabaseConnection.java`. 