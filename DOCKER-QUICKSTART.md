# 🐳 Quick Start - Docker

## ⚡ Inicio Rápido

```bash
# 1. Abrir WSL2 (Fedora)
wsl -d FedoraLinux-43

# 2. Iniciar Docker
sudo systemctl start docker

# 3. Ir al proyecto
cd /mnt/c/Users/snayb/IdeaProjects/Api-control-financiero

# 4. Levantar todo
docker compose up -d

# 5. Ver logs
docker compose logs -f

# 6. Detener todo
docker compose down
```

## 📊 Estado de Servicios

```bash
docker compose ps
```

## 🔥 Probar API

```bash
# Crear usuario
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{"username":"test","email":"test@test.com","password":"123456","fullName":"Test User"}'

# Ver usuarios
curl http://localhost:8080/api/usuarios/1
```

## 🗄️ Acceder a PostgreSQL

```bash
docker exec -it control-financiero-db psql -U admin -d control_financiero
```

## 📝 Comandos Útiles

```bash
# Ver logs de la API
docker compose logs app -f

# Ver logs de PostgreSQL
docker compose logs postgres -f

# Reiniciar servicios
docker compose restart

# Reconstruir imagen
docker compose build --no-cache

# Limpiar todo
docker compose down -v
```

## 🎯 URLs

- **API**: http://localhost:8080
- **PostgreSQL**: localhost:5432
  - Usuario: `admin`
  - Password: `admin123`
  - Base de datos: `control_financiero`

## 🔧 Troubleshooting

```bash
# Ver uso de recursos
docker stats

# Entrar al contenedor de la API
docker exec -it control-financiero-api sh

# Ver variables de entorno
docker compose config
```

