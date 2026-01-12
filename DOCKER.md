# 🐳 Guía de Docker - API Control Financiero

## 📋 Prerrequisitos

### 🔥 OPCIÓN A: Docker Engine en WSL2 (Sin Docker Desktop - Recomendado)
- WSL2 instalado en Windows
- Docker Engine instalado en WSL2
- Docker Compose instalado

### 💪 OPCIÓN B: Podman (Alternativa open-source)
- Podman instalado
- Compatible con comandos de Docker

---

## 🔧 Instalación (Sin Docker Desktop)

### ⚡ Método 1: Docker Engine en WSL2 (Ubuntu)

#### PASO 1: Habilitar WSL2 en Windows
```powershell
# En PowerShell como Administrador
wsl --install

# Reinicia tu PC después de esto
```

#### PASO 2: Instalar Docker Engine en WSL2
```bash
# Abre Ubuntu desde el menú de Windows
# Luego ejecuta estos comandos:

# Actualizar sistema
sudo apt update && sudo apt upgrade -y

# Instalar dependencias
sudo apt install -y ca-certificates curl gnupg lsb-release

# Agregar llave GPG oficial de Docker
sudo mkdir -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg

# Configurar repositorio de Docker
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

# Actualizar índice de paquetes
sudo apt update

# Instalar Docker Engine, CLI, Containerd y Compose
sudo apt install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

# Agregar tu usuario al grupo docker (para no usar sudo)
sudo usermod -aG docker $USER

# Aplicar cambios de grupo
newgrp docker
```

#### PASO 3: Configurar inicio automático
```bash
# Crear script de inicio
echo '#!/bin/sh
sudo service docker start' | sudo tee /usr/local/bin/start-docker
sudo chmod +x /usr/local/bin/start-docker

# Ahora cada vez que abras WSL2, ejecuta:
start-docker
```

#### PASO 4: Verificar instalación
```bash
docker --version
docker compose version
docker ps
```

---

### ⚡ Método 2: Podman (Windows nativo)

```powershell
# Con Chocolatey
choco install podman-desktop

# O descarga el instalador desde:
# https://github.com/containers/podman/releases

# Después de instalar, verifica:
podman --version

# Podman usa los mismos comandos que Docker:
# podman run = docker run
# podman-compose = docker-compose
```

---

## 🚀 Comandos Básicos

### 1️⃣ Construir y levantar los servicios

```bash
# Desde WSL2, navega a tu proyecto:
cd /mnt/c/Users/snayb/IdeaProjects/Api-control-financiero

# Construir las imágenes y levantar los contenedores
docker compose up --build

# O en modo detached (segundo plano)
docker compose up --build -d

# Nota: "docker compose" (con espacio) es la nueva sintaxis
# "docker-compose" (con guión) es la versión antigua
```

### 2️⃣ Ver logs

```bash
# Ver logs de todos los servicios
docker compose logs -f

# Ver logs solo de la API
docker compose logs -f app

# Ver logs solo de PostgreSQL
docker compose logs -f postgres
```

### 3️⃣ Detener servicios

```bash
# Detener servicios (mantiene los datos)
docker compose stop

# Detener y eliminar contenedores (mantiene volúmenes)
docker compose down

# Detener, eliminar contenedores Y volúmenes (BORRA DATOS)
docker compose down -v
```

### 4️⃣ Ver estado de contenedores

```bash
# Ver contenedores corriendo
docker compose ps

# Ver todos los contenedores
docker ps -a
```

### 5️⃣ Reiniciar servicios

```bash
# Reiniciar todos los servicios
docker compose restart

# Reiniciar solo la API
docker compose restart app
```

### 6️⃣ Acceder al contenedor

```bash
# Entrar a la shell de la API
docker exec -it control-financiero-api sh

# Entrar a PostgreSQL
docker exec -it control-financiero-db psql -U admin -d control_financiero
```

---

## 🔧 Configuración

### Variables de Entorno

Puedes modificar las variables en `docker-compose.yml` o crear un archivo `.env`:

```env
# Base de datos
POSTGRES_DB=control_financiero
POSTGRES_USER=admin
POSTGRES_PASSWORD=admin123

# Spring
SPRING_PROFILES_ACTIVE=docker
JAVA_OPTS=-Xms512m -Xmx1024m
```

### Puertos

- **API**: http://localhost:8080
- **PostgreSQL**: localhost:5432

---

## 📊 Comandos Útiles

### Ver uso de recursos

```bash
docker stats
```

### Limpiar sistema Docker

```bash
# Eliminar contenedores detenidos
docker container prune

# Eliminar imágenes no utilizadas
docker image prune

# Limpieza completa (cuidado!)
docker system prune -a
```

### Backup de la base de datos

```bash
# Hacer backup
docker exec control-financiero-db pg_dump -U admin control_financiero > backup.sql

# Restaurar backup
docker exec -i control-financiero-db psql -U admin control_financiero < backup.sql
```

---

## 🐛 Troubleshooting

### Docker no inicia en WSL2

```bash
# Iniciar servicio Docker
sudo service docker start

# Ver estado
sudo service docker status

# Si falla, revisar logs
sudo journalctl -u docker.service
```

### La API no inicia

1. Verificar que PostgreSQL esté corriendo y saludable:
```bash
docker compose ps
```

2. Ver logs de la API:
```bash
docker compose logs app
```

### Puerto ya en uso

Si el puerto 8080 o 5432 está ocupado, cambia en `docker-compose.yml`:

```yaml
ports:
  - "8081:8080"  # Puerto externo:interno
```

### Problemas de conexión a base de datos

1. Verificar que los contenedores estén en la misma red
2. Verificar credenciales en variables de entorno
3. Reiniciar servicios:
```bash
docker compose restart
```

### Error: "Cannot connect to Docker daemon"

```bash
# Iniciar Docker
sudo service docker start

# O agregar tu usuario al grupo docker
sudo usermod -aG docker $USER
newgrp docker
```

---

## 🏗️ Arquitectura Docker

```
┌─────────────────────────────────────┐
│   Docker Compose Network            │
│                                     │
│  ┌──────────────┐  ┌─────────────┐ │
│  │              │  │             │ │
│  │  PostgreSQL  │◄─┤  Spring API │ │
│  │   :5432      │  │   :8080     │ │
│  │              │  │             │ │
│  └──────────────┘  └─────────────┘ │
│         ▲                 ▲         │
│         │                 │         │
│    postgres-data      app-logs     │
│     (volumen)        (volumen)     │
└─────────────────────────────────────┘
         │                 │
         └─────────────────┘
           WSL2 / Host
     localhost:5432  localhost:8080
```

---

## 📝 Notas Importantes

- Los datos de PostgreSQL persisten en el volumen `postgres-data`
- Los logs de la aplicación se guardan en el volumen `app-logs`
- La aplicación usa el perfil `docker` de Spring
- El healthcheck verifica que los servicios estén funcionando correctamente

### 🔥 Ventajas de usar Docker Engine sin Desktop:
- ✅ Más ligero (no consume recursos en segundo plano)
- ✅ Más rápido
- ✅ Más control
- ✅ Open-source al 100%
- ✅ No telemetría ni rastreo

---

## 🎯 Quick Start (Resumen)

```bash
# 1. Abrir WSL2 (Ubuntu)
wsl

# 2. Iniciar Docker
sudo service docker start

# 3. Ir al proyecto
cd /mnt/c/Users/snayb/IdeaProjects/Api-control-financiero

# 4. Levantar todo
docker compose up --build -d

# 5. Ver logs
docker compose logs -f

# 6. Probar la API
curl http://localhost:8080/api/usuarios

# 7. Cuando termines
docker compose down
```

---

## 🔐 Seguridad

**⚠️ IMPORTANTE para Producción:**

1. Cambiar contraseñas por defecto
2. Usar secretos de Docker en lugar de variables de entorno
3. Configurar red con restricciones
4. Habilitar SSL/TLS
5. Limitar recursos de contenedores
6. No exponer puertos innecesarios
7. Usar imágenes oficiales y verificadas

---

## 🆘 Comandos de Emergencia

```bash
# Matar todos los contenedores
docker kill $(docker ps -q)

# Eliminar todo (contenedores, imágenes, volúmenes, redes)
docker system prune -a --volumes

# Ver uso de disco de Docker
docker system df

# Limpiar cache de build
docker builder prune
```

