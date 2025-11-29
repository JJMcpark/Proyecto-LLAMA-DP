# 🐳 Configuración de MySQL con Docker Desktop

Este documento explica cómo configurar MySQL usando Docker Desktop para el proyecto de Patrones de Diseño.

## Prerrequisitos

1. **Instalar Docker Desktop**
   - Descarga desde: https://www.docker.com/products/docker-desktop
   - Sigue las instrucciones de instalación para Windows
   - Reinicia tu computadora si es necesario

2. **Verificar instalación**
   ```powershell
   docker --version
   docker-compose --version
   ```

---

## 🚀 Opción 1: Comando Directo (Rápido)

Ejecuta este comando en PowerShell para iniciar MySQL:

```powershell
docker run --name mysql-proyecto -e MYSQL_ROOT_PASSWORD=Sopa123Do234Macaco345 -e MYSQL_DATABASE=proyecto_db -p 3306:3306 -d mysql:8.0
```

### Explicación del comando:
| Parámetro | Descripción |
|-----------|-------------|
| `--name mysql-proyecto` | Nombre del contenedor |
| `-e MYSQL_ROOT_PASSWORD=...` | Contraseña de root |
| `-e MYSQL_DATABASE=proyecto_db` | Crea la BD automáticamente |
| `-p 3306:3306` | Mapea el puerto 3306 |
| `-d` | Ejecuta en segundo plano |
| `mysql:8.0` | Imagen de MySQL 8.0 |

### Comandos útiles:

```powershell
# Ver contenedores en ejecución
docker ps

# Detener MySQL
docker stop mysql-proyecto

# Iniciar MySQL (si ya existe)
docker start mysql-proyecto

# Ver logs del contenedor
docker logs mysql-proyecto

# Eliminar el contenedor
docker rm -f mysql-proyecto
```

---

## 🚀 Opción 2: Docker Compose (Recomendado)

### Paso 1: Crear archivo docker-compose.yml

Crea un archivo `docker-compose.yml` en la raíz del proyecto con este contenido:

```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    container_name: mysql-proyecto
    restart: unless-stopped
    environment:
      MYSQL_ROOT_PASSWORD: Sopa123Do234Macaco345
      MYSQL_DATABASE: proyecto_db
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql
    command: 
      - --character-set-server=utf8mb4
      - --collation-server=utf8mb4_unicode_ci

volumes:
  mysql_data:
```

### Paso 2: Iniciar MySQL

```powershell
# Navegar a la carpeta del proyecto
cd "c:\Users\esteb\OneDrive\Escritorio\proyectollama"

# Iniciar MySQL
docker-compose up -d

# Ver estado
docker-compose ps

# Ver logs
docker-compose logs -f mysql

# Detener
docker-compose down

# Detener y eliminar volúmenes (BORRA DATOS)
docker-compose down -v
```

---

## ⚙️ Configuración en application.properties

El proyecto ya está configurado para conectarse a MySQL en `application.properties`:

```properties
# MySQL Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/proyecto_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=Sopa123Do234Macaco345
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

---

## 🔧 Conectar a MySQL desde terminal

Si necesitas acceder a MySQL directamente:

```powershell
# Acceder a la consola de MySQL dentro del contenedor
docker exec -it mysql-proyecto mysql -u root -p
# Ingresa la contraseña: Sopa123Do234Macaco345

# Una vez dentro de MySQL:
USE proyecto_db;
SHOW TABLES;
SELECT * FROM producto;
```

---

## 🧪 Verificar conexión

Antes de iniciar Spring Boot, verifica que MySQL esté funcionando:

```powershell
# Ver que el contenedor esté corriendo
docker ps | findstr mysql

# Debería mostrar algo como:
# abc123  mysql:8.0  "docker-entrypoint..."  Up 5 minutes  0.0.0.0:3306->3306/tcp  mysql-proyecto
```

---

## 🔥 Troubleshooting

### Error: Puerto 3306 ya en uso

```powershell
# Ver qué está usando el puerto
netstat -ano | findstr :3306

# Si es otro MySQL local, detenerlo o cambiar el puerto en docker:
docker run --name mysql-proyecto -e MYSQL_ROOT_PASSWORD=Sopa123Do234Macaco345 -p 3307:3306 -d mysql:8.0

# Y actualizar application.properties:
# spring.datasource.url=jdbc:mysql://localhost:3307/proyecto_db...
```

### Error: No puede conectar a la BD

1. Verificar que Docker esté corriendo (ícono en la barra de tareas)
2. Verificar que el contenedor esté activo: `docker ps`
3. Ver logs por errores: `docker logs mysql-proyecto`
4. Esperar unos segundos después de iniciar (MySQL tarda en arrancar)

### Error: Authentication plugin

Si aparece error de autenticación, actualiza el plugin:

```powershell
docker exec -it mysql-proyecto mysql -u root -p -e "ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY 'Sopa123Do234Macaco345';"
```

---

## 📋 Resumen de comandos

| Acción | Comando |
|--------|---------|
| Iniciar MySQL | `docker start mysql-proyecto` |
| Detener MySQL | `docker stop mysql-proyecto` |
| Ver logs | `docker logs mysql-proyecto` |
| Entrar a MySQL | `docker exec -it mysql-proyecto mysql -u root -p` |
| Estado | `docker ps` |

---

## 🎓 Para la presentación

1. Antes de la presentación, inicia Docker Desktop
2. Ejecuta: `docker start mysql-proyecto`
3. Espera 10 segundos
4. Inicia Spring Boot: `.\mvnw spring-boot:run`
5. Accede a: http://localhost:8080

¡Listo! 🎉
