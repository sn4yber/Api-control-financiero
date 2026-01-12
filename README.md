# 🏦 API Control Financiero Personal

> Sistema RESTful de gestión financiera personal con **Clean Architecture** + **DDD**

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.9-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue.svg)](https://www.postgresql.org/)
[![API REST](https://img.shields.io/badge/API-RESTful-blue.svg)](https://restfulapi.net/)

---

## 📋 Índice

- [¿Qué es?](#-qué-es)
- [Características](#-características)
- [Arquitectura](#-arquitectura)
- [Stack Tecnológico](#-stack-tecnológico)
- [Modelo de Dominio](#-modelo-de-dominio)
- [API Endpoints](#-api-endpoints)
- [Estado del Proyecto](#-estado-del-proyecto)

---

## 🎯 ¿Qué es?

**API Control Financiero** es una solución backend completa para gestión de finanzas personales. Se adapta a diferentes realidades financieras sin imponer reglas fijas.

### Características Distintivas

✅ **Adaptable** - Soporta múltiples tipos de ingresos (mensual, quincenal, por proyecto, variable)  
✅ **Inteligente** - Calcula saldos en tiempo real sin almacenamiento redundante  
✅ **Escalable** - Clean Architecture permite crecimiento sin acoplamiento  
✅ **Realista** - Diferencia entre ingresos reales y préstamos  
✅ **Motivadora** - Sistema de metas con progreso automático  

---

## ✨ Características

### 🎯 Gestión de Usuarios
- Registro y perfiles de usuario
- Contexto financiero personalizado por usuario
- Datos completamente aislados entre usuarios

### 💼 Contexto Financiero
- **Tipos de ingreso**: Mensual, Quincenal, Semanal, Por Proyecto, Variable
- **Periodo de análisis**: Mensual, Quincenal, Personalizado
- **Configuración de ahorro**: Porcentaje deseado
- **Multi-moneda**: COP, USD, EUR, etc.

### 🏷️ Categorías
- Creación ilimitada de categorías personalizadas
- **Tipos**: Gastos, Ahorros, Inversiones, Deudas
- Personalización visual (colores e iconos)
- Activación/desactivación dinámica

### 💰 Fuentes de Ingreso
- **Salario** - Ingreso fijo
- **Freelance** - Proyectos independientes
- **Préstamos** - Identificados como no-ingreso real
- **Becas y Subsidios**
- **Inversiones**
- **Otros** - Ingresos ocasionales

### 🎯 Metas Financieras
- Objetivos con monto y fecha
- **Prioridades**: Baja, Media, Alta, Crítica
- **Estados**: Activa, Completada, Cancelada, Pausada
- Progreso calculado automáticamente
- Vinculación directa con ahorros

### 📊 Movimientos Financieros (Core)
- **Ingresos** - Todas las entradas de dinero
- **Gastos** - Control de egresos
- **Ahorros** - Vinculados a metas
- **Recurrencia** - Soporte para movimientos periódicos
- **Relaciones inteligentes**:
  - Ingresos ↔ Fuente de Ingreso
  - Gastos ↔ Categoría
  - Ahorros ↔ Meta Financiera

### 📈 Consultas y Análisis
- Listado completo de movimientos
- **Filtros**: Por tipo, fecha, categoría
- Progreso de metas en tiempo real
- Estado financiero actualizado

---

## 🏗️ Arquitectura

Implementación de **Clean Architecture** (Uncle Bob) + **Domain-Driven Design**:

```
📦 com.controfinanciero
│
├── 🎯 domain/                    # Capa de Dominio
│   ├── model/                    # Entidades de negocio
│   │   ├── Usuario
│   │   ├── ContextoFinanciero
│   │   ├── MovimientoFinanciero
│   │   ├── Categoria
│   │   ├── FuenteIngreso
│   │   └── MetaFinanciera
│   ├── repository/               # Interfaces (Ports)
│   ├── service/                  # Servicios de dominio
│   │   ├── CalculadorSaldo
│   │   ├── CalculadorProgresoMeta
│   │   └── GeneradorResumen
│   ├── valueobject/              # Objetos de valor
│   └── exception/                # Excepciones de dominio
│
├── 📋 application/               # Capa de Aplicación
│   ├── dto/                      # Data Transfer Objects
│   └── usecase/                  # Casos de uso
│       ├── usuario/
│       ├── contexto/
│       ├── categoria/
│       ├── fuente/
│       ├── meta/
│       └── movimiento/
│
└── 🔧 infrastructure/            # Capa de Infraestructura
    ├── persistence/              # Persistencia
    │   ├── entity/               # Entidades JPA
    │   ├── repository/           # Repositorios Spring Data
    │   ├── adapter/              # Implementación de Ports
    │   ├── mapper/               # Mappers Domain ↔ Entity
    │   └── converter/            # Converters de ENUMs
    ├── web/                      # API REST
    │   ├── controller/           # Controllers
    │   ├── dto/                  # Request/Response DTOs
    │   └── exception/            # Exception Handlers
    └── config/                   # Configuración
        ├── BeanConfiguration
        ├── DatabaseConfig
        └── PostgreSQLEnumDialect
```

### Principios Aplicados

✅ **Independencia de Frameworks** - Lógica de negocio independiente de Spring  
✅ **Testeable** - Cada capa es testeable de forma aislada  
✅ **Independencia de UI** - Backend puro, múltiples frontends posibles  
✅ **Independencia de BD** - Fácil migración entre bases de datos  
✅ **Mantenible** - Separación clara de responsabilidades  

---

## 🛠️ Stack Tecnológico

### Backend
- **Java 21** - LTS
- **Spring Boot 3.5.9**
- **Spring Data JPA**
- **Hibernate 6.6**
- **Maven 3.9**

### Base de Datos
- **PostgreSQL 17.7**
- **Neon** (PostgreSQL Serverless)
- **ENUMs nativos** de PostgreSQL

### Patrones y Arquitectura
- **Clean Architecture** (Robert C. Martin)
- **Domain-Driven Design** (Eric Evans)
- **Repository Pattern**
- **Use Case Pattern**
- **Adapter Pattern**

### Validación
- **Jakarta Validation**
- **Custom Domain Validations**

---

## 📊 Modelo de Dominio

### Entidades Principales

#### 👤 Usuario
```
- ID único
- Username (único)
- Email (único)
- Password (hash)
- Nombre completo
- Estado: Activo/Inactivo
- Timestamps
```

#### 💼 Contexto Financiero
```
- Usuario (1:1)
- Tipo de ingreso
- Tiene ingreso variable
- % ahorro deseado
- Periodo de análisis
- Código moneda
```

#### 🏷️ Categoría
```
- Usuario (N:1)
- Nombre
- Descripción
- Color (Hex)
- Icono
- Tipo: EXPENSE | SAVINGS | INVESTMENT | DEBT
- Estado: Activa/Inactiva
```

#### 💰 Fuente de Ingreso
```
- Usuario (N:1)
- Nombre
- Descripción
- Tipo: SALARY | FREELANCE | LOAN | SCHOLARSHIP | SUBSIDY | INVESTMENT | OTHER
- Es ingreso real: Sí/No
- Estado: Activa/Inactiva
```

#### 🎯 Meta Financiera
```
- Usuario (N:1)
- Nombre
- Descripción
- Monto objetivo
- Monto actual
- Fecha objetivo
- Prioridad: LOW | MEDIUM | HIGH | CRITICAL
- Estado: ACTIVE | COMPLETED | CANCELLED | PAUSED
```

#### 📊 Movimiento Financiero
```
- Usuario (N:1)
- Tipo: INCOME | EXPENSE | SAVINGS | LOAN | TRANSFER
- Monto
- Descripción
- Fecha movimiento
- Categoría (opcional, N:1)
- Fuente Ingreso (opcional, N:1)
- Meta (opcional, N:1)
- Es recurrente
- Patrón recurrencia
- Notas
```

### Relaciones del Modelo

```
Usuario ←1:1→ ContextoFinanciero
Usuario ←1:N→ Categoría
Usuario ←1:N→ FuenteIngreso
Usuario ←1:N→ MetaFinanciera
Usuario ←1:N→ MovimientoFinanciero

MovimientoFinanciero ←N:1→ Categoría (opcional)
MovimientoFinanciero ←N:1→ FuenteIngreso (opcional)
MovimientoFinanciero ←N:1→ MetaFinanciera (opcional)
```

---

## 🌐 API Endpoints

### Base URL
```
http://localhost:8080/api
```

---

### 👤 Usuarios `/usuarios`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/usuarios` | Crear usuario |
| GET | `/usuarios/{id}` | Obtener usuario |

---

### 💼 Contexto Financiero `/contextos-financieros`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/contextos-financieros` | Crear contexto |
| GET | `/contextos-financieros/usuario/{id}` | Obtener por usuario |

**Tipos de Ingreso**: `MONTHLY` `BIWEEKLY` `WEEKLY` `PROJECT_BASED` `VARIABLE`

---

### 🏷️ Categorías `/categorias`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/categorias` | Crear categoría |
| GET | `/categorias/usuario/{id}` | Todas las categorías |
| GET | `/categorias/usuario/{id}?activas=true` | Solo activas |
| GET | `/categorias/usuario/{id}?tipo=EXPENSE` | Por tipo |

**Tipos**: `EXPENSE` `SAVINGS` `INVESTMENT` `DEBT`

---

### 💰 Fuentes de Ingreso `/fuentes-ingreso`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/fuentes-ingreso` | Crear fuente |
| GET | `/fuentes-ingreso/usuario/{id}` | Todas las fuentes |
| GET | `/fuentes-ingreso/usuario/{id}?activas=true` | Solo activas |
| GET | `/fuentes-ingreso/usuario/{id}?tipo=SALARY` | Por tipo |

**Tipos**: `SALARY` `FREELANCE` `LOAN` `SCHOLARSHIP` `SUBSIDY` `INVESTMENT` `OTHER`

---

### 🎯 Metas Financieras `/metas`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/metas` | Crear meta |
| GET | `/metas/usuario/{id}` | Todas las metas |
| GET | `/metas/usuario/{id}?estado=ACTIVE` | Por estado |

**Prioridades**: `LOW` `MEDIUM` `HIGH` `CRITICAL`  
**Estados**: `ACTIVE` `COMPLETED` `CANCELLED` `PAUSED`

---

### 📊 Movimientos Financieros `/movimientos`

#### Crear Movimiento
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/movimientos` | Registrar movimiento |

#### Consultar Movimientos
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/movimientos/usuario/{id}` | Todos los movimientos |
| GET | `/movimientos/usuario/{id}?tipo=INCOME` | Solo ingresos |
| GET | `/movimientos/usuario/{id}?tipo=EXPENSE` | Solo gastos |
| GET | `/movimientos/usuario/{id}?tipo=SAVINGS` | Solo ahorros |
| GET | `/movimientos/usuario/{id}?fechaInicio=...&fechaFin=...` | Rango de fechas |
| GET | `/movimientos/usuario/{id}?categoriaId=1` | Por categoría |

**Tipos**: `INCOME` `EXPENSE` `SAVINGS` `LOAN` `TRANSFER`

---

## 📦 Colección Postman

Incluye colección completa con **35+ requests** pre-configurados:

```
API-Control-Financiero-Postman-Collection.json
├── 1. Usuarios (2 requests)
├── 2. Contexto Financiero (2 requests)
├── 3. Categorías (6 requests)
├── 4. Fuentes de Ingreso (5 requests)
├── 5. Metas Financieras (6 requests)
└── 6. Movimientos Financieros (14 requests)
    ├── Ingresos (2)
    ├── Gastos (3)
    ├── Ahorros (2)
    └── Consultas (6)
```

---

## 📊 Estado del Proyecto

### Completado (90%)

#### Capa de Dominio ✅ 100%
- 7 Enums
- 4 Value Objects
- 6 Entidades completas
- 3 Servicios de dominio
- 6 Repository interfaces

#### Capa de Infraestructura ✅ 100%
- 6 Entidades JPA
- 6 Repositorios Spring Data
- 6 Adapters
- 6 Mappers Domain ↔ Entity
- 5 Converters para ENUMs PostgreSQL
- Dialect personalizado

#### Capa de Aplicación ✅ 100%
- 12 DTOs
- 12 Use Cases
- Validaciones completas

#### Capa de Presentación ✅ 100%
- 6 Controllers REST
- 12 Request DTOs
- 12 Response DTOs
- Global Exception Handler

#### Base de Datos ✅ 100%
- Esquema PostgreSQL
- ENUMs nativos
- Índices optimizados
- Triggers y funciones
- Vistas calculadas

### En Desarrollo (10%)
- 🔄 Autenticación JWT
- 🔄 Endpoints de estadísticas
- 🔄 Tests unitarios
- 🔄 Swagger/OpenAPI
- 🔄 Docker

---

## 📚 Documentación Adicional

- 📖 `README.md` - Este documento
- 📋 `API_TESTING_GUIDE.md` - Guía de pruebas
- 🏗️ `ARQUITECTURA.md` - Detalles arquitectónicos
- 📊 `API-Control-Financiero-Postman-Collection.json` - Colección Postman

---

## 🎯 Casos de Uso

### Registrar Ingreso Mensual
1. Crear fuente de ingreso tipo `SALARY`
2. Registrar movimiento `INCOME` vinculado
3. Marcar como recurrente `MENSUAL`

### Control de Gastos
1. Crear categorías personalizadas
2. Registrar gastos vinculados
3. Consultar por categoría para análisis

### Ahorrar para Meta
1. Crear meta con monto y fecha
2. Registrar movimientos `SAVINGS`
3. Sistema calcula progreso automático

### Análisis Mensual
1. Consultar movimientos del mes
2. Filtrar por tipo
3. Revisar progreso de metas

---

## 👨‍💻 Autor

**Snayber** - Control Financiero Personal  
GitHub: [@sn4yber](https://github.com/sn4yber)

---

## 📄 Licencia

Proyecto privado - Todos los derechos reservados

---

<div align="center">

**Desarrollado con ❤️ usando Clean Architecture + DDD**

Enero 2026

</div>

