# 📚 API Control Financiero - Endpoints Completos

## 🌐 Base URL
- **Producción**: `https://api-control-financiero.onrender.com`
- **Local**: `http://localhost:8080`

---

## 🔥 Keep-Alive & Monitoreo

### Health Check
```http
GET /api/health
```

**Respuesta (200 OK):**
```json
{
  "status": "UP",
  "timestamp": "2026-01-22T10:30:45.123Z",
  "database": "connected",
  "uptime_seconds": 3600
}
```

### Ping (Ultra-Ligero)
```http
GET /api/ping
```

**Respuesta (200 OK):**
```json
{
  "status": "alive",
  "timestamp": "2026-01-22T10:30:45.123Z",
  "uptime_seconds": 3600
}
```

> **💡 Nota:** Usa estos endpoints para mantener el servicio activo en Render Free Tier.  
> Ver [KEEP-ALIVE-STRATEGY.md](./KEEP-ALIVE-STRATEGY.md) para implementación completa.

---

## 🔐 Autenticación

### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "snayber",
  "password": "123456"
}
```

**Respuesta (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "id": 1,
  "username": "snayber",
  "email": "snayber@example.com",
  "fullName": "Snayber Dev"
}
```

### Registro
```http
POST /api/auth/register
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
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "id": 1,
  "username": "snayber",
  "email": "snayber@example.com",
  "fullName": "Snayber Dev"
}
```

**⚠️ Nota**: Después del login/registro, incluye el token en las peticiones:
```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

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

## 📄 Reportes y Exportación

### Generar PDF (Estado de Cuenta)
```http
GET /api/reportes/pdf?fechaInicio=2026-01-01&fechaFin=2026-01-31
Authorization: Bearer {token}
```

**Query Parameters:**
- `fechaInicio` (requerido): Fecha inicial en formato YYYY-MM-DD
- `fechaFin` (requerido): Fecha final en formato YYYY-MM-DD

**Respuesta (200 OK):**
- **Content-Type**: `application/pdf`
- **Filename**: `estado-cuenta-{fechaInicio}-{fechaFin}.pdf`
- Retorna un archivo PDF con el estado de cuenta del período

**Características del PDF:**
- ✅ Gráfico de ingresos vs gastos
- ✅ Lista detallada de todos los movimientos
- ✅ Totales y balance del período
- ✅ Formato profesional con colores

### Generar Excel
```http
GET /api/reportes/excel?fechaInicio=2026-01-01&fechaFin=2026-01-31
Authorization: Bearer {token}
```

**Query Parameters:**
- `fechaInicio` (requerido): Fecha inicial en formato YYYY-MM-DD
- `fechaFin` (requerido): Fecha final en formato YYYY-MM-DD

**Respuesta (200 OK):**
- **Content-Type**: `application/vnd.openxmlformats-officedocument.spreadsheetml.sheet`
- **Filename**: `reporte-financiero-{fechaInicio}-{fechaFin}.xlsx`
- Retorna un archivo Excel con el reporte financiero

**Características del Excel:**
- ✅ Hoja de movimientos con todos los detalles
- ✅ Fórmulas automáticas para totales
- ✅ Formato profesional con colores
- ✅ Fácil de analizar en Excel/Google Sheets

---

## 🧠 Inteligencia Financiera

### Predicción de Gastos
```http
GET /api/inteligencia/prediccion
Authorization: Bearer {token}
```

**Qué hace:**
- Analiza tus últimos 6 meses de gastos
- Calcula el promedio histórico
- Proyecta cuánto gastarás este mes basado en tu ritmo actual

**Respuesta (200 OK):**
```json
{
  "promedioMensualHistorico": 2500000,
  "gastoActualMes": 1800000,
  "proyeccionFinMes": 3200000,
  "diasTranscurridos": 20,
  "diasTotalesMes": 31,
  "mensaje": "Proyectas gastar $3,200,000 este mes. Tu promedio histórico es $2,500,000"
}
```

### Detección de Anomalías
```http
GET /api/inteligencia/anomalias
Authorization: Bearer {token}
```

**Qué hace:**
- Detecta gastos inusualmente altos usando algoritmos estadísticos (desviaciones estándar)
- Te alerta sobre movimientos sospechosos o extraordinarios

**Ejemplo:** Si normalmente gastas $50,000 en comida y de repente gastas $300,000, lo detecta.

**Respuesta (200 OK):**
```json
[
  {
    "id": 45,
    "usuarioId": 1,
    "tipoMovimiento": "EXPENSE",
    "monto": 300000,
    "descripcion": "Cena romántica especial",
    "fechaMovimiento": "2026-01-15",
    "categoriaId": 2,
    "categoriaNombre": "Alimentación",
    "esAnomalia": true
  }
]
```

### Recomendaciones Personalizadas
```http
GET /api/inteligencia/recomendaciones
Authorization: Bearer {token}
```

**Qué hace:**
- Analiza tu comportamiento financiero
- Te da consejos personalizados para ayudarte a ahorrar

**Respuesta (200 OK):**
```json
{
  "recomendaciones": [
    "⚠️ Tus gastos proyectados ($3,200,000) superan tu promedio histórico ($2,500,000). Considera revisar tus gastos.",
    "🚨 Detectamos gastos inusualmente altos en los últimos 3 meses. Revisa si son necesarios.",
    "💰 Podrías ahorrar $700,000 este mes reduciendo gastos en Ocio ($200,000) y Alimentación ($500,000)."
  ],
  "total": 3
}
```

### Dashboard de Inteligencia Completo
```http
GET /api/inteligencia/dashboard
Authorization: Bearer {token}
```

**Qué incluye:**
- Predicciones de gastos
- Anomalías detectadas
- Recomendaciones personalizadas
- Análisis completo

**Respuesta (200 OK):**
```json
{
  "prediccion": {
    "promedioMensualHistorico": 2500000,
    "gastoActualMes": 1800000,
    "proyeccionFinMes": 3200000,
    "diasTranscurridos": 20,
    "diasTotalesMes": 31,
    "mensaje": "Proyectas gastar $3,200,000 este mes. Tu promedio histórico es $2,500,000"
  },
  "anomalias": [
    {
      "id": 45,
      "monto": 300000,
      "descripcion": "Cena romántica especial",
      "fechaMovimiento": "2026-01-15"
    }
  ],
  "recomendaciones": [
    "⚠️ Tus gastos proyectados superan tu promedio histórico. Considera revisar tus gastos.",
    "💰 Podrías ahorrar $700,000 este mes reduciendo gastos en Ocio y Alimentación."
  ]
}
```

---

## 🔔 Notificaciones

### Obtener Todas las Notificaciones
```http
GET /api/notificaciones
Authorization: Bearer {token}
```

**Respuesta (200 OK):**
```json
[
  {
    "id": 1,
    "usuarioId": 1,
    "tipoNotificacion": "PRESUPUESTO_ALERTA",
    "titulo": "⚠️ Alerta de Presupuesto",
    "mensaje": "Estás cerca del límite de tu presupuesto en Ocio (80% usado)",
    "leida": false,
    "createdAt": "2026-01-15T10:30:00"
  },
  {
    "id": 2,
    "usuarioId": 1,
    "tipoNotificacion": "META_ALCANZADA",
    "titulo": "🎉 Meta Completada",
    "mensaje": "¡Felicidades! Has alcanzado tu meta 'Comprar Moto XR 190L'",
    "leida": true,
    "createdAt": "2026-01-14T18:20:00"
  }
]
```

**Tipos de Notificaciones:**
- `PRESUPUESTO_WARNING`: Se envía cuando gastas el 80% del presupuesto
- `PRESUPUESTO_ALERTA`: Se envía cuando excedes el presupuesto
- `MOVIMIENTO_AUTOMATICO`: Se envía cuando se ejecuta un movimiento recurrente automáticamente
- `RESUMEN_MENSUAL`: Se envía cada mes con tu resumen financiero
- `META_ALCANZADA`: Se envía cuando alcanzas una meta financiera

### Obtener Notificaciones No Leídas
```http
GET /api/notificaciones/no-leidas
Authorization: Bearer {token}
```

**Respuesta (200 OK):**
```json
[
  {
    "id": 1,
    "usuarioId": 1,
    "tipoNotificacion": "PRESUPUESTO_ALERTA",
    "titulo": "⚠️ Alerta de Presupuesto",
    "mensaje": "Estás cerca del límite de tu presupuesto en Ocio (80% usado)",
    "leida": false,
    "createdAt": "2026-01-15T10:30:00"
  }
]
```

### Obtener Contador de No Leídas
```http
GET /api/notificaciones/contador
Authorization: Bearer {token}
```

**Respuesta (200 OK):**
```json
{
  "noLeidas": 5
}
```

### Marcar Notificación como Leída
```http
PUT /api/notificaciones/{id}/marcar-leida
Authorization: Bearer {token}
```

**Respuesta (200 OK):**
```json
{
  "id": 1,
  "usuarioId": 1,
  "tipoNotificacion": "PRESUPUESTO_ALERTA",
  "titulo": "⚠️ Alerta de Presupuesto",
  "mensaje": "Estás cerca del límite de tu presupuesto en Ocio (80% usado)",
  "leida": true,
  "createdAt": "2026-01-15T10:30:00"
}
```

### Marcar Todas como Leídas
```http
PUT /api/notificaciones/marcar-todas-leidas
Authorization: Bearer {token}
```

**Respuesta (200 OK):**
```json
{
  "mensaje": "5 notificaciones marcadas como leídas"
}
```

### Eliminar Notificación
```http
DELETE /api/notificaciones/{id}
Authorization: Bearer {token}
```

**Respuesta (200 OK):**
```json
{
  "mensaje": "Notificación eliminada"
}
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

### Descargar PDF
```javascript
const descargarPDF = async (fechaInicio, fechaFin) => {
  try {
    const response = await api.get('/reportes/pdf', {
      params: { fechaInicio, fechaFin },
      responseType: 'blob', // Importante para archivos binarios
    });
    
    // Crear un enlace de descarga
    const url = window.URL.createObjectURL(new Blob([response.data]));
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', `estado-cuenta-${fechaInicio}-${fechaFin}.pdf`);
    document.body.appendChild(link);
    link.click();
    link.remove();
  } catch (error) {
    console.error('Error al descargar PDF:', error);
    throw error;
  }
};
```

### Obtener Dashboard de Inteligencia
```javascript
const obtenerDashboard = async () => {
  try {
    const response = await api.get('/inteligencia/dashboard');
    return response.data;
  } catch (error) {
    console.error('Error al obtener dashboard:', error);
    throw error;
  }
};

// Hook personalizado
export const useDashboardInteligencia = () => {
  const [dashboard, setDashboard] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchDashboard = async () => {
      try {
        setLoading(true);
        const data = await obtenerDashboard();
        setDashboard(data);
      } catch (error) {
        console.error(error);
      } finally {
        setLoading(false);
      }
    };

    fetchDashboard();
  }, []);

  return { dashboard, loading };
};
```

### Gestionar Notificaciones
```javascript
// Obtener notificaciones no leídas
const obtenerNoLeidas = async () => {
  try {
    const response = await api.get('/notificaciones/no-leidas');
    return response.data;
  } catch (error) {
    console.error('Error al obtener notificaciones:', error);
    throw error;
  }
};

// Marcar como leída
const marcarLeida = async (id) => {
  try {
    const response = await api.put(`/notificaciones/${id}/marcar-leida`);
    return response.data;
  } catch (error) {
    console.error('Error al marcar notificación:', error);
    throw error;
  }
};

// Hook personalizado para notificaciones
export const useNotificaciones = () => {
  const [notificaciones, setNotificaciones] = useState([]);
  const [contador, setContador] = useState(0);
  const [loading, setLoading] = useState(true);

  const fetchNotificaciones = async () => {
    try {
      setLoading(true);
      const [noLeidas, count] = await Promise.all([
        api.get('/notificaciones/no-leidas'),
        api.get('/notificaciones/contador')
      ]);
      setNotificaciones(noLeidas.data);
      setContador(count.data.noLeidas);
    } catch (error) {
      console.error('Error al obtener notificaciones:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchNotificaciones();
  }, []);

  const marcarComoLeida = async (id) => {
    await marcarLeida(id);
    fetchNotificaciones(); // Actualizar lista
  };

  const marcarTodasLeidas = async () => {
    await api.put('/notificaciones/marcar-todas-leidas');
    fetchNotificaciones(); // Actualizar lista
  };

  return { 
    notificaciones, 
    contador, 
    loading, 
    marcarComoLeida,
    marcarTodasLeidas,
    refetch: fetchNotificaciones
  };
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

