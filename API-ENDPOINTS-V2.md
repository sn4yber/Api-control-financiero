# 📘 API REST - Control Financiero

## 🔒 Autenticación

Todos los endpoints (excepto `/api/auth/**`) requieren autenticación mediante JWT Bearer Token.

### Header requerido:
```
Authorization: Bearer <tu_token_jwt>
```

---

## 📋 Tabla de Contenidos

1. [Autenticación](#-endpoints-de-autenticación)
2. [Dashboard & Reportes](#-dashboard--reportes)
3. [Movimientos Financieros](#-movimientos-financieros)
4. [Categorías](#-categorías)
5. [Metas Financieras](#-metas-financieras)
6. [Fuentes de Ingreso](#-fuentes-de-ingreso)
7. [Movimientos Recurrentes](#-movimientos-recurrentes)
8. [Presupuestos](#-presupuestos)
9. [Reportes y Exportación](#-reportes-y-exportación)
10. [Inteligencia Financiera](#-inteligencia-financiera)
11. [Notificaciones](#-notificaciones)
12. [Auditoría](#-auditoría)

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

## 💵 Fuentes de Ingreso

### 16. Crear Fuente de Ingreso
**POST** `/api/fuentes-ingreso`

**Request Body:**
```json
{
  "nombre": "Salario Mensual",
  "descripcion": "Salario trabajo principal",
  "tipoFuente": "SALARY",
  "esIngresoReal": true,
  "activa": true
}
```

**Tipos de Fuente:**
- `SALARY`: Salario
- `FREELANCE`: Trabajo independiente
- `INVESTMENT`: Inversiones
- `SCHOLARSHIP`: Beca
- `SUBSIDY`: Subsidio
- `LOAN`: Préstamo
- `OTHER`: Otro

**Response (201):**
```json
{
  "id": 1,
  "usuarioId": 1,
  "nombre": "Salario Mensual",
  "descripcion": "Salario trabajo principal",
  "tipoFuente": "SALARY",
  "esIngresoReal": true,
  "activa": true,
  "createdAt": "2026-01-11T22:00:00",
  "updatedAt": "2026-01-11T22:00:00"
}
```

---

### 17. Obtener Mis Fuentes de Ingreso
**GET** `/api/fuentes-ingreso`

**Response (200):** Lista de fuentes de ingreso del usuario autenticado

---

### 18. Obtener Fuente de Ingreso por ID
**GET** `/api/fuentes-ingreso/{id}`

---

### 19. Actualizar Fuente de Ingreso
**PUT** `/api/fuentes-ingreso/{id}`

---

### 20. Eliminar Fuente de Ingreso
**DELETE** `/api/fuentes-ingreso/{id}`

**Errores:**
- **409:** No se puede eliminar porque tiene movimientos asociados

---

## 🔄 Movimientos Recurrentes

### 21. Crear Movimiento Recurrente
**POST** `/api/movimientos-recurrentes`

**Request Body:**
```json
{
  "nombre": "Arriendo",
  "descripcion": "Pago mensual de arriendo",
  "tipoMovimiento": "EXPENSE",
  "monto": 800000,
  "categoriaId": 2,
  "fuenteIngresoId": null,
  "metaId": null,
  "frecuencia": "MONTHLY",
  "diaEjecucion": 5,
  "proximaEjecucion": "2026-02-05",
  "activo": true
}
```

**Frecuencias disponibles:**
- `DAILY`: Diario
- `WEEKLY`: Semanal
- `BIWEEKLY`: Quincenal
- `MONTHLY`: Mensual
- `QUARTERLY`: Trimestral
- `YEARLY`: Anual

**Response (201):**
```json
{
  "id": 1,
  "usuarioId": 1,
  "nombre": "Arriendo",
  "tipoMovimiento": "EXPENSE",
  "monto": 800000.00,
  "frecuencia": "MONTHLY",
  "diaEjecucion": 5,
  "proximaEjecucion": "2026-02-05",
  "activo": true,
  "createdAt": "2026-01-16T10:00:00"
}
```

---

### 22. Obtener Mis Movimientos Recurrentes
**GET** `/api/movimientos-recurrentes`

---

### 23. Obtener Movimiento Recurrente por ID
**GET** `/api/movimientos-recurrentes/{id}`

---

### 24. Activar/Desactivar Movimiento Recurrente
**PATCH** `/api/movimientos-recurrentes/{id}/toggle`

---

### 25. Eliminar Movimiento Recurrente
**DELETE** `/api/movimientos-recurrentes/{id}`

---

## 💰 Presupuestos

### 26. Crear Presupuesto
**POST** `/api/presupuestos`

**Request Body:**
```json
{
  "categoriaId": 1,
  "montoLimite": 500000,
  "periodo": "MONTHLY",
  "fechaInicio": "2026-01-01",
  "fechaFin": "2026-12-31",
  "alertaEn": 80,
  "activo": true
}
```

**Periodos disponibles:**
- `DAILY`: Diario
- `WEEKLY`: Semanal
- `MONTHLY`: Mensual
- `YEARLY`: Anual

**Response (201):**
```json
{
  "id": 1,
  "usuarioId": 1,
  "categoriaId": 1,
  "categoriaNombre": "Gastos Personales",
  "montoLimite": 500000.00,
  "montoGastado": 0.00,
  "porcentajeUsado": 0.0,
  "periodo": "MONTHLY",
  "fechaInicio": "2026-01-01",
  "fechaFin": "2026-12-31",
  "alertaEn": 80,
  "activo": true,
  "excedido": false
}
```

---

### 27. Obtener Mis Presupuestos
**GET** `/api/presupuestos`

**Query Params (opcionales):**
- `activos`: boolean (true para solo activos)

---

### 28. Obtener Presupuesto por ID
**GET** `/api/presupuestos/{id}`

---

### 29. Actualizar Presupuesto
**PUT** `/api/presupuestos/{id}`

---

### 30. Eliminar Presupuesto
**DELETE** `/api/presupuestos/{id}`

---

## 📊 Reportes y Exportación

### 31. Exportar Reporte a PDF
**GET** `/api/reportes/export/pdf`

**Query Params:**
- `startDate`: YYYY-MM-DD (requerido)
- `endDate`: YYYY-MM-DD (requerido)

**Ejemplo:**
```
GET /api/reportes/export/pdf?startDate=2026-01-01&endDate=2026-01-31
```

**Response (200):**
- Content-Type: `application/pdf`
- Content-Disposition: `attachment; filename="reporte-financiero-2026-01.pdf"`

**Contenido del PDF:**
- Resumen de ingresos, gastos y balance
- Gráfico de distribución por categorías
- Listado detallado de movimientos
- Estado de metas financieras
- Análisis de tendencias

---

### 32. Exportar Reporte a Excel
**GET** `/api/reportes/export/excel`

**Query Params:**
- `startDate`: YYYY-MM-DD (requerido)
- `endDate`: YYYY-MM-DD (requerido)

**Response (200):**
- Content-Type: `application/vnd.openxmlformats-officedocument.spreadsheetml.sheet`
- Content-Disposition: `attachment; filename="reporte-financiero-2026-01.xlsx"`

**Contenido del Excel:**
- Hoja "Resumen": Totales y métricas clave
- Hoja "Movimientos": Listado completo con filtros
- Hoja "Categorías": Gastos por categoría
- Hoja "Metas": Progreso de metas financieras
- Formato profesional con colores y tablas automáticas

---

### 33. Enviar Reporte por Email
**POST** `/api/reportes/send-email`

**Request Body:**
```json
{
  "email": "usuario@example.com",
  "startDate": "2026-01-01",
  "endDate": "2026-01-31",
  "format": "PDF"
}
```

**Formatos disponibles:**
- `PDF`: Documento PDF
- `EXCEL`: Archivo Excel

**Response (200):**
```json
{
  "message": "Reporte enviado exitosamente a usuario@example.com",
  "emailSent": true
}
```

---

## 🧠 Inteligencia Financiera

### 34. Obtener Predicción de Gastos
**GET** `/api/inteligencia/prediccion-gastos`

**Query Params (opcionales):**
- `meses`: int (número de meses históricos a analizar, default: 6)

**Response (200):**
```json
{
  "promedioMensualHistorico": 1250000.00,
  "gastoActualMes": 800000.00,
  "proyeccionFinMes": 1400000.00,
  "diasTranscurridos": 16,
  "diasTotalesMes": 31,
  "mensaje": "Proyectas gastar $1,400,000 este mes. Tu promedio histórico es $1,250,000.",
  "alertaNivel": "MEDIUM",
  "recomendaciones": [
    "Estás gastando más que tu promedio histórico",
    "Considera revisar gastos en 'Ocio' y 'Restaurantes'"
  ]
}
```

---

### 35. Detectar Anomalías
**GET** `/api/inteligencia/anomalias`

**Query Params (opcionales):**
- `meses`: int (número de meses a analizar, default: 3)

**Response (200):**
```json
{
  "anomaliasDetectadas": [
    {
      "movimientoId": 45,
      "descripcion": "Compra electrónica",
      "monto": 2500000.00,
      "fecha": "2026-01-10",
      "categoriaId": 3,
      "categoriaNombre": "Tecnología",
      "desviacionEstandar": 3.2,
      "nivelAlerta": "HIGH",
      "mensaje": "Este gasto es inusualmente alto para tu categoría 'Tecnología'. Normalmente gastas $300,000."
    }
  ],
  "totalAnomalias": 1
}
```

---

### 36. Obtener Recomendaciones Personalizadas
**GET** `/api/inteligencia/recomendaciones`

**Response (200):**
```json
{
  "recomendaciones": [
    {
      "tipo": "AHORRO",
      "prioridad": "HIGH",
      "titulo": "Oportunidad de ahorro detectada",
      "mensaje": "Podrías ahorrar $200,000 este mes reduciendo gastos en 'Ocio'",
      "categoriaAfectada": "Ocio",
      "impactoEstimado": 200000.00
    },
    {
      "tipo": "PRESUPUESTO",
      "prioridad": "MEDIUM",
      "titulo": "Presupuesto cerca del límite",
      "mensaje": "Has usado el 85% de tu presupuesto de 'Gastos Personales'",
      "categoriaAfectada": "Gastos Personales",
      "porcentajeUsado": 85.0
    }
  ],
  "totalRecomendaciones": 2
}
```

---

## 🔔 Notificaciones

### 37. Obtener Mis Notificaciones
**GET** `/api/notificaciones`

**Query Params (opcionales):**
- `leidas`: boolean (filtrar por leídas/no leídas)
- `tipo`: PRESUPUESTO_WARNING | PRESUPUESTO_ALERT | MOVIMIENTO_AUTOMATICO | RESUMEN_MENSUAL | META_ALCANZADA

**Response (200):**
```json
[
  {
    "id": 1,
    "tipo": "PRESUPUESTO_ALERT",
    "titulo": "⚠️ Presupuesto excedido",
    "mensaje": "Has superado el presupuesto de 'Gastos Personales' en un 15%",
    "leida": false,
    "importante": true,
    "fechaCreacion": "2026-01-16T14:30:00",
    "entidadRelacionadaId": 1,
    "entidadRelacionadaTipo": "PRESUPUESTO"
  }
]
```

---

### 38. Marcar Notificación como Leída
**PATCH** `/api/notificaciones/{id}/leer`

**Response (200):**
```json
{
  "message": "Notificación marcada como leída"
}
```

---

### 39. Marcar Todas como Leídas
**PATCH** `/api/notificaciones/leer-todas`

---

### 40. Eliminar Notificación
**DELETE** `/api/notificaciones/{id}`

---

## 📝 Auditoría

### 41. Obtener Historial de Auditoría
**GET** `/api/auditoria`

**Query Params (opcionales):**
- `tipoEntidad`: MOVIMIENTO | CATEGORIA | META | FUENTE_INGRESO | PRESUPUESTO
- `accion`: CREATE | UPDATE | DELETE
- `fechaDesde`: YYYY-MM-DD
- `fechaHasta`: YYYY-MM-DD
- `page`: int (página, default: 0)
- `size`: int (tamaño, default: 20)

**Ejemplo:**
```
GET /api/auditoria?tipoEntidad=MOVIMIENTO&accion=DELETE&page=0&size=10
```

**Response (200):**
```json
{
  "content": [
    {
      "id": 1,
      "usuarioId": 1,
      "usuarioNombre": "Snayber Dev",
      "tipoEntidad": "MOVIMIENTO",
      "entidadId": 45,
      "accion": "DELETE",
      "descripcion": "Eliminó movimiento: Compra supermercado - $150,000",
      "ipAddress": "192.168.1.100",
      "userAgent": "Mozilla/5.0...",
      "timestamp": "2026-01-16T10:30:00"
    }
  ],
  "totalElements": 50,
  "totalPages": 5,
  "currentPage": 0,
  "pageSize": 10
}
```

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

