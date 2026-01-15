# 📚 API Control Financiero - Endpoints Completos

## 🌐 Base URL
- **Producción**: `https://api-control-financiero.onrender.com`
- **Local**: `http://localhost:8080`

---

## 👤 Usuarios

### Crear Usuario
```http
POST /api/usuarios
Content-Type: application/json

{
  "username": "snayber",
  "email": "snayber@example.com",
  "password": "123456",
  "fullName": "Snayber Dev"
}
```

**Respuesta (201 Created):**
```json
{
  "id": 1,
  "username": "snayber",
  "email": "snayber@example.com",
  "fullName": "Snayber Dev",
  "active": true,
  "createdAt": "2026-01-11T21:18:58.863Z"
}
```

### Obtener Usuario por ID
```http
GET /api/usuarios/{id}
```

**Respuesta (200 OK):**
```json
{
  "id": 1,
  "username": "snayber",
  "email": "snayber@example.com",
  "fullName": "Snayber Dev",
  "active": true,
  "createdAt": "2026-01-11T21:18:58.863Z"
}
```

---

## 💼 Contexto Financiero

### Crear Contexto Financiero
```http
POST /api/contextos-financieros
Content-Type: application/json

{
  "usuarioId": 1,
  "tipoIngreso": "MONTHLY",
  "tieneIngresoVariable": false,
  "porcentajeAhorroDeseado": 20.0,
  "periodoAnalisis": "MONTHLY",
  "diasPeriodoPersonalizado": null
}
```

**Valores permitidos:**
- **tipoIngreso**: `MONTHLY`, `BIWEEKLY`, `WEEKLY`, `PROJECT_BASED`, `VARIABLE`
- **periodoAnalisis**: `MONTHLY`, `BIWEEKLY`, `CUSTOM`

**Respuesta (201 Created):**
```json
{
  "id": 1,
  "usuarioId": 1,
  "tipoIngreso": "MONTHLY",
  "tieneIngresoVariable": false,
  "porcentajeAhorroDeseado": 20,
  "periodoAnalisis": "MONTHLY",
  "diasPeriodoPersonalizado": null,
  "codigoMoneda": "COP",
  "createdAt": "2026-01-06T21:48:16.3819555",
  "updatedAt": "2026-01-06T21:48:16.3819555"
}
```

### Obtener Contexto por Usuario
```http
GET /api/contextos-financieros/usuario/{usuarioId}
```

### Obtener Contexto por ID
```http
GET /api/contextos-financieros/{id}
```

---

## 🏷️ Categorías

### Crear Categoría
```http
POST /api/categorias
Content-Type: application/json

{
  "usuarioId": 1,
  "nombre": "Gastos Personales",
  "descripcion": "Gastos del dia a dia",
  "colorHex": "#FF5733",
  "icono": "wallet",
  "tipoCategoria": "EXPENSE"
}
```

**Valores permitidos:**
- **tipoCategoria**: `EXPENSE`, `SAVINGS`, `INVESTMENT`, `DEBT`

**Respuesta (201 Created):**
```json
{
  "id": 1,
  "usuarioId": 1,
  "nombre": "Gastos Personales",
  "descripcion": "Gastos del dia a dia",
  "colorHex": "#FF5733",
  "icono": "wallet",
  "tipoCategoria": "EXPENSE",
  "activa": true,
  "createdAt": "2026-01-06T22:39:36.9782118",
  "updatedAt": "2026-01-06T22:39:36.9782118"
}
```

### Obtener Categoría por ID
```http
GET /api/categorias/{id}
```

### Obtener Categorías por Usuario
```http
GET /api/categorias/usuario/{usuarioId}
```

### Obtener Categorías Activas por Usuario
```http
GET /api/categorias/usuario/{usuarioId}/activas
```

**Respuesta (200 OK):**
```json
[
  {
    "id": 1,
    "usuarioId": 1,
    "nombre": "Gastos Personales",
    "descripcion": "Gastos del dia a dia",
    "colorHex": "#FF5733",
    "icono": "wallet",
    "tipoCategoria": "EXPENSE",
    "activa": true,
    "createdAt": "2026-01-06T22:39:36.9782118",
    "updatedAt": "2026-01-06T22:39:36.9782118"
  }
]
```

---

## 💰 Fuentes de Ingreso

### Crear Fuente de Ingreso
```http
POST /api/fuentes-ingreso
Content-Type: application/json

{
  "usuarioId": 1,
  "nombre": "Salario Mensual",
  "descripcion": "Salario trabajo principal",
  "tipoFuente": "SALARY",
  "esIngresoReal": true,
  "activa": true
}
```

**Valores permitidos:**
- **tipoFuente**: `SALARY`, `FREELANCE`, `LOAN`, `SCHOLARSHIP`, `SUBSIDY`, `INVESTMENT`, `OTHER`

**Respuesta (201 Created):**
```json
{
  "id": 1,
  "usuarioId": 1,
  "nombre": "Salario Mensual",
  "descripcion": "Salario trabajo principal",
  "tipoFuente": "SALARY",
  "esIngresoReal": true,
  "activa": true,
  "createdAt": "2026-01-11T21:44:05.1600408",
  "updatedAt": "2026-01-11T21:44:05.1600408"
}
```

### Obtener Fuentes de Ingreso por Usuario
```http
GET /api/fuentes-ingreso/usuario/{usuarioId}?soloActivas=false
```

**Query Parameters:**
- `soloActivas` (opcional): `true` o `false` (default: `false`)

**Respuesta (200 OK):**
```json
[
  {
    "id": 1,
    "usuarioId": 1,
    "nombre": "Salario Mensual",
    "descripcion": "Salario trabajo principal",
    "tipoFuente": "SALARY",
    "esIngresoReal": true,
    "activa": true,
    "createdAt": "2026-01-11T21:44:05.1600408",
    "updatedAt": "2026-01-11T21:44:05.1600408"
  }
]
```

---

## 🎯 Metas Financieras

### Crear Meta Financiera
```http
POST /api/metas
Content-Type: application/json

{
  "usuarioId": 1,
  "nombre": "Comprar Moto XR 190L",
  "descripcion": "Moto para transporte",
  "montoObjetivo": 15000000,
  "fechaObjetivo": "2026-12-31",
  "prioridad": "HIGH"
}
```

**Valores permitidos:**
- **prioridad**: `LOW`, `MEDIUM`, `HIGH`, `CRITICAL`

**Respuesta (201 Created):**
```json
{
  "id": 1,
  "usuarioId": 1,
  "nombre": "Comprar Moto XR 190L",
  "descripcion": "Moto para transporte",
  "montoObjetivo": 15000000,
  "montoActual": 0,
  "fechaObjetivo": "2026-12-31",
  "prioridad": "HIGH",
  "estado": "ACTIVE",
  "createdAt": "2026-01-11T21:44:30.123Z",
  "updatedAt": "2026-01-11T21:44:30.123Z",
  "completedAt": null
}
```

### Obtener Metas por Usuario
```http
GET /api/metas/usuario/{usuarioId}?estado=ACTIVE
```

**Query Parameters:**
- `estado` (opcional): `ACTIVE`, `COMPLETED`, `CANCELLED`, `PAUSED`

**Respuesta (200 OK):**
```json
[
  {
    "id": 1,
    "usuarioId": 1,
    "nombre": "Comprar Moto XR 190L",
    "descripcion": "Moto para transporte",
    "montoObjetivo": 15000000,
    "montoActual": 0,
    "fechaObjetivo": "2026-12-31",
    "prioridad": "HIGH",
    "estado": "ACTIVE",
    "createdAt": "2026-01-11T21:44:30.123Z",
    "updatedAt": "2026-01-11T21:44:30.123Z",
    "completedAt": null
  }
]
```

---

## 💸 Movimientos Financieros

### Crear Movimiento Financiero (Ingreso)
```http
POST /api/movimientos
Content-Type: application/json

{
  "usuarioId": 1,
  "tipoMovimiento": "INCOME",
  "monto": 3000000,
  "descripcion": "Pago de salario Enero 2026",
  "fechaMovimiento": "2026-01-11",
  "categoriaId": null,
  "fuenteIngresoId": 1,
  "metaId": null,
  "esRecurrente": true,
  "patronRecurrencia": "MENSUAL",
  "notas": "Primer pago del año"
}
```

### Crear Movimiento Financiero (Gasto)
```http
POST /api/movimientos
Content-Type: application/json

{
  "usuarioId": 1,
  "tipoMovimiento": "EXPENSE",
  "monto": 150000,
  "descripcion": "Salida con la novia - Cine y cena",
  "fechaMovimiento": "2026-01-11",
  "categoriaId": 1,
  "fuenteIngresoId": null,
  "metaId": null,
  "esRecurrente": false,
  "patronRecurrencia": null,
  "notas": "Fecha romántica"
}
```

### Crear Movimiento Financiero (Ahorro)
```http
POST /api/movimientos
Content-Type: application/json

{
  "usuarioId": 1,
  "tipoMovimiento": "SAVINGS",
  "monto": 500000,
  "descripcion": "Ahorro para la moto",
  "fechaMovimiento": "2026-01-11",
  "categoriaId": null,
  "fuenteIngresoId": null,
  "metaId": 1,
  "esRecurrente": true,
  "patronRecurrencia": "MENSUAL",
  "notas": "Aporte mensual para la XR 190L"
}
```

**Valores permitidos:**
- **tipoMovimiento**: `INCOME`, `EXPENSE`, `SAVINGS`, `LOAN`, `TRANSFER`

**Respuesta (201 Created):**
```json
{
  "id": 1,
  "usuarioId": 1,
  "tipoMovimiento": "INCOME",
  "monto": 3000000.00,
  "descripcion": "Pago de salario Enero 2026",
  "fechaMovimiento": "2026-01-11",
  "categoriaId": null,
  "categoriaNombre": null,
  "fuenteIngresoId": 1,
  "fuenteIngresoNombre": "Salario Mensual",
  "metaId": null,
  "metaNombre": null,
  "esRecurrente": true,
  "patronRecurrencia": "MENSUAL",
  "notas": "Primer pago del año",
  "createdAt": "2026-01-11T22:20:15.123Z",
  "updatedAt": "2026-01-11T22:20:15.123Z"
}
```

### Obtener Movimientos por Usuario
```http
GET /api/movimientos/usuario/{usuarioId}
```

### Filtrar Movimientos por Tipo
```http
GET /api/movimientos/usuario/{usuarioId}?tipo=EXPENSE
```

**Query Parameters:**
- `tipo` (opcional): `INCOME`, `EXPENSE`, `SAVINGS`, `LOAN`, `TRANSFER`

### Filtrar Movimientos por Rango de Fechas
```http
GET /api/movimientos/usuario/{usuarioId}?fechaInicio=2026-01-01&fechaFin=2026-01-31
```

**Query Parameters:**
- `fechaInicio` (formato: YYYY-MM-DD)
- `fechaFin` (formato: YYYY-MM-DD)

### Filtrar Movimientos por Categoría
```http
GET /api/movimientos/usuario/{usuarioId}?categoriaId=1
```

**Query Parameters:**
- `categoriaId`: ID de la categoría

**Respuesta (200 OK):**
```json
[
  {
    "id": 1,
    "usuarioId": 1,
    "tipoMovimiento": "INCOME",
    "monto": 3000000.00,
    "descripcion": "Pago de salario Enero 2026",
    "fechaMovimiento": "2026-01-11",
    "categoriaId": null,
    "categoriaNombre": null,
    "fuenteIngresoId": 1,
    "fuenteIngresoNombre": "Salario Mensual",
    "metaId": null,
    "metaNombre": null,
    "esRecurrente": true,
    "patronRecurrencia": "MENSUAL",
    "notas": "Primer pago del año",
    "createdAt": "2026-01-11T22:20:15.123Z",
    "updatedAt": "2026-01-11T22:20:15.123Z"
  },
  {
    "id": 2,
    "usuarioId": 1,
    "tipoMovimiento": "EXPENSE",
    "monto": 150000.00,
    "descripcion": "Salida con la novia - Cine y cena",
    "fechaMovimiento": "2026-01-11",
    "categoriaId": 1,
    "categoriaNombre": "Gastos Personales",
    "fuenteIngresoId": null,
    "fuenteIngresoNombre": null,
    "metaId": null,
    "metaNombre": null,
    "esRecurrente": false,
    "patronRecurrencia": null,
    "notas": "Fecha romántica",
    "createdAt": "2026-01-11T22:21:30.456Z",
    "updatedAt": "2026-01-11T22:21:30.456Z"
  }
]
```

---

## 📊 Ejemplos de Uso con React

### Configuración de Axios
```javascript
import axios from 'axios';

const api = axios.create({
  baseURL: 'https://api-control-financiero.onrender.com/api',
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000,
});

export default api;
```

### Crear Usuario
```javascript
const crearUsuario = async (userData) => {
  try {
    const response = await api.post('/usuarios', userData);
    return response.data;
  } catch (error) {
    console.error('Error al crear usuario:', error);
    throw error;
  }
};
```

### Obtener Movimientos con Filtros
```javascript
const obtenerMovimientos = async (usuarioId, filtros = {}) => {
  try {
    const params = new URLSearchParams();
    
    if (filtros.tipo) params.append('tipo', filtros.tipo);
    if (filtros.fechaInicio) params.append('fechaInicio', filtros.fechaInicio);
    if (filtros.fechaFin) params.append('fechaFin', filtros.fechaFin);
    if (filtros.categoriaId) params.append('categoriaId', filtros.categoriaId);
    
    const response = await api.get(
      `/movimientos/usuario/${usuarioId}?${params.toString()}`
    );
    return response.data;
  } catch (error) {
    console.error('Error al obtener movimientos:', error);
    throw error;
  }
};
```

### Hook de React Personalizado
```javascript
import { useState, useEffect } from 'react';
import api from './api';

export const useMovimientos = (usuarioId, filtros = {}) => {
  const [movimientos, setMovimientos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchMovimientos = async () => {
      try {
        setLoading(true);
        const params = new URLSearchParams(filtros).toString();
        const response = await api.get(
          `/movimientos/usuario/${usuarioId}?${params}`
        );
        setMovimientos(response.data);
        setError(null);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchMovimientos();
  }, [usuarioId, JSON.stringify(filtros)]);

  return { movimientos, loading, error };
};
```

---

## 🔐 Manejo de Errores

Todos los endpoints retornan códigos de estado HTTP estándar:

- **200 OK**: Operación exitosa (GET)
- **201 Created**: Recurso creado exitosamente (POST)
- **400 Bad Request**: Datos de entrada inválidos
- **404 Not Found**: Recurso no encontrado
- **500 Internal Server Error**: Error del servidor

### Formato de Error
```json
{
  "status": 500,
  "message": "Descripción del error",
  "timestamp": "2026-01-11T22:30:00.000Z"
}
```

---

## 📝 Notas Importantes

1. **CORS está configurado** para permitir peticiones desde:
   - `http://localhost:3000` (React dev)
   - `http://localhost:5173` (Vite dev)
   - `https://*.vercel.app`
   - `https://*.netlify.app`
   - `https://*.onrender.com`

2. **Todas las fechas** deben usar formato ISO 8601: `YYYY-MM-DD`

3. **Montos** son números decimales (pueden incluir centavos)

4. **IDs** son números enteros (Long en Java)

5. **La API está en producción** en: https://api-control-financiero.onrender.com

---

## 🚀 Testing con cURL

### Crear Usuario
```bash
curl -X POST https://api-control-financiero.onrender.com/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "123456",
    "fullName": "Test User"
  }'
```

### Obtener Movimientos
```bash
curl -X GET "https://api-control-financiero.onrender.com/api/movimientos/usuario/1?tipo=EXPENSE"
```

---

**✅ API Lista para usar con React!**

