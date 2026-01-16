# 📘 API REST - Control Financiero

## 🔒 Autenticación

Todos los endpoints (excepto `/api/auth/**`) requieren autenticación mediante JWT Bearer Token.

### Header requerido:
```
Authorization: Bearer <tu_token_jwt>
```

---

## 🔐 Endpoints de Autenticación

### 1. Registro de Usuario
**POST** `/api/auth/register`

**Request Body:**
```json
{
  "username": "snayber",
  "email": "snayber@example.com",
  "password": "123456",
  "fullName": "Snayber Dev"
}
```

**Response (201):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "userId": 1,
  "username": "snayber",
  "email": "snayber@example.com",
  "fullName": "Snayber Dev"
}
```

---

### 2. Login de Usuario
**POST** `/api/auth/login`

**Request Body:**
```json
{
  "username": "snayber",
  "password": "123456"
}
```

**Response (200):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "userId": 1,
  "username": "snayber",
  "email": "snayber@example.com",
  "fullName": "Snayber Dev"
}
```

---

## 📊 Dashboard & Reportes

### 3. Obtener Resumen Financiero
**GET** `/api/dashboard/summary`

**Query Params (opcionales):**
- `startDate`: YYYY-MM-DD (por defecto: primer día del mes actual)
- `endDate`: YYYY-MM-DD (por defecto: último día del mes actual)

**Ejemplo:**
```
GET /api/dashboard/summary?startDate=2026-01-01&endDate=2026-01-31
```

**Response (200):**
```json
{
  "totalIncome": 3000000.00,
  "totalExpenses": 1500000.00,
  "totalSavings": 500000.00,
  "currentBalance": 1000000.00,
  "availableBalance": 500000.00,
  "periodStart": "2026-01-01",
  "periodEnd": "2026-01-31",
  "totalTransactions": 15,
  "goalsOverview": {
    "activeGoals": 2,
    "completedGoals": 1,
    "totalTargetAmount": 20000000.00,
    "totalSavedAmount": 1500000.00,
    "averageProgress": 7.5
  }
}
```

---

### 4. Obtener Gastos por Categoría
**GET** `/api/dashboard/expenses-by-category`

**Query Params (opcionales):**
- `startDate`: YYYY-MM-DD
- `endDate`: YYYY-MM-DD

**Response (200):**
```json
{
  "categories": [
    {
      "categoryId": 1,
      "categoryName": "Gastos Personales",
      "colorHex": "#FF5733",
      "amount": 500000.00,
      "transactionCount": 8,
      "percentage": 33.33
    },
    {
      "categoryId": 2,
      "categoryName": "Novia",
      "colorHex": "#FF69B4",
      "amount": 300000.00,
      "transactionCount": 5,
      "percentage": 20.00
    }
  ],
  "totalExpenses": 1500000.00
}
```

---

## 💰 Movimientos Financieros

### 5. Crear Movimiento
**POST** `/api/movimientos`

**⚠️ NOTA:** Ya NO se envía `usuarioId` (se obtiene del token JWT)

**Request Body:**
```json
{
  "tipoMovimiento": "EXPENSE",
  "monto": 150000,
  "descripcion": "Salida con la novia",
  "fechaMovimiento": "2026-01-11",
  "categoriaId": 1,
  "fuenteIngresoId": null,
  "metaId": null,
  "esRecurrente": false,
  "patronRecurrencia": null,
  "notas": "Cine y cena"
}
```

**Response (201):**
```json
{
  "id": 1,
  "usuarioId": 1,
  "tipoMovimiento": "EXPENSE",
  "monto": 150000.00,
  "descripcion": "Salida con la novia",
  "fechaMovimiento": "2026-01-11",
  "categoriaId": 1,
  "categoriaNombre": "Gastos Personales",
  "fuenteIngresoId": null,
  "fuenteIngresoNombre": null,
  "metaId": null,
  "metaNombre": null,
  "esRecurrente": false,
  "patronRecurrencia": null,
  "notas": "Cine y cena",
  "createdAt": "2026-01-11T22:30:00",
  "updatedAt": "2026-01-11T22:30:00"
}
```

---

### 6. Obtener Mis Movimientos
**GET** `/api/movimientos`

**⚠️ CAMBIO:** URL sin `/usuario/{id}` - El usuario se obtiene del token.

**Query Params (opcionales):**
- `tipo`: INCOME | EXPENSE | SAVINGS | LOAN | TRANSFER
- `fechaInicio`: YYYY-MM-DD
- `fechaFin`: YYYY-MM-DD
- `categoriaId`: Long

**Ejemplos:**
```
GET /api/movimientos
GET /api/movimientos?tipo=EXPENSE
GET /api/movimientos?fechaInicio=2026-01-01&fechaFin=2026-01-31
GET /api/movimientos?categoriaId=1
```

**Response (200):**
```json
[
  {
    "id": 1,
    "usuarioId": 1,
    "tipoMovimiento": "EXPENSE",
    "monto": 150000.00,
    "descripcion": "Salida con la novia",
    "fechaMovimiento": "2026-01-11",
    "categoriaId": 1,
    "categoriaNombre": "Gastos Personales",
    ...
  }
]
```

---

### 7. Obtener Movimiento por ID
**GET** `/api/movimientos/{id}`

**Response (200):** Detalle del movimiento

**Errores:**
- **404:** Movimiento no encontrado
- **403:** No tienes permiso (no es tuyo)

---

### 8. Eliminar Movimiento
**DELETE** `/api/movimientos/{id}`

**✅ MEJORA:** Ahora retorna **409 Conflict** en lugar de **500** cuando hay referencias.

**Response (204):** Sin contenido (éxito)

**Errores:**
- **404:** Movimiento no encontrado
- **403:** No tienes permiso
- **409:** No se puede eliminar porque está vinculado a una meta activa

---

## 📁 Categorías

### 9. Crear Categoría
**POST** `/api/categorias`

**⚠️ NOTA:** Ya NO se envía `usuarioId`

**Request Body:**
```json
{
  "nombre": "Gastos Personales",
  "descripcion": "Gastos del día a día",
  "tipoCategoria": "EXPENSE",
  "colorHex": "#FF5733",
  "icono": "wallet"
}
```

**Response (201):**
```json
{
  "id": 1,
  "usuarioId": 1,
  "nombre": "Gastos Personales",
  "descripcion": "Gastos del día a día",
  "colorHex": "#FF5733",
  "icono": "wallet",
  "tipoCategoria": "EXPENSE",
  "activa": true,
  "createdAt": "2026-01-11T22:00:00",
  "updatedAt": "2026-01-11T22:00:00"
}
```

---

### 10. Obtener Mis Categorías
**GET** `/api/categorias`

**Response (200):** Lista de categorías del usuario autenticado

---

### 11. Obtener Categoría por ID
**GET** `/api/categorias/{id}`

---

### 12. Eliminar Categoría
**DELETE** `/api/categorias/{id}`

**Errores:**
- **409:** No se puede eliminar porque tiene movimientos asociados

---

## 🎯 Metas Financieras

### 13. Crear Meta
**POST** `/api/metas`

**⚠️ NOTA:** Ya NO se envía `usuarioId`

**Request Body:**
```json
{
  "nombre": "Comprar Moto XR 190L",
  "descripcion": "Moto para transporte",
  "montoObjetivo": 15000000,
  "fechaObjetivo": "2026-12-31",
  "prioridad": "HIGH"
}
```

**Response (201):**
```json
{
  "id": 1,
  "usuarioId": 1,
  "nombre": "Comprar Moto XR 190L",
  "descripcion": "Moto para transporte",
  "montoObjetivo": 15000000.00,
  "montoActual": 0.00,
  "fechaObjetivo": "2026-12-31",
  "prioridad": "HIGH",
  "estado": "ACTIVE",
  "createdAt": "2026-01-11T22:00:00",
  "updatedAt": "2026-01-11T22:00:00",
  "completedAt": null
}
```

---

### 14. Obtener Mis Metas
**GET** `/api/metas`

---

### 15. Obtener Meta por ID
**GET** `/api/metas/{id}`

---

### 16. Eliminar Meta
**DELETE** `/api/metas/{id}`

**Errores:**
- **409:** No se puede eliminar porque tiene movimientos de ahorro asociados

---

## 🔧 Códigos de Error

| Código | Significado |
|--------|-------------|
| **200** | OK |
| **201** | Creado exitosamente |
| **204** | Sin contenido (eliminación exitosa) |
| **400** | Bad Request (validación fallida) |
| **401** | No autenticado (token inválido/expirado) |
| **403** | Prohibido (no tienes permiso) |
| **404** | No encontrado |
| **409** | Conflicto (no se puede eliminar por referencias) |
| **500** | Error interno del servidor |

---

## 🌐 CORS

La API permite peticiones desde:
- `http://localhost:5173` (Vite dev)
- `http://localhost:3000` (React dev)
- `https://*.vercel.app` (Vercel)
- `https://*.netlify.app` (Netlify)
- `https://api-control-financiero.onrender.com` (Backend)

---

## 📝 Ejemplo de Flujo Completo

### 1. Registrarse
```bash
POST /api/auth/register
Body: { "username": "john", "email": "john@example.com", "password": "123456" }
```

### 2. Guardar el token
```javascript
const token = response.token;
localStorage.setItem('token', token);
```

### 3. Hacer peticiones autenticadas
```bash
GET /api/dashboard/summary
Headers: { "Authorization": "Bearer <token>" }
```

### 4. Crear un movimiento
```bash
POST /api/movimientos
Headers: { "Authorization": "Bearer <token>" }
Body: {
  "tipoMovimiento": "INCOME",
  "monto": 3000000,
  "descripcion": "Salario",
  "fechaMovimiento": "2026-01-15"
}
```

---

## ✅ Mejoras Implementadas

1. **✅ Sin userId en URLs** - El usuario se extrae del token JWT
2. **✅ Mejor manejo de errores** - 409 en lugar de 500 en eliminaciones
3. **✅ Validación de ownership** - No puedes ver/editar recursos de otros usuarios
4. **✅ Endpoints de agregación** - Dashboard con totales calculados en BD
5. **✅ CORS configurado** - Listo para React/Angular frontend

---

## 🚀 URL Base en Producción

```
https://api-control-financiero.onrender.com
```

**Ejemplo:**
```
POST https://api-control-financiero.onrender.com/api/auth/login
```

