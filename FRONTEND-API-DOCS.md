# 📱 MetaFy API v1.6.0 - Documentación para Frontend

**Base URL:** `https://tu-api.render.com/api`  
**Autenticación:** Todos los endpoints requieren `Bearer Token` (excepto `/auth/login` y `/auth/register`)  
**Rate Limiting:** 100 peticiones/minuto por usuario  
**Formato de fechas:** ISO 8601 (`YYYY-MM-DD`)

---

## 🔐 Autenticación

### Headers requeridos en todas las peticiones:
```javascript
{
  "Authorization": "Bearer {tu_token_jwt}",
  "Content-Type": "application/json"
}
```

### Códigos de error estándar:
- `200` - OK
- `201` - Creado exitosamente
- `400` - Bad Request (datos inválidos)
- `401` - No autenticado (token inválido/expirado)
- `403` - Prohibido (sin permisos)
- `404` - No encontrado
- `429` - Too Many Requests (rate limit excedido)
- `500` - Error del servidor

---

## 1. 🤖 COACH IA / INTELIGENCIA FINANCIERA

### 1.1 Consejo del Día

**Endpoint:** `GET /api/coach/consejo-del-dia`

**Autenticación:** ✅ Requerida

**Período analizado:** Últimos 30 días

**Respuesta exitosa (200):**
```json
{
  "consejo": "💰 Has realizado 15 gastos en Alimentación este mes. Considera establecer un presupuesto para controlar mejor estos gastos.",
  "categoria": "Alimentación",
  "nivelImpacto": "ALTO",
  "ahorroPotencial": 850.00,
  "accionSugerida": "Reduce gastos en Alimentación y podrías ahorrar $850 este mes"
}
```

**Campos:**
- `consejo` (string): Mensaje personalizado con emoji
- `categoria` (string): Categoría analizada
- `nivelImpacto` (string): `"ALTO"`, `"MEDIO"`, `"BAJO"`
- `ahorroPotencial` (number): Monto estimado de ahorro en tu moneda
- `accionSugerida` (string): Recomendación específica

**Cache recomendado:** 24 horas

**Ejemplo de uso:**
```javascript
const consejo = await fetch(`${API_URL}/coach/consejo-del-dia`, {
  headers: { 'Authorization': `Bearer ${token}` }
}).then(res => res.json());
```

---

### 1.2 Análisis de Hábitos

**Endpoint:** `GET /api/coach/analisis-habitos`

**Autenticación:** ✅ Requerida

**Período analizado:** Últimos 90 días

**Respuesta exitosa (200):**
```json
{
  "ingresosPromedio": 5200.00,
  "gastosPromedio": 3800.00,
  "tasaAhorro": 26.92,
  "categoriasActivas": 8,
  "diasRegistrados": 67,
  "insights": [
    "✅ ¡Excelente! Estás ahorrando 26.9% de tus ingresos.",
    "📊 El 35% de tus gastos van a una sola categoría. Considera diversificar.",
    "📝 Has registrado movimientos solo 67 días en los últimos 90. La constancia mejora el control."
  ],
  "recomendaciones": [
    "🎯 Establece como meta ahorrar el 20% de tus ingresos",
    "📅 Programa revisiones semanales de tus gastos",
    "🎁 Considera el método de los 30 días para compras grandes",
    "💳 Revisa suscripciones que no uses y cancélalas"
  ]
}
```

**Campos:**
- `ingresosPromedio` (number): Promedio mensual de ingresos
- `gastosPromedio` (number): Promedio mensual de gastos
- `tasaAhorro` (number): Porcentaje de ahorro (0-100)
- `categoriasActivas` (number): Número de categorías usadas
- `diasRegistrados` (number): Días con actividad registrada
- `insights` (array[string]): Lista de observaciones con emojis
- `recomendaciones` (array[string]): Lista de consejos accionables

---

## 2. 🎮 GAMIFICACIÓN

### 2.1 Obtener Logros del Usuario

**Endpoint:** `GET /api/gamificacion/logros`

**Autenticación:** ✅ Requerida

**Respuesta exitosa (200):**
```json
[
  {
    "id": 1,
    "usuarioId": 123,
    "tipoLogro": "PRIMERA_META",
    "desbloqueadoAt": "2026-02-01T10:30:00Z",
    "reclamado": false,
    "progresoActual": 1,
    "progresoRequerido": 1
  },
  {
    "id": 2,
    "usuarioId": 123,
    "tipoLogro": "RACHA_7_DIAS",
    "desbloqueadoAt": "2026-02-05T08:15:00Z",
    "reclamado": true,
    "progresoActual": 7,
    "progresoRequerido": 7
  }
]
```

**Tipos de logros disponibles:**
```javascript
{
  "PRIMERA_META": "🏆 Primera Meta",
  "RACHA_7_DIAS": "🔥 Racha de 7 días",
  "RACHA_30_DIAS": "🔥🔥 Racha de 30 días",
  "AHORRADOR_NINJA": "💰 Ahorrador Ninja",
  "PRESUPUESTO_MAESTRO": "📊 Maestro del Presupuesto",
  "META_MILLONARIA": "💎 Meta Millonaria",
  "DISCIPLINA_FINANCIERA": "🎯 Disciplina Financiera",
  "ANALISTA_PRO": "📈 Analista Pro",
  "INVERSIONISTA": "💼 Inversionista",
  "CERO_DEUDAS": "✨ Cero Deudas"
}
```

---

### 2.2 Obtener Racha Actual

**Endpoint:** `GET /api/gamificacion/racha`

**Autenticación:** ✅ Requerida

**Respuesta exitosa (200):**
```json
{
  "id": 1,
  "usuarioId": 123,
  "rachaActual": 15,
  "rachaMaxima": 30,
  "ultimaActividad": "2026-02-07",
  "diasConsecutivos": 15
}
```

**Campos:**
- `rachaActual` (number): Días consecutivos actuales
- `rachaMaxima` (number): Récord de días consecutivos
- `ultimaActividad` (date): Última fecha de actividad
- `diasConsecutivos` (number): Total de días consecutivos

---

### 2.3 Estadísticas de Gamificación

**Endpoint:** `GET /api/gamificacion/estadisticas`

**Autenticación:** ✅ Requerida

**Respuesta exitosa (200):**
```json
{
  "logrosDesbloqueados": 5,
  "logrosTotales": 10,
  "logrosNoReclamados": 2,
  "rachaActual": 15,
  "rachaMaxima": 30,
  "diasConsecutivos": 15,
  "porcentajeCompletado": 50.0
}
```

---

### 2.4 Reclamar Logro

**Endpoint:** `POST /api/gamificacion/logros/{id}/reclamar`

**Autenticación:** ✅ Requerida

**Path Parameters:**
- `id` (number): ID del logro a reclamar

**Respuesta exitosa (200):**
```json
{
  "mensaje": "Logro reclamado exitosamente"
}
```

**Errores:**
- `404` - Logro no encontrado
- `400` - Logro ya reclamado

---

## 3. 🔔 RECORDATORIOS

### 3.1 Listar Recordatorios

**Endpoint:** `GET /api/recordatorios`

**Autenticación:** ✅ Requerida

**Query Parameters:** Ninguno (devuelve solo activos)

**Respuesta exitosa (200):**
```json
[
  {
    "id": 1,
    "usuarioId": 123,
    "titulo": "Pago de Renta",
    "descripcion": "Renta del departamento",
    "monto": 8000.00,
    "fechaPago": "2026-02-10",
    "recurrente": true,
    "frecuencia": "MENSUAL",
    "diasAnticipacion": 3,
    "activo": true,
    "pagado": false,
    "movimientoId": null,
    "createdAt": "2026-02-01T10:00:00Z",
    "updatedAt": "2026-02-01T10:00:00Z"
  }
]
```

---

### 3.2 Obtener Próximos Recordatorios

**Endpoint:** `GET /api/recordatorios/proximos`

**Autenticación:** ✅ Requerida

**Query Parameters:**
- `dias` (number, opcional): Días hacia adelante (default: 7)

**Ejemplo:** `GET /api/recordatorios/proximos?dias=15`

**Respuesta:** Igual a 3.1

---

### 3.3 Crear Recordatorio

**Endpoint:** `POST /api/recordatorios`

**Autenticación:** ✅ Requerida

**Body (application/json):**
```json
{
  "titulo": "Pago de Netflix",
  "descripcion": "Suscripción mensual",
  "monto": 199.00,
  "fechaPago": "2026-02-15",
  "recurrente": true,
  "frecuencia": "MENSUAL",
  "diasAnticipacion": 3
}
```

**Campos obligatorios:**
- `titulo` (string)
- `fechaPago` (date, formato: YYYY-MM-DD)

**Campos opcionales:**
- `descripcion` (string)
- `monto` (number)
- `recurrente` (boolean, default: false)
- `frecuencia` (string): `"DIARIO"`, `"SEMANAL"`, `"MENSUAL"`, `"ANUAL"`
- `diasAnticipacion` (number, default: 3)

**Respuesta exitosa (201):**
```json
{
  "id": 5,
  "usuarioId": 123,
  "titulo": "Pago de Netflix",
  ...
}
```

---

### 3.4 Marcar como Pagado

**Endpoint:** `PUT /api/recordatorios/{id}/marcar-pagado`

**Autenticación:** ✅ Requerida

**Path Parameters:**
- `id` (number): ID del recordatorio

**Respuesta exitosa (200):**
```json
{
  "id": 1,
  "pagado": true,
  ...
}
```

---

### 3.5 Eliminar Recordatorio

**Endpoint:** `DELETE /api/recordatorios/{id}`

**Autenticación:** ✅ Requerida

**Respuesta exitosa (200):**
```json
{
  "mensaje": "Recordatorio eliminado"
}
```

---

## 4. 💡 AHORRO AUTOMÁTICO

### 4.1 Obtener Estadísticas

**Endpoint:** `GET /api/ahorro-automatico/estadisticas`

**Autenticación:** ✅ Requerida

**Respuesta exitosa (200):**
```json
{
  "id": 1,
  "usuarioId": 123,
  "activo": true,
  "metaDestinoId": 5,
  "tipoRedondeo": "PESO",
  "totalAhorrado": 1250.00,
  "movimientosProcesados": 87,
  "createdAt": "2026-01-15T09:00:00Z",
  "updatedAt": "2026-02-07T14:30:00Z"
}
```

**Campos:**
- `activo` (boolean): Si está activo el ahorro automático
- `metaDestinoId` (number): ID de la meta donde se acumula
- `tipoRedondeo` (string): `"PESO"`, `"CINCO"`, `"DIEZ"`
- `totalAhorrado` (number): Total acumulado hasta ahora
- `movimientosProcesados` (number): Cantidad de movimientos redondeados

---

### 4.2 Configurar Ahorro Automático

**Endpoint:** `POST /api/ahorro-automatico/configurar`

**Autenticación:** ✅ Requerida

**Body (application/json):**
```json
{
  "metaDestinoId": 5,
  "tipoRedondeo": "PESO"
}
```

**Campos:**
- `metaDestinoId` (number, requerido): ID de la meta donde irá el ahorro
- `tipoRedondeo` (string, opcional): `"PESO"` (default), `"CINCO"`, `"DIEZ"`

**Tipos de redondeo:**
- `"PESO"`: Redondea al peso más cercano ($42.30 → $43.00 = ahorra $0.70)
- `"CINCO"`: Redondea a múltiplo de 5 ($42.30 → $45.00 = ahorra $2.70)
- `"DIEZ"`: Redondea a múltiplo de 10 ($42.30 → $50.00 = ahorra $7.70)

**Respuesta exitosa (200):**
```json
{
  "id": 1,
  "usuarioId": 123,
  "activo": true,
  "metaDestinoId": 5,
  "tipoRedondeo": "PESO",
  "totalAhorrado": 0.00,
  "movimientosProcesados": 0
}
```

---

### 4.3 Pausar Ahorro Automático

**Endpoint:** `POST /api/ahorro-automatico/pausar`

**Autenticación:** ✅ Requerida

**Body:** Ninguno

**Respuesta exitosa (200):**
```json
{
  "mensaje": "Ahorro automático pausado"
}
```

---

## 5. 🤝 METAS COMPARTIDAS

**✅ IMPLEMENTADO - Endpoints funcionales**

### 5.1 Compartir Meta con Otro Usuario

**Endpoint:** `POST /api/metas-compartidas/{metaId}/compartir`

**Autenticación:** ✅ Requerida

**Path Parameters:**
- `metaId` (number): ID de la meta a compartir

**Body (application/json):**
```json
{
  "usernameInvitado": "john_doe"
}
```

**Campos obligatorios:**
- `usernameInvitado` (string): Username del usuario con quien compartir

**✨ Cambio importante:** Ahora se usa **username** en lugar de userId para que sea más amigable

**Respuesta exitosa (201 Created):**
```json
{
  "id": 10,
  "metaId": 1,
  "usuarioId": 456,
  "esCreador": false,
  "aporteTotal": 0.00,
  "porcentajeAporte": 0.0,
  "activo": true,
  "invitadoAt": "2026-02-08T15:30:00Z",
  "aceptadoAt": null
}
```

**Notificación que recibe el invitado:**
```json
{
  "id": 1,
  "usuarioId": 456,
  "tipo": "META_COMPARTIDA",
  "titulo": "🤝 Invitación a Meta Compartida",
  "mensaje": "john_doe te ha invitado a colaborar en 'Vacaciones 2026'",
  "leida": false,
  "fechaEnvio": "2026-02-08T03:54:49",
  "metaId": 1,
  "usuarioInvitador": "john_doe",
  "metaNombre": "Vacaciones 2026"
}
```

**Validaciones:**
- ✅ Usuario debe ser dueño de la meta
- ✅ No se puede compartir con uno mismo
- ✅ Username debe existir
- ✅ Se envía notificación automática con **metaId**, **usuarioInvitador** y **metaNombre**

**Errores posibles:**
- `404` - Meta no encontrada
- `403` - No eres dueño de esta meta
- `400` - Usuario 'username' no encontrado

---

### 5.2 Realizar Aporte a Meta Compartida

**Endpoint:** `POST /api/metas-compartidas/{metaId}/aportar`

**Autenticación:** ✅ Requerida

**Path Parameters:**
- `metaId` (number): ID de la meta

**Body (application/json):**
```json
{
  "monto": 1000.00,
  "descripcion": "Aporte mensual"
}
```

**Campos obligatorios:**
- `monto` (number): Monto a aportar (debe ser > 0)

**Campos opcionales:**
- `descripcion` (string): Descripción del aporte

**Respuesta exitosa (200):**
```json
{
  "mensaje": "Aporte registrado exitosamente",
  "monto": 1000.00
}
```

**Comportamiento:**
- ✅ Suma el aporte al total del colaborador
- ✅ Actualiza porcentaje de aporte automáticamente
- ✅ Notifica a otros colaboradores del aporte
- ✅ Crea movimiento de tipo SAVINGS vinculado a la meta

---

### 5.3 Ver Colaboradores de una Meta

**Endpoint:** `GET /api/metas-compartidas/{metaId}/colaboradores`

**Autenticación:** ✅ Requerida

**Path Parameters:**
- `metaId` (number): ID de la meta

**Respuesta exitosa (200):**
```json
[
  {
    "id": 8,
    "metaId": 1,
    "usuarioId": 123,
    "esCreador": true,
    "aporteTotal": 3000.00,
    "porcentajeAporte": 60.0,
    "activo": true,
    "invitadoAt": "2026-01-01T00:00:00Z",
    "aceptadoAt": "2026-01-01T00:00:00Z"
  },
  {
    "id": 9,
    "metaId": 1,
    "usuarioId": 456,
    "esCreador": false,
    "aporteTotal": 2000.00,
    "porcentajeAporte": 40.0,
    "activo": true,
    "invitadoAt": "2026-01-15T10:00:00Z",
    "aceptadoAt": "2026-01-15T11:30:00Z"
  }
]
```

**Campos importantes:**
- `esCreador` (boolean): Si es el dueño original de la meta
- `aporteTotal` (number): Total aportado por este colaborador
- `porcentajeAporte` (number): % del total aportado
- `aceptadoAt` (datetime): Cuándo aceptó la invitación (null si no ha aceptado)

---

### 5.4 Obtener Mis Metas Compartidas

**Endpoint:** `GET /api/metas-compartidas/mis-metas`

**Autenticación:** ✅ Requerida

**Descripción:** Devuelve todas las metas donde el usuario es colaborador (propias y compartidas con él)

**Respuesta exitosa (200):**
```json
[
  {
    "id": 15,
    "metaId": 5,
    "usuarioId": 123,
    "esCreador": false,
    "aporteTotal": 1500.00,
    "porcentajeAporte": 30.0,
    "activo": true,
    "invitadoAt": "2026-02-01T10:00:00Z",
    "aceptadoAt": "2026-02-01T11:30:00Z"
  }
]
```

---

### 5.5 Aceptar Invitación a Meta Compartida

**Endpoint:** `POST /api/metas-compartidas/{metaId}/aceptar`

**Autenticación:** ✅ Requerida

**Path Parameters:**
- `metaId` (number): ID de la meta

**Body:** Ninguno

**Respuesta exitosa (200):**
```json
{
  "mensaje": "Invitación aceptada exitosamente",
  "monto": null
}
```

**Descripción:** Marca la invitación como aceptada y actualiza `aceptadoAt`

---

### 5.6 Salir de Meta Compartida

**Endpoint:** `DELETE /api/metas-compartidas/{metaId}/salir`

**Autenticación:** ✅ Requerida

**Path Parameters:**
- `metaId` (number): ID de la meta

**Respuesta exitosa (200):**
```json
{
  "mensaje": "Has salido de la meta compartida",
  "monto": null
}
```

**Validaciones:**
- ❌ El creador NO puede salir de su propia meta
- ✅ Colaboradores pueden salir cuando quieran

**Error:**
- `400` - "El creador no puede salir de la meta"

---

### 5.7 Eliminar Colaborador (Solo Creador)

**Endpoint:** `DELETE /api/metas-compartidas/{metaId}/colaborador/{colaboradorId}`

**Autenticación:** ✅ Requerida

**Path Parameters:**
- `metaId` (number): ID de la meta
- `colaboradorId` (number): ID del usuario a eliminar

**Respuesta exitosa (200):**
```json
{
  "mensaje": "Colaborador eliminado exitosamente",
  "monto": null
}
```

**Validaciones:**
- ✅ Solo el creador puede eliminar colaboradores
- ❌ No se puede eliminar al creador
- ✅ Envía notificación al colaborador eliminado

**Errores:**
- `403` - "Solo el creador puede eliminar colaboradores"
- `400` - "No puedes eliminar al creador"

---

### 📱 Ejemplo de Integración en React Native

```javascript
// services/metasCompartidasService.js
import api from './api';

export const metasCompartidasService = {
  // Compartir meta con otro usuario (por username)
  compartirMeta: async (metaId, usernameInvitado) => {
    const response = await api.post(
      `/metas-compartidas/${metaId}/compartir`,
      { usernameInvitado }
    );
    return response.data;
  },

  // Realizar un aporte
  aportar: async (metaId, monto, descripcion = '') => {
    const response = await api.post(
      `/metas-compartidas/${metaId}/aportar`,
      { monto, descripcion }
    );
    return response.data;
  },

  // Ver colaboradores
  getColaboradores: async (metaId) => {
    const response = await api.get(
      `/metas-compartidas/${metaId}/colaboradores`
    );
    return response.data;
  },

  // Mis metas compartidas
  getMisMetasCompartidas: async () => {
    const response = await api.get('/metas-compartidas/mis-metas');
    return response.data;
  },

  // Aceptar invitación
  aceptarInvitacion: async (metaId) => {
    const response = await api.post(
      `/metas-compartidas/${metaId}/aceptar`
    );
    return response.data;
  },

  // Salir de meta
  salirDeMeta: async (metaId) => {
    const response = await api.delete(
      `/metas-compartidas/${metaId}/salir`
    );
    return response.data;
  },

  // Eliminar colaborador (solo creador)
  eliminarColaborador: async (metaId, colaboradorId) => {
    const response = await api.delete(
      `/metas-compartidas/${metaId}/colaborador/${colaboradorId}`
    );
    return response.data;
  }
};
```

### 🎨 Ejemplo de UI

```javascript
// screens/MetaCompartidaScreen.js
import { useState, useEffect } from 'react';
import { metasCompartidasService } from '../services';

const MetaCompartidaScreen = ({ metaId }) => {
  const [colaboradores, setColaboradores] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadColaboradores();
  }, [metaId]);

  const loadColaboradores = async () => {
    try {
      const data = await metasCompartidasService.getColaboradores(metaId);
      setColaboradores(data);
    } catch (error) {
      console.error('Error:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleAportar = async () => {
    try {
      await metasCompartidasService.aportar(metaId, 500, 'Aporte rápido');
      Alert.alert('✅', 'Aporte registrado exitosamente');
      loadColaboradores(); // Recargar datos
    } catch (error) {
      Alert.alert('Error', error.message);
    }
  };

  return (
    <View>
      <Text style={styles.title}>Colaboradores</Text>
      {colaboradores.map(col => (
        <View key={col.id} style={styles.colaborador}>
          <Text>{col.esCreador ? '👑' : '🤝'} Usuario #{col.usuarioId}</Text>
          <Text>Aportado: ${col.aporteTotal}</Text>
          <Text>Porcentaje: {col.porcentajeAporte}%</Text>
        </View>
      ))}
      <Button title="Aportar $500" onPress={handleAportar} />
    </View>
  );
};
```

---

## 6. ❓ CONTEXTOS FINANCIEROS

### ⚠️ Estado Actual

**Endpoint:** `GET /api/contextos-financieros`

**Estado:** ✅ **FUNCIONANDO** (error corregido en v1.5.0)

**Autenticación:** ✅ Requerida

**Respuesta exitosa (200):**
```json
{
  "id": 1,
  "usuarioId": 123,
  "saldoActual": 15000.00,
  "totalIngresos": 45000.00,
  "totalGastos": 30000.00,
  "totalAhorros": 5000.00,
  "metasActivas": 3,
  "metasCompletadas": 1,
  "ultimaActualizacion": "2026-02-07T15:45:00Z"
}
```

**Si devuelve 500:** Verifica que el token sea válido y que el usuario exista.

---

## 7. 🔔 NOTIFICACIONES

### 7.1 Obtener Todas las Notificaciones

**Endpoint:** `GET /api/notificaciones`

**Autenticación:** ✅ Requerida

**Respuesta exitosa (200):**
```json
[
  {
    "id": 1,
    "usuarioId": 123,
    "tipo": "META_COMPARTIDA",
    "titulo": "🤝 Invitación a Meta Compartida",
    "mensaje": "john_doe te ha invitado a colaborar en 'Vacaciones 2026'",
    "leida": false,
    "fechaEnvio": "2026-02-08T03:54:49",
    "createdAt": "2026-02-08T03:54:49",
    "metaId": 1,
    "usuarioInvitador": "john_doe",
    "metaNombre": "Vacaciones 2026",
    "version": "1.6.0"
  }
]
```

---

### 7.2 Obtener Notificaciones No Leídas

**Endpoint:** `GET /api/notificaciones/no-leidas`

**Autenticación:** ✅ Requerida

**Respuesta:** Igual a 7.1, solo notificaciones con `leida: false`

---

### 7.3 Contador de No Leídas

**Endpoint:** `GET /api/notificaciones/contador`

**Autenticación:** ✅ Requerida

**Respuesta exitosa (200):**
```json
{
  "noLeidas": 5
}
```

---

### 7.4 Marcar como Leída

**Endpoint:** `PUT /api/notificaciones/{id}/marcar-leida`

**Autenticación:** ✅ Requerida

**Path Parameters:**
- `id` (number): ID de la notificación

**Respuesta exitosa (200):**
```json
{
  "id": 1,
  "leida": true,
  ...
}
```

---

### 7.5 Marcar Todas como Leídas

**Endpoint:** `PUT /api/notificaciones/marcar-todas-leidas`

**Autenticación:** ✅ Requerida

**Respuesta exitosa (200):**
```json
{
  "mensaje": "5 notificaciones marcadas como leídas"
}
```

---

### 7.6 Eliminar Notificación

**Endpoint:** `DELETE /api/notificaciones/{id}`

**Autenticación:** ✅ Requerida

**Path Parameters:**
- `id` (number): ID de la notificación

**Respuesta exitosa (200):**
```json
{
  "mensaje": "Notificación eliminada"
}
```

---

### 7.7 Limpiar Notificaciones Antiguas ⭐ NUEVO

**Endpoint:** `DELETE /api/notificaciones/limpiar-antiguas`

**Autenticación:** ✅ Requerida

**Estado:** ✅ **CORREGIDO Y FUNCIONAL** (v1.6.0+)

**Descripción:** Elimina notificaciones sin metadata (pre-v1.6.0) que no tienen `metaId`, `usuarioInvitador`, etc. Este endpoint **resuelve el problema** de notificaciones antiguas que causan errores de `metaId undefined`.

**¿Por qué es necesario?**
Las notificaciones pre-v1.6.0 no tienen los campos necesarios para metas compartidas:
- ❌ Sin `metaId` → Error al intentar aceptar meta
- ❌ Sin `usuarioInvitador` → No se sabe quién invita
- ❌ Sin `metaNombre` → No se sabe qué meta es

**Respuesta exitosa (200):**
```json
{
  "mensaje": "Notificaciones antiguas eliminadas",
  "cantidad": 12,
  "version": "1.6.0"
}
```

**Campos de respuesta:**
- `mensaje` (string): Mensaje de confirmación
- `cantidad` (number): Número real de notificaciones eliminadas
- `version` (string): Versión del formato actual

**Uso recomendado:**
- ✅ Llamar **una sola vez** al actualizar la app a v1.6.0
- ✅ Usar control de versión con AsyncStorage
- ✅ Ejecutar en background al iniciar la app

**Implementación completa:**

```javascript
// services/notificationService.js
import api from './api';
import AsyncStorage from '@react-native-async-storage/async-storage';

const APP_VERSION = '1.6.0';
const VERSION_KEY = 'lastAppVersion';

/**
 * Limpia notificaciones antiguas (pre-v1.6.0) una sola vez
 * Resuelve el problema de metaId undefined
 */
export const limpiarCacheAntiguo = async () => {
  try {
    const lastVersion = await AsyncStorage.getItem(VERSION_KEY);
    
    // Solo ejecutar si es primera vez con v1.6.0
    if (lastVersion !== APP_VERSION) {
      console.log('🧹 Limpiando notificaciones antiguas...');
      
      const { data } = await api.delete('/notificaciones/limpiar-antiguas');
      
      console.log(`✅ ${data.cantidad} notificaciones obsoletas eliminadas`);
      console.log(`📦 Versión actual: ${data.version}`);
      
      // Guardar versión para no volver a limpiar
      await AsyncStorage.setItem(VERSION_KEY, APP_VERSION);
      
      return {
        success: true,
        cantidad: data.cantidad,
        version: data.version
      };
    }
    
    console.log('✨ Cache ya está limpio');
    return { success: true, cantidad: 0, cached: true };
    
  } catch (error) {
    console.error('❌ Error limpiando notificaciones:', error);
    return { 
      success: false, 
      error: error.message,
      cantidad: 0 
    };
  }
};

/**
 * Valida que una notificación tenga el formato v1.6.0
 */
export const esNotificacionValida = (notif) => {
  // Verificar que tenga versión correcta
  if (!notif.version || notif.version !== '1.6.0') {
    console.warn('⚠️ Notificación con formato antiguo:', notif.id);
    return false;
  }
  
  // Si es META_COMPARTIDA, verificar metadata obligatoria
  if (notif.tipo === 'META_COMPARTIDA') {
    const tieneMetadata = 
      notif.metaId && 
      notif.usuarioInvitador && 
      notif.metaNombre;
    
    if (!tieneMetadata) {
      console.warn('⚠️ META_COMPARTIDA sin metadata completa:', notif.id);
      return false;
    }
  }
  
  return true;
};

/**
 * Filtra solo notificaciones válidas v1.6.0
 */
export const filtrarNotificacionesValidas = (notificaciones) => {
  return notificaciones.filter(esNotificacionValida);
};
```

**Uso en App.js:**

```javascript
// App.js
import { useEffect, useState } from 'react';
import { limpiarCacheAntiguo } from './services/notificationService';

const App = () => {
  const [isReady, setIsReady] = useState(false);

  useEffect(() => {
    inicializarApp();
  }, []);

  const inicializarApp = async () => {
    try {
      // Limpiar notificaciones antiguas (solo primera vez)
      const resultado = await limpiarCacheAntiguo();
      
      if (resultado.success && resultado.cantidad > 0) {
        console.log(`🎉 App actualizada a v${resultado.version}`);
        console.log(`🧹 ${resultado.cantidad} notificaciones antiguas eliminadas`);
        
        // Opcional: Mostrar toast al usuario
        // Toast.show('Cache limpiado exitosamente');
      }
    } catch (error) {
      console.error('Error inicializando app:', error);
    } finally {
      setIsReady(true);
    }
  };

  if (!isReady) {
    return <SplashScreen />;
  }

  return <NavigationContainer>{/* Tu app */}</NavigationContainer>;
};
```

**Validación en componentes:**

```javascript
// components/NotificationBadge.js
import { esNotificacionValida } from '../services/notificationService';

const NotificationBadge = ({ notification }) => {
  // Validar antes de renderizar
  if (!esNotificacionValida(notification)) {
    return null; // No mostrar notificaciones antiguas
  }

  const handleAccept = async () => {
    if (notification.tipo === 'META_COMPARTIDA') {
      // ✅ Garantizado que metaId existe en v1.6.0
      await goalService.acceptGoal(notification.metaId);
    }
  };

  return (
    <View>
      <Text>{notification.mensaje}</Text>
      {notification.tipo === 'META_COMPARTIDA' && (
        <>
          <Text>Invitado por: {notification.usuarioInvitador}</Text>
          <Text>Meta: {notification.metaNombre}</Text>
          <Button 
            onPress={handleAccept}
            // ✅ Ya no necesitas disabled, siempre tendrá metaId
          >
            Aceptar Invitación
          </Button>
        </>
      )}
    </View>
  );
};
```

---

### 📱 Estructura de Notificación v1.6.0

**Todos los tipos de notificación ahora incluyen:**
- `version` (string): Versión del formato ("1.6.0")

**Notificaciones relacionadas con metas compartidas incluyen `metaId`:**

1. **META_COMPARTIDA** (Invitación a meta compartida):
   - `metaId` (number): ID de la meta para aceptar/rechazar
   - `usuarioInvitador` (string): Username de quien invita
   - `metaNombre` (string): Nombre de la meta

2. **APORTE_META_COMPARTIDA** (Notificación de nuevo aporte):
   - `metaId` (number): ID de la meta donde se hizo el aporte
   - `usuarioInvitador` (string): Username de quien hizo el aporte
   - `metaNombre` (string): Nombre de la meta

3. **META_COMPLETADA** (Meta alcanzada):
   - `metaId` (number): ID de la meta completada
   - `metaNombre` (string): Nombre de la meta

4. **RECORDATORIO_META** (Recordatorio de meta):
   - `metaId` (number): ID de la meta
   - `metaNombre` (string): Nombre de la meta

**Validación en frontend:**
```javascript
const esNotificacionValida = (notif) => {
  // Verificar que tenga versión
  if (!notif.version || notif.version !== '1.6.0') {
    return false;
  }
  
  // Tipos de notificación que REQUIEREN metaId
  const tiposConMetaId = [
    'META_COMPARTIDA',
    'APORTE_META_COMPARTIDA',
    'META_COMPLETADA',
    'RECORDATORIO_META'
  ];
  
  // Si es un tipo que necesita metaId, validar que exista
  if (tiposConMetaId.includes(notif.tipo)) {
    if (!notif.metaId || notif.metaId === null) {
      return false;
    }
    
    // Validación especial para META_COMPARTIDA
    if (notif.tipo === 'META_COMPARTIDA') {
      return notif.usuarioInvitador && notif.metaNombre;
    }
  }
  
  return true;
};
```

**Ejemplo de notificación de aporte:**
```json
{
  "id": 2,
  "usuarioId": 123,
  "tipo": "APORTE_META_COMPARTIDA",
  "titulo": "💰 Nuevo Aporte a Meta Compartida",
  "mensaje": "john_doe ha realizado un aporte de $500.00 a 'Vacaciones 2026'",
  "leida": false,
  "fechaEnvio": "2026-02-08T10:30:00",
  "metaId": 1,
  "usuarioInvitador": "john_doe",
  "metaNombre": "Vacaciones 2026",
  "version": "1.6.0"
}
```

---

## 📊 INFORMACIÓN ADICIONAL

### Rate Limiting
- **Límite:** 100 peticiones por minuto
- **Header de respuesta:** `X-Rate-Limit-Remaining: 95`
- **Error 429:** Espera 60 segundos antes de reintentar

### Paginación
Los siguientes endpoints soportan paginación:

**Movimientos:**
```
GET /api/movimientos/paginated?page=0&size=20&sort=fechaMovimiento,desc
```

**Notificaciones:**
```
GET /api/notificaciones?page=0&size=10
```

### Manejo de Errores

**Estructura de error estándar:**
```json
{
  "error": "Bad Request",
  "message": "El campo 'titulo' es obligatorio",
  "status": 400,
  "timestamp": "2026-02-07T15:50:00Z"
}
```

### Tokens JWT
- **Duración Access Token:** 24 horas
- **Duración Refresh Token:** 30 días
- **Endpoint para refrescar:** `POST /api/auth/refresh`

---

## 🚀 Ejemplos de Código

### React Native (Axios)

```javascript
// services/api.js
import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';

const API_URL = 'https://tu-api.render.com/api';

const api = axios.create({
  baseURL: API_URL,
  timeout: 10000,
});

// Interceptor para agregar token automáticamente
api.interceptors.request.use(async (config) => {
  const token = await AsyncStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Interceptor para manejar errores
api.interceptors.response.use(
  response => response,
  async error => {
    if (error.response?.status === 401) {
      // Token expirado, intentar refresh
      await refreshToken();
    }
    return Promise.reject(error);
  }
);

export default api;
```

### Uso de Coach IA

```javascript
// screens/CoachScreen.js
import { useState, useEffect } from 'react';
import api from '../services/api';

const CoachScreen = () => {
  const [consejo, setConsejo] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadConsejo();
  }, []);

  const loadConsejo = async () => {
    try {
      const { data } = await api.get('/coach/consejo-del-dia');
      setConsejo(data);
    } catch (error) {
      console.error('Error cargando consejo:', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <Loading />;

  return (
    <View>
      <Text style={styles.consejo}>{consejo.consejo}</Text>
      <Badge nivel={consejo.nivelImpacto} />
      <Text style={styles.ahorro}>
        Ahorro potencial: ${consejo.ahorroPotencial}
      </Text>
      <Button onPress={() => {}}>
        {consejo.accionSugerida}
      </Button>
    </View>
  );
};
```

---

## 📞 Soporte

**Problemas o dudas:**
- Revisa este documento primero
- Verifica que el token sea válido
- Comprueba los códigos de error HTTP
- Si el problema persiste, contacta al equipo de backend

**Versión del documento:** 1.6.0  
**Última actualización:** 2026-02-08

---

## 🔧 TROUBLESHOOTING

### Problema: "metaId is undefined" en notificaciones

**Causa:** Notificaciones antiguas (pre-v1.6.0) sin metadata

**Solución:**
```javascript
// Ejecutar una sola vez al actualizar app
await api.delete('/notificaciones/limpiar-antiguas');
```

**Prevención:**
- Validar `notification.version === '1.6.0'` antes de usar
- Filtrar notificaciones con `esNotificacionValida()`
- No mostrar notificaciones sin `metaId` si son de tipo META_COMPARTIDA

---

### Problema: Endpoint devuelve 401 Unauthorized

**Causa:** Token expirado o inválido

**Solución:**
```javascript
// Usar refresh token
const response = await api.post('/auth/refresh', {
  refreshToken: storedRefreshToken
});
const newToken = response.data.accessToken;
```

---

### Problema: Error 429 Too Many Requests

**Causa:** Rate limit excedido (100 req/min)

**Solución:**
- Implementar debounce en peticiones
- Cachear respuestas localmente
- Esperar 60 segundos antes de reintentar
- Verificar header `X-Rate-Limit-Remaining`

---

## 📋 MEJORES PRÁCTICAS

### 1. Manejo de Notificaciones

✅ **HACER:**
```javascript
// Validar versión antes de usar
if (notification.version === '1.6.0') {
  // Usar notificación
}

// Filtrar notificaciones válidas
const validas = notifications.filter(esNotificacionValida);
```

❌ **NO HACER:**
```javascript
// No asumir que metaId siempre existe
await goalService.acceptGoal(notification.metaId); // Puede ser undefined

// No ignorar el campo version
// Siempre validar antes de usar
```

---

### 2. Limpieza de Cache

✅ **HACER:**
```javascript
// Limpiar solo una vez por versión
const APP_VERSION = '1.6.0';
const lastVersion = await AsyncStorage.getItem('lastAppVersion');

if (lastVersion !== APP_VERSION) {
  await limpiarCacheAntiguo();
  await AsyncStorage.setItem('lastAppVersion', APP_VERSION);
}
```

❌ **NO HACER:**
```javascript
// No limpiar en cada inicio
useEffect(() => {
  limpiarCacheAntiguo(); // ❌ Innecesario y lento
}, []);
```

---

### 3. Metas Compartidas

✅ **HACER:**
```javascript
// Usar username, no userId
await api.post('/metas-compartidas/1/compartir', {
  usernameInvitado: 'john_doe' // ✅ Amigable
});

// Validar metadata en notificación
if (notif.metaId && notif.usuarioInvitador) {
  // Mostrar UI completa
}
```

❌ **NO HACER:**
```javascript
// No usar userId (deprecated)
await api.post('/metas-compartidas/1/compartir', {
  usuarioInvitadoId: 456 // ❌ Ya no funciona
});
```

---

### 4. Manejo de Errores

✅ **HACER:**
```javascript
try {
  const response = await api.get('/endpoint');
  return response.data;
} catch (error) {
  if (error.response?.status === 401) {
    // Token expirado, refresh
    await refreshToken();
  } else if (error.response?.status === 429) {
    // Rate limit, esperar
    await delay(60000);
  } else {
    // Otros errores
    console.error('Error:', error.message);
  }
  throw error;
}
```

---

### 5. Optimización de Peticiones

✅ **HACER:**
```javascript
// Cachear respuestas
const [notificaciones, setNotificaciones] = useState([]);
const [lastFetch, setLastFetch] = useState(0);

const fetchNotificaciones = async () => {
  const now = Date.now();
  // Cachear por 30 segundos
  if (now - lastFetch < 30000 && notificaciones.length > 0) {
    return notificaciones;
  }
  
  const { data } = await api.get('/notificaciones');
  setNotificaciones(data);
  setLastFetch(now);
  return data;
};
```

---

## 🎯 CHECKLIST DE INTEGRACIÓN

### Al implementar v1.6.0:

- [ ] Implementar limpieza de cache al actualizar
- [ ] Validar `version` en todas las notificaciones
- [ ] Actualizar compartir metas para usar `username`
- [ ] Agregar validación de metadata en META_COMPARTIDA
- [ ] Implementar manejo de rate limiting
- [ ] Configurar refresh token automático
- [ ] Agregar manejo de errores 401/429
- [ ] Cachear respuestas frecuentes
- [ ] Probar con notificaciones antiguas
- [ ] Verificar que metaId siempre exista antes de usar

---

## 📚 RECURSOS ADICIONALES

### Estructura de Notificación v1.6.0:
```typescript
interface NotificacionV160 {
  id: number;
  usuarioId: number;
  tipo: string;
  titulo: string;
  mensaje: string;
  leida: boolean;
  fechaEnvio: string;
  createdAt: string;
  version: '1.6.0';  // ✅ Siempre presente
  
  // Metadata para META_COMPARTIDA
  metaId?: number;
  usuarioInvitador?: string;
  metaNombre?: string;
}
```

### Validación TypeScript:
```typescript
const validarNotificacion = (notif: any): notif is NotificacionV160 => {
  return (
    notif.version === '1.6.0' &&
    (notif.tipo !== 'META_COMPARTIDA' || (
      typeof notif.metaId === 'number' &&
      typeof notif.usuarioInvitador === 'string' &&
      typeof notif.metaNombre === 'string'
    ))
  );
};
```

---

**✅ Documentación completa y actualizada con todas las correcciones y mejores prácticas.**

