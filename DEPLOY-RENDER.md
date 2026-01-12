# 🚀 Guía de Despliegue en Render

## 📋 Prerrequisitos

1. Cuenta en [Render](https://render.com) (gratis)
2. Tu código en GitHub
3. 5 minutos de tu tiempo

---

## 🎯 OPCIÓN 1: Despliegue Automático con render.yaml (Recomendado)

### Paso 1: Push a GitHub

```bash
git add .
git commit -m "feat: Add Render deployment configuration"
git push origin main
```

### Paso 2: Conectar en Render

1. Ve a [dashboard.render.com](https://dashboard.render.com)
2. Click en **"New +"** → **"Blueprint"**
3. Conecta tu repositorio de GitHub
4. Selecciona el repositorio: `Api-control-financiero`
5. Render detectará automáticamente el `render.yaml`
6. Click en **"Apply"**

### Paso 3: Esperar el despliegue

- PostgreSQL se creará primero (~2 minutos)
- Luego la API se desplegará automáticamente (~5 minutos)
- Render te dará una URL: `https://control-financiero-api.onrender.com`

---

## 🎯 OPCIÓN 2: Despliegue Manual (Más control)

### Paso 1: Crear Base de Datos PostgreSQL

1. En Render Dashboard → **"New +"** → **"PostgreSQL"**
2. Configuración:
   - **Name**: `control-financiero-db`
   - **Database**: `control_financiero`
   - **User**: `admin` (o el que quieras)
   - **Region**: Oregon (US West)
   - **Plan**: Free
3. Click en **"Create Database"**
4. **Guarda la URL de conexión** (la necesitarás en el siguiente paso)

### Paso 2: Crear Web Service

1. En Render Dashboard → **"New +"** → **"Web Service"**
2. Conecta tu repositorio de GitHub
3. Configuración:
   - **Name**: `control-financiero-api`
   - **Region**: Oregon (mismo que la BD)
   - **Branch**: `main`
   - **Runtime**: Docker
   - **Plan**: Free

4. **Variables de Entorno** (Environment Variables):
   ```
   SPRING_DATASOURCE_URL=<URL de PostgreSQL de Render>
   SPRING_DATASOURCE_USERNAME=admin
   SPRING_DATASOURCE_PASSWORD=<password generado por Render>
   SPRING_JPA_HIBERNATE_DDL_AUTO=update
   SPRING_PROFILES_ACTIVE=prod
   JAVA_OPTS=-Xms256m -Xmx512m
   ```

5. **Health Check Path**: `/actuator/health`

6. Click en **"Create Web Service"**

---

## ⚙️ Configuración Avanzada

### Variables de Entorno Disponibles

| Variable | Descripción | Valor Ejemplo |
|----------|-------------|---------------|
| `SPRING_DATASOURCE_URL` | URL de PostgreSQL | `jdbc:postgresql://...` |
| `SPRING_DATASOURCE_USERNAME` | Usuario de BD | `admin` |
| `SPRING_DATASOURCE_PASSWORD` | Password de BD | `xxxxx` |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | Modo Hibernate | `update` o `validate` |
| `SPRING_PROFILES_ACTIVE` | Perfil de Spring | `prod` |
| `JAVA_OPTS` | Opciones de JVM | `-Xms256m -Xmx512m` |

### Health Check

Render verificará cada 30 segundos que tu API esté viva en:
```
https://tu-app.onrender.com/actuator/health
```

Respuesta esperada:
```json
{
  "status": "UP"
}
```

---

## 🔍 Verificar Despliegue

### 1. Probar Health Check

```bash
curl https://control-financiero-api.onrender.com/actuator/health
```

Respuesta:
```json
{
  "status": "UP"
}
```

### 2. Crear un usuario

```bash
curl -X POST https://control-financiero-api.onrender.com/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@render.com",
    "password": "123456",
    "fullName": "Test User"
  }'
```

### 3. Ver usuarios

```bash
curl https://control-financiero-api.onrender.com/api/usuarios/1
```

---

## 📊 Monitoreo en Render

### Ver Logs en Tiempo Real

1. Ve a tu servicio en Render Dashboard
2. Click en **"Logs"**
3. Verás logs en tiempo real de tu aplicación

### Ver Métricas

1. Click en **"Metrics"**
2. Verás:
   - CPU usage
   - Memory usage
   - Request count
   - Response times

### Ver Estado de Health Check

1. Click en **"Events"**
2. Verás el historial de health checks

---

## ⚠️ IMPORTANTE: Plan Free de Render

### Limitaciones

- ✅ **GRATIS** para siempre
- ⚠️ **Se duerme después de 15 minutos** sin actividad
- ⏱️ **Primera request tarda ~30 segundos** en despertar
- 💾 **PostgreSQL Free**: 
  - 256 MB de RAM
  - 1 GB de almacenamiento
  - Expira después de 90 días (pero puedes renovar gratis)
- 🔄 **750 horas/mes** de runtime (suficiente para desarrollo)

### Soluciones

#### Mantener despierta la API (opcional)

Usa un servicio de ping como [UptimeRobot](https://uptimerobot.com) (gratis):
- Configurar para hacer ping cada 5 minutos a `/actuator/health`
- Mantiene tu API activa 24/7

#### Migrar a PostgreSQL externo (opcional)

Si tu base de datos crece, puedes usar:
- [ElephantSQL](https://www.elephantsql.com) - 20 MB gratis
- [Neon](https://neon.tech) - 10 GB gratis
- [Supabase](https://supabase.com) - 500 MB gratis

---

## 🐛 Troubleshooting

### Error: "Health check failed"

1. Verifica logs en Render Dashboard
2. Asegúrate de que `/actuator/health` responda
3. Verifica variables de entorno

### Error: "Database connection failed"

1. Verifica que la URL de PostgreSQL sea correcta
2. Asegúrate de que ambos servicios estén en la misma región
3. Verifica usuario y password

### Error: "Build failed"

1. Verifica que el Dockerfile sea correcto
2. Asegúrate de que el código compile localmente
3. Revisa logs de build en Render

### App está lenta

- Es normal en plan Free después de despertar
- Primera request tarda ~30 segundos
- Considera usar UptimeRobot para mantenerla activa

---

## 🔄 Actualizar la Aplicación

### Despliegue Continuo (Automático)

Render detecta automáticamente cambios en GitHub:

```bash
# Hacer cambios en el código
git add .
git commit -m "feat: Nueva funcionalidad"
git push origin main

# Render desplegará automáticamente en ~3-5 minutos
```

### Redesplegar Manualmente

1. Ve a Render Dashboard
2. Click en tu servicio
3. Click en **"Manual Deploy"** → **"Deploy latest commit"**

---

## 🎯 URLs Importantes

Después del despliegue tendrás:

- **API**: `https://control-financiero-api.onrender.com`
- **Health Check**: `https://control-financiero-api.onrender.com/actuator/health`
- **Swagger** (si lo agregas): `https://control-financiero-api.onrender.com/swagger-ui.html`
- **PostgreSQL**: Solo accesible desde tu app en Render

---

## 📝 Comandos Útiles

### Ver logs desde CLI

```bash
# Instalar Render CLI
npm install -g @render-inc/cli

# Login
render login

# Ver logs
render logs control-financiero-api --tail
```

### Conectarse a PostgreSQL

```bash
# Desde terminal local (si tienes psql instalado)
psql <URL_POSTGRESQL_DE_RENDER>

# Ver tablas
\dt

# Ver datos
SELECT * FROM users;
```

---

## 🚀 Mejoras Futuras

### Agregar Swagger/OpenAPI

```xml
<!-- En pom.xml -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

Luego accede a: `https://tu-app.onrender.com/swagger-ui.html`

### Agregar CORS

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("https://tu-frontend.com")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true);
    }
}
```

### Agregar HTTPS (Ya incluido)

Render proporciona HTTPS automáticamente con certificado SSL gratuito.

---

## 💰 Costo

| Servicio | Plan | Costo |
|----------|------|-------|
| PostgreSQL | Free | $0/mes |
| Web Service | Free | $0/mes |
| **TOTAL** | | **$0/mes** 🎉 |

### Si necesitas más (futuro):

| Servicio | Plan | Costo |
|----------|------|-------|
| PostgreSQL | Starter | $7/mes |
| Web Service | Starter | $7/mes |
| **TOTAL** | | **$14/mes** |

---

## ✅ Checklist de Despliegue

- [ ] Código pusheado a GitHub
- [ ] render.yaml en el repositorio
- [ ] Actuator agregado al pom.xml
- [ ] application-prod.properties configurado
- [ ] Cuenta en Render creada
- [ ] PostgreSQL creado en Render
- [ ] Web Service creado y desplegado
- [ ] Variables de entorno configuradas
- [ ] Health check funcionando
- [ ] API respondiendo correctamente
- [ ] Base de datos conectada

---

¡Tu API está lista para el mundo! 🌍

