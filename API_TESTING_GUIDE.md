# 🚀 CÓMO PROBAR LA API - Control Financiero

## ✅ Endpoints Disponibles

### 1️⃣ **Crear Usuario**
```http
POST http://localhost:8080/api/usuarios
Content-Type: application/json

{
  "username": "snayber",
  "email": "snayber@example.com",
  "password": "123456",
  "fullName": "Snayber Developer"
}
```

**Respuesta esperada (201 Created):**
```json
{
  "id": 1,
  "username": "snayber",
  "email": "snayber@example.com",
  "fullName": "Snayber Developer",
  "active": true,
  "createdAt": "2026-01-05T..."
}
```

---

### 2️⃣ **Obtener Usuario por ID**
```http
GET http://localhost:8080/api/usuarios/1
```

**Respuesta esperada (200 OK):**
```json
{
  "id": 1,
  "username": "snayber",
  "email": "snayber@example.com",
  "fullName": "Snayber Developer",
  "active": true,
  "createdAt": "2026-01-05T..."
}
```

---

## 🔧 Instrucciones para Ejecutar

### Opción 1: Desde IntelliJ IDEA
1. Abrir el proyecto
2. Buscar la clase `ControlFinacieroApplication.java`
3. Click derecho → **Run 'ControlFinacieroApplication'**
4. Esperar a que inicie (verás: "Started ControlFinacieroApplication in X seconds")

### Opción 2: Desde Terminal/CMD
```bash
# En la raíz del proyecto
mvnw spring-boot:run
```

### Opción 3: Con Maven
```bash
mvn clean install
mvn spring-boot:run
```

---

## 🧪 Probar con cURL

### Crear Usuario:
```bash
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"snayber\",\"email\":\"snayber@example.com\",\"password\":\"123456\",\"fullName\":\"Snayber Developer\"}"
```

### Obtener Usuario:
```bash
curl http://localhost:8080/api/usuarios/1
```

---

## 🧪 Probar con Postman

1. **Crear Usuario:**
   - Método: `POST`
   - URL: `http://localhost:8080/api/usuarios`
   - Headers: `Content-Type: application/json`
   - Body (raw JSON):
   ```json
   {
     "username": "snayber",
     "email": "snayber@example.com",
     "password": "123456",
     "fullName": "Snayber Developer"
   }
   ```

2. **Obtener Usuario:**
   - Método: `GET`
   - URL: `http://localhost:8080/api/usuarios/1`

---

## 📋 Casos de Error

### Error: Email duplicado
```http
POST http://localhost:8080/api/usuarios
{
  "username": "otro",
  "email": "snayber@example.com",  # Email ya existe
  "password": "123456",
  "fullName": "Otro Usuario"
}
```
**Respuesta (400 Bad Request):**
```json
{
  "status": 400,
  "message": "Ya existe un usuario con ese email",
  "timestamp": "2026-01-05T..."
}
```

### Error: Usuario no encontrado
```http
GET http://localhost:8080/api/usuarios/999
```
**Respuesta (404 Not Found):**
```json
{
  "status": 404,
  "message": "Usuario no encontrado con ID: 999",
  "timestamp": "2026-01-05T..."
}
```

### Error: Validación
```http
POST http://localhost:8080/api/usuarios
{
  "username": "ab",  # Menos de 3 caracteres
  "email": "invalido",  # Email inválido
  "password": "123"  # Menos de 6 caracteres
}
```
**Respuesta (400 Bad Request):**
```json
{
  "status": 400,
  "message": "Error de validación",
  "errors": {
    "username": "El username debe tener entre 3 y 50 caracteres",
    "email": "El email debe ser válido",
    "password": "La contraseña debe tener al menos 6 caracteres"
  },
  "timestamp": "2026-01-05T..."
}
```

---

## 🎯 Verificar que la API está funcionando

Una vez iniciada la aplicación, abre el navegador:
```
http://localhost:8080/api/usuarios/1
```

Si ves un error 404 (usuario no existe), ¡la API está funcionando! Solo necesitas crear un usuario primero.

---

## 📊 Verificar en la Base de Datos (Neon)

Puedes verificar los datos directamente en Neon PostgreSQL:

```sql
-- Ver todos los usuarios
SELECT * FROM users;

-- Ver un usuario específico
SELECT * FROM users WHERE id = 1;
```

---

## ✅ Checklist de Verificación

- [ ] La aplicación inicia sin errores
- [ ] Puerto 8080 está disponible
- [ ] Puedes crear un usuario con POST
- [ ] Puedes obtener el usuario con GET
- [ ] Los errores de validación funcionan
- [ ] Los datos se guardan en la base de datos Neon

---

## 🐛 Troubleshooting

### Error: "Port 8080 is already in use"
Cambia el puerto en `application.properties`:
```properties
server.port=8081
```

### Error: "Unable to obtain JDBC Connection"
Verifica que las credenciales de Neon sean correctas en `application.properties`.

### Error: "Table 'users' doesn't exist"
Ejecuta el script SQL de creación de tablas en Neon (ya deberías tenerlo).

---

## 🎉 ¡Listo!

Tu API REST está funcionando con:
- ✅ Clean Architecture
- ✅ Hexagonal Architecture
- ✅ Domain-Driven Design
- ✅ PostgreSQL (Neon)
- ✅ Spring Boot 3.5.9
- ✅ Java 21

