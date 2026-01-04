# 🏗️ Arquitectura del Proyecto - Control Financiero

## 📐 Principios Aplicados

### Clean Architecture
- **Dependencias hacia adentro**: Domain no depende de nada, Application depende de Domain, Infrastructure depende de Application y Domain
- **Reglas de negocio en el centro**: El dominio es independiente de frameworks y detalles técnicos

### Hexagonal Architecture (Ports & Adapters)
- **Ports**: Interfaces en el dominio que definen contratos
- **Adapters**: Implementaciones en infrastructure que adaptan tecnologías específicas

### The Pragmatic Programmer
- **DRY (Don't Repeat Yourself)**: Sin duplicación de lógica
- **Orthogonality**: Componentes independientes y desacoplados
- **Reversibility**: Decisiones técnicas fáciles de cambiar

### Clean Code
- **Nombres descriptivos**: Variables, métodos y clases con nombres que expresan intención
- **Funciones pequeñas**: Una responsabilidad por función
- **Comentarios significativos**: Solo cuando el código no puede expresarse por sí mismo

---

## 📂 Estructura de Carpetas

```
com.controfinanciero/
│
├── domain/                          # CAPA DE DOMINIO (núcleo del negocio)
│   ├── model/                       # Entidades de dominio (objetos de negocio puros)
│   │   ├── Usuario.java
│   │   ├── ContextoFinanciero.java
│   │   ├── MovimientoFinanciero.java
│   │   ├── Categoria.java
│   │   ├── FuenteIngreso.java
│   │   ├── MetaFinanciera.java
│   │   └── ContribucionMeta.java
│   │
│   ├── valueobject/                 # Value Objects (objetos inmutables)
│   │   ├── ResumenFinanciero.java
│   │   ├── ProgresoMeta.java
│   │   ├── Dinero.java
│   │   └── Periodo.java
│   │
│   ├── repository/                  # PORTS - Interfaces de repositorio
│   │   ├── UsuarioRepository.java
│   │   ├── MovimientoFinancieroRepository.java
│   │   ├── CategoriaRepository.java
│   │   ├── FuenteIngresoRepository.java
│   │   └── MetaFinancieraRepository.java
│   │
│   ├── service/                     # Domain Services (lógica que no pertenece a una entidad)
│   │   ├── CalculadorSaldo.java
│   │   ├── CalculadorProgresoMeta.java
│   │   └── GeneradorResumen.java
│   │
│   └── exception/                   # Excepciones de dominio
│       ├── DomainException.java
│       ├── UsuarioNoEncontradoException.java
│       ├── MovimientoInvalidoException.java
│       └── MetaNoEncontradaException.java
│
├── application/                     # CAPA DE APLICACIÓN (casos de uso)
│   ├── usecase/                     # Casos de uso (orquestación de lógica de negocio)
│   │   ├── usuario/
│   │   │   ├── CrearUsuarioUseCase.java
│   │   │   ├── ObtenerUsuarioUseCase.java
│   │   │   └── ConfigurarContextoFinancieroUseCase.java
│   │   │
│   │   ├── movimiento/
│   │   │   ├── RegistrarIngresoUseCase.java
│   │   │   ├── RegistrarGastoUseCase.java
│   │   │   ├── RegistrarAhorroUseCase.java
│   │   │   └── ObtenerMovimientosUseCase.java
│   │   │
│   │   ├── meta/
│   │   │   ├── CrearMetaFinancieraUseCase.java
│   │   │   ├── ConsultarProgresoMetaUseCase.java
│   │   │   ├── ActualizarMetaUseCase.java
│   │   │   └── CompletarMetaUseCase.java
│   │   │
│   │   └── resumen/
│   │       ├── GenerarResumenMensualUseCase.java
│   │       ├── GenerarResumenQuincenalUseCase.java
│   │       └── GenerarResumenPersonalizadoUseCase.java
│   │
│   ├── dto/                         # DTOs de application (input/output de casos de uso)
│   │   ├── RegistrarIngresoCommand.java
│   │   ├── RegistrarGastoCommand.java
│   │   ├── CrearMetaCommand.java
│   │   └── ResumenFinancieroDTO.java
│   │
│   └── mapper/                      # Mappers entre Domain y DTOs
│       ├── MovimientoMapper.java
│       └── MetaMapper.java
│
├── infrastructure/                  # CAPA DE INFRAESTRUCTURA (detalles técnicos)
│   ├── persistence/                 # Adaptador de persistencia
│   │   ├── entity/                  # Entidades JPA (modelo de BD)
│   │   │   ├── UsuarioEntity.java
│   │   │   ├── ContextoFinancieroEntity.java
│   │   │   ├── MovimientoFinancieroEntity.java
│   │   │   ├── CategoriaEntity.java
│   │   │   ├── FuenteIngresoEntity.java
│   │   │   ├── MetaFinancieraEntity.java
│   │   │   └── ContribucionMetaEntity.java
│   │   │
│   │   ├── repository/              # JPA Repositories (Spring Data)
│   │   │   ├── UsuarioJpaRepository.java
│   │   │   ├── MovimientoFinancieroJpaRepository.java
│   │   │   ├── CategoriaJpaRepository.java
│   │   │   ├── FuenteIngresoJpaRepository.java
│   │   │   └── MetaFinancieraJpaRepository.java
│   │   │
│   │   └── adapter/                 # ADAPTERS - Implementaciones de ports
│   │       ├── UsuarioRepositoryAdapter.java
│   │       ├── MovimientoFinancieroRepositoryAdapter.java
│   │       ├── CategoriaRepositoryAdapter.java
│   │       ├── FuenteIngresoRepositoryAdapter.java
│   │       └── MetaFinancieraRepositoryAdapter.java
│   │
│   ├── web/                         # Adaptador web (REST API)
│   │   ├── controller/              # Controllers REST
│   │   │   ├── UsuarioController.java
│   │   │   ├── MovimientoController.java
│   │   │   ├── MetaController.java
│   │   │   └── ResumenController.java
│   │   │
│   │   ├── dto/                     # DTOs de API (Request/Response)
│   │   │   ├── request/
│   │   │   │   ├── CrearUsuarioRequest.java
│   │   │   │   ├── RegistrarIngresoRequest.java
│   │   │   │   ├── RegistrarGastoRequest.java
│   │   │   │   └── CrearMetaRequest.java
│   │   │   │
│   │   │   └── response/
│   │   │       ├── UsuarioResponse.java
│   │   │       ├── MovimientoResponse.java
│   │   │       ├── MetaResponse.java
│   │   │       └── ResumenFinancieroResponse.java
│   │   │
│   │   └── mapper/                  # Mappers entre Web DTOs y Application DTOs
│   │       ├── UsuarioWebMapper.java
│   │       ├── MovimientoWebMapper.java
│   │       └── MetaWebMapper.java
│   │
│   └── config/                      # Configuraciones de Spring
│       ├── DatabaseConfig.java      # ✅ Ya existe
│       ├── BeanConfiguration.java   # Inyección de dependencias
│       └── WebConfig.java           # Configuración web (CORS, etc.)
│
└── shared/                          # CAPA COMPARTIDA (utilidades transversales)
    ├── util/                        # Utilidades genéricas
    │   ├── DateUtils.java
    │   └── ValidationUtils.java
    │
    └── exception/                   # Manejo global de excepciones
        ├── GlobalExceptionHandler.java
        └── ApiError.java

```

---

## 🔄 Flujo de Datos (Ejemplo: Registrar Ingreso)

```
1. HTTP Request → UsuarioController (infrastructure/web)
                     ↓
2. Request DTO → WebMapper → Application Command
                     ↓
3. RegistrarIngresoUseCase (application)
                     ↓
4. Domain Model (Usuario, MovimientoFinanciero)
                     ↓
5. Repository Port (domain/repository interface)
                     ↓
6. Repository Adapter (infrastructure/persistence)
                     ↓
7. JPA Repository → Database (Neon PostgreSQL)
```

---

## 📦 Enums del Sistema

Todos los enums deben estar en el dominio:

```
domain/model/enums/
├── TipoIngreso.java        → MONTHLY, BIWEEKLY, WEEKLY, PROJECT_BASED, VARIABLE
├── PeriodoAnalisis.java    → MONTHLY, BIWEEKLY, CUSTOM
├── TipoFuente.java         → SALARY, FREELANCE, LOAN, SCHOLARSHIP, SUBSIDY, INVESTMENT, OTHER
├── TipoCategoria.java      → EXPENSE, SAVINGS, INVESTMENT, DEBT
├── Prioridad.java          → LOW, MEDIUM, HIGH, CRITICAL
├── EstadoMeta.java         → ACTIVE, COMPLETED, CANCELLED, PAUSED
└── TipoMovimiento.java     → INCOME, EXPENSE, SAVINGS, LOAN, TRANSFER
```

---

## ✅ Reglas de Dependencia

### ❌ PROHIBIDO:
- Domain NO puede importar nada de Application, Infrastructure o Shared
- Application NO puede importar nada de Infrastructure
- Infrastructure puede importar de Domain y Application

### ✅ PERMITIDO:
```
Domain ← Application ← Infrastructure
  ↑                        ↑
  └────────── Shared ──────┘
```

---

## 🎯 Convenciones de Nombres

### Entidades de Dominio
- Sustantivos en singular
- Nombres de negocio, no técnicos
- Ejemplo: `Usuario`, `MovimientoFinanciero`, `MetaFinanciera`

### Repositorios (Ports)
- Nombre de entidad + `Repository`
- Ejemplo: `UsuarioRepository`, `MovimientoFinancieroRepository`

### Casos de Uso
- Verbo + Sustantivo + `UseCase`
- Ejemplo: `RegistrarIngresoUseCase`, `ConsultarProgresoMetaUseCase`

### Adapters
- Nombre del port + `Adapter`
- Ejemplo: `UsuarioRepositoryAdapter`

### Controllers
- Nombre del recurso + `Controller`
- Ejemplo: `UsuarioController`, `MovimientoController`

### DTOs
- Request: `[Acción][Recurso]Request`
- Response: `[Recurso]Response`
- Ejemplo: `CrearUsuarioRequest`, `UsuarioResponse`

---

## 🧪 Testing (Estructura futura)

```
test/java/com/controfinanciero/
├── domain/                  # Tests unitarios de dominio
├── application/             # Tests de casos de uso
└── infrastructure/          # Tests de integración
```

---

## 📝 Notas Importantes

1. **Sin lógica en controllers**: Solo reciben requests y delegan a casos de uso
2. **Sin lógica en adapters**: Solo transforman datos entre capas
3. **Sin anotaciones de Spring en Domain**: El dominio debe ser framework-agnostic
4. **Value Objects inmutables**: Usar records de Java cuando sea posible
5. **Validaciones en capas**:
   - Web: Validaciones de formato (@Valid, @NotNull, etc.)
   - Application: Validaciones de reglas de negocio
   - Domain: Invariantes del modelo

---

## 🚀 Orden de Implementación Recomendado

1. ✅ **Enums** → Base del sistema
2. ✅ **Value Objects** → Objetos inmutables reutilizables
3. ✅ **Entidades de Dominio** → Modelo de negocio
4. ✅ **Repository Ports** → Contratos de persistencia
5. ✅ **Domain Services** → Lógica de dominio compartida
6. ✅ **Entidades JPA** → Modelo de base de datos
7. ✅ **Repository Adapters** → Implementación de persistencia
8. ✅ **Casos de Uso** → Lógica de aplicación
9. ✅ **Controllers & DTOs** → API REST
10. ✅ **Exception Handling** → Manejo de errores

---

**Creado por**: Snayber & GitHub Copilot
**Fecha**: 2026-01-04
**Versión**: 1.0

