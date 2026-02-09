# 📱 DOCUMENTACIÓN FRONTEND API - MetaFy v1.6.0
**Fecha:** 2026-02-09  
**Versión API:** v1.6.0  
**Estado:** ✅ Producción
---
## 🐛 PROBLEMA SOLUCIONADO: Metas Compartidas
### **Síntoma del Error**
El usuario invitado **aceptaba** una invitación a meta compartida, pero la meta **NO aparecía** en su lista de metas (`GET /api/metas`).
### **Causa Raíz**
La query SQL que obtenía las metas del usuario incluía metas compartidas **sin validar** si la invitación había sido **aceptada**. Esto causaba que:
1. ❌ Metas compartidas **pendientes** (sin aceptar) aparecieran en el listado
2. ❌ Todas las metas del creador aparecían para el colaborador
3. ❌ No se validaba el campo `aceptadoAt` en `meta_colaboradores`
### **Solución Implementada**
**Archivo modificado:** `MetaFinancieraJpaRepository.java`
**Query anterior (incorrecta):**
```java
@Query("SELECT DISTINCT m FROM MetaFinancieraEntity m " +
       "LEFT JOIN MetaColaboradorEntity c ON m.id = c.metaId " +
       "WHERE (m.userId = :userId OR (c.usuarioId = :userId AND c.activo = true))")
```
**Query corregida:**
```java
@Query("SELECT DISTINCT m FROM MetaFinancieraEntity m " +
       "LEFT JOIN MetaColaboradorEntity c ON m.id = c.metaId " +
       "WHERE m.userId = :userId " +
       "OR (c.usuarioId = :userId AND c.activo = true AND c.aceptadoAt IS NOT NULL)")
```
### **Cambios clave:**
1. ✅ Agregado `c.aceptadoAt IS NOT NULL` → Solo metas **aceptadas**
2. ✅ Separado condiciones con `OR` → Claridad en la lógica
3. ✅ Validación de `activo = true` → Excluye colaboradores que salieron
---
## 📡 ENDPOINTS DE METAS COMPARTIDAS
### **1. Compartir Meta con Otro Usuario**
**Endpoint:** `POST /api/metas-compartidas/{metaId}/compartir`
**Headers:**
- Authorization: Bearer {token}
- Content-Type: application/json
**Body:**
```json
{
  "usernameInvitado": "shell"
}
```
**Response 201:**
```json
{
  "id": 15,
  "metaId": 14,
  "usuarioId": 6,
  "esCreador": false,
  "aporteTotal": 0,
  "porcentajeAporte": 0.0,
  "activo": true,
  "invitadoAt": "2026-02-09T10:10:27.710043",
  "aceptadoAt": null
}
```
**Errores:**
- 400 - Usuario no encontrado
- 409 - Este usuario ya es colaborador
- 403 - Sin permiso
---
### **2. Aceptar Invitación**
**Endpoint:** `POST /api/metas-compartidas/{metaId}/aceptar`
**Headers:**
- Authorization: Bearer {token}
**Response 200:**
```json
{
  "mensaje": "Invitación aceptada exitosamente",
  "monto": null
}
```
---
### **3. Obtener Todas las Metas**
**Endpoint:** `GET /api/metas`
**Comportamiento:**
- ✅ Incluye metas propias
- ✅ Incluye metas compartidas ACEPTADAS
- ❌ NO incluye pendientes
**Response 200:**
```json
[
  {
    "id": 14,
    "usuarioId": 5,
    "nombre": "Meta Compartida",
    "montoObjetivo": 5000.00,
    ...
  },
  {
    "id": 15,
    "usuarioId": 6,
    "nombre": "Meta Propia",
    "montoObjetivo": 2000.00,
    ...
  }
]
```
**Diferenciar en frontend:**
```typescript
if (meta.usuarioId === currentUserId) {
  // Meta propia
} else {
  // Meta compartida
}
```
---
## 🧪 FLUJO COMPLETO (curl)
```bash
# 1. Login Usuario A
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"sn4yber2","password":"snayber4589"}' \
  | jq -r '.token' > token_a.txt
# 2. Crear Meta
TOKEN_A=$(cat token_a.txt)
curl -X POST http://localhost:8080/api/metas \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN_A" \
  -d '{
    "nombre": "Vacaciones",
    "descripcion": "Viaje",
    "montoObjetivo": 5000.00,
    "fechaObjetivo": "2026-12-31",
    "prioridad": "HIGH"
  }' | jq -r '.id' > meta_id.txt
# 3. Compartir con Usuario B
META_ID=$(cat meta_id.txt)
curl -X POST http://localhost:8080/api/metas-compartidas/$META_ID/compartir \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN_A" \
  -d '{"usernameInvitado":"shell"}' | jq
# 4. Login Usuario B
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"shell","password":"shelsin0407"}' \
  | jq -r '.token' > token_b.txt
# 5. Ver Notificaciones
TOKEN_B=$(cat token_b.txt)
curl -X GET http://localhost:8080/api/notificaciones \
  -H "Authorization: Bearer $TOKEN_B" | jq
# 6. Aceptar Invitación
curl -X POST http://localhost:8080/api/metas-compartidas/$META_ID/aceptar \
  -H "Authorization: Bearer $TOKEN_B" | jq
# 7. Verificar Meta Aparece
curl -X GET http://localhost:8080/api/metas \
  -H "Authorization: Bearer $TOKEN_B" | jq
```
---
## 📋 CAMBIOS EN EL BACKEND
### Archivos Modificados:
1. **SharedGoalsService.java** - Validación duplicados
2. **MetaFinancieraJpaRepository.java** - Query corregida
3. **MetaFinancieraRepositoryAdapter.java** - Usa nueva query
4. **SecurityConfig.java** - Permitir /api/maintenance/**
5. **WebMvcConfig.java** - Excluir maintenance del rate limit
### Archivos Nuevos:
6. **MaintenanceController.java** - Endpoints de limpieza
---
## 🎯 INTEGRACIÓN FRONTEND
### Mostrar Metas
```typescript
const fetchMetas = async () => {
  const response = await fetch('/api/metas', {
    headers: { 'Authorization': `Bearer ${token}` }
  });
  const metas = await response.json();
  const currentUserId = getUserIdFromToken();
  const metasPropias = metas.filter(m => m.usuarioId === currentUserId);
  const metasCompartidas = metas.filter(m => m.usuarioId !== currentUserId);
};
```
### Indicador Visual
```jsx
{meta.usuarioId !== currentUserId && <Badge>🤝 Compartida</Badge>}
```
### Aceptar Invitación
```typescript
const aceptarInvitacion = async (metaId: number) => {
  await fetch(`/api/metas-compartidas/${metaId}/aceptar`, {
    method: 'POST',
    headers: { 'Authorization': `Bearer ${token}` }
  });
  await fetchMetas(); // Recargar lista
};
```
---
## ✅ CHECKLIST
- [ ] Actualizar función obtención de metas
- [ ] Agregar indicador visual compartida
- [ ] Filtrar notificaciones v1.6.0
- [ ] Botones aceptar/rechazar
- [ ] Recargar metas al aceptar
- [ ] Vista colaboradores
- [ ] Funcionalidad aportes
- [ ] Opción salir de meta
---
**Última actualización:** 2026-02-09  
**Versión:** 1.0
---
### Obtener Aportes de Meta Compartida
```typescript
const fetchAportes = async (metaId: number) => {
  const response = await fetch(`/api/metas-compartidas/${metaId}/aportes`, {
    headers: { 'Authorization': `Bearer ${token}` }
  });
  const aportes = await response.json();
  // Ejemplo de respuesta:
  // [
  //   {
  //     usuarioId: 5,
  //     username: "sn4yber2",
  //     email: "bmxsnayber@gmail.com",
  //     aporteTotal: 1500.00,
  //     porcentajeAporte: 30.0,
  //     esCreador: true,
  //     fechaAceptacion: "2026-02-09T10:10:17.397693"
  //   },
  //   ...
  // ]
  return aportes;
};
```
**Endpoint:** `GET /api/metas-compartidas/{metaId}/aportes`
**Headers:**
- Authorization: Bearer {token}
**curl:**
```bash
curl -X GET http://localhost:8080/api/metas-compartidas/14/aportes \
  -H "Authorization: Bearer $TOKEN"
```
