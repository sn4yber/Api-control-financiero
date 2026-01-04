# 🏦 Control Financiero - API REST

Sistema de control financiero personal construido con **Clean Architecture** y **Spring Boot**.

---

## 🚀 Configuración Inicial

### 1️⃣ Requisitos Previos
- **Java 21**
- **Maven 3.x**
- **Cuenta en Neon PostgreSQL** (ya configurada)

### 2️⃣ Configurar Credenciales de Neon

#### Opción A: Usando variables de entorno (Recomendado)
1. Copia el archivo de ejemplo:
   ```bash
   cp .env.example .env
   ```

2. Edita `.env` con tus credenciales reales de Neon:
   ```properties
   NEON_HOST=tu-proyecto.neon.tech
   DATABASE_NAME=neondb
   NEON_USERNAME=tu-usuario
   NEON_PASSWORD=tu-password
   ```

3. **IMPORTANTE**: El archivo `.env` está en `.gitignore` y nunca se debe subir al repositorio.

#### Opción B: Directamente en application.properties
Edita `src/main/resources/application.properties` y reemplaza los placeholders:

```properties
spring.datasource.url=jdbc:postgresql://TU_HOST.neon.tech/TU_DATABASE?sslmode=require
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_PASSWORD
```

⚠️ **Advertencia**: Si usas esta opción, ten cuidado de no commitear tus credenciales.

### 3️⃣ Instalar Dependencias
```bash
cd demo
mvn clean install
```

### 4️⃣ Ejecutar la Aplicación
```bash
mvn spring-boot:run
```

Si la conexión es exitosa, verás en la consola:
```
✅ Conexión exitosa a la base de datos Neon PostgreSQL
📊 Database: neondb
🔗 URL: jdbc:postgresql://...
👤 Usuario: tu-usuario
```

---

## 📐 Arquitectura del Proyecto

Seguimos **Clean Architecture** con separación clara de capas:

```
com.controfinanciero
├── domain/              → Entidades y reglas de negocio puras
│   ├── model/           → Entidades del dominio
│   └── repository/      → Interfaces de repositorio (ports)
│
├── application/         → Casos de uso
│   └── usecase/         → Implementación de lógica de negocio
│
├── infrastructure/      → Detalles de implementación
│   ├── persistence/     → JPA entities & repositories
│   ├── web/             → Controllers REST
│   └── config/          → Configuraciones Spring
│
└── shared/              → Utilidades transversales
```

### Principios Aplicados
- ✅ **Clean Code**: Código legible y mantenible
- ✅ **Clean Architecture**: Dependencias hacia adentro
- ✅ **SOLID**: Diseño orientado a objetos
- ✅ **DRY**: Don't Repeat Yourself
- ✅ **Hexagonal Architecture**: Puertos y adaptadores

---

## 🗄️ Modelo de Base de Datos

La base de datos en **Neon PostgreSQL** incluye:

### Tablas Principales:
- `users` - Usuarios del sistema
- `financial_contexts` - Configuración financiera por usuario
- `financial_movements` - Registro de movimientos (ingresos/gastos)
- `categories` - Categorías de movimientos
- `income_sources` - Fuentes de ingreso
- `financial_goals` - Metas financieras
- `goal_contributions` - Contribuciones a metas

### ENUMs Personalizados:
- `income_type_enum`: MONTHLY, BIWEEKLY, WEEKLY, PROJECT_BASED, VARIABLE
- `movement_type_enum`: INCOME, EXPENSE, SAVINGS, LOAN, TRANSFER
- `goal_status_enum`: ACTIVE, COMPLETED, CANCELLED, PAUSED
- Y más...

---

## 🧪 Verificar la Conexión

Una vez ejecutada la aplicación, puedes verificar que todo funciona correctamente:

1. **Revisa los logs** en la consola buscando el mensaje de conexión exitosa
2. **Prueba el endpoint de health** (cuando esté implementado):
   ```bash
   curl http://localhost:8080/actuator/health
   ```

---

## 🛠️ Tecnologías

- **Spring Boot 3.5.9**
- **Java 21**
- **Spring Data JPA**
- **PostgreSQL** (Neon)
- **Lombok**
- **Maven**

---

## 📝 Progreso del Proyecto

### ✅ Completado
- [x] **Capa de Dominio (100%)**
  - [x] 7 Enums (TipoIngreso, PeriodoAnalisis, TipoMovimiento, Prioridad, TipoFuente, TipoCategoria, EstadoMeta)
  - [x] 4 Value Objects (Dinero, Periodo, ResumenFinanciero, ProgresoMeta)
  - [x] 6 Entidades (Usuario, ContextoFinanciero, MovimientoFinanciero, Categoria, FuenteIngreso, MetaFinanciera)
  - [x] 5 Repository Ports (Interfaces)
- [x] Conexión a Neon PostgreSQL funcionando
- [x] Estructura Clean Architecture implementada

### 🚧 En Desarrollo
- [ ] Capa de Infraestructura
  - [ ] Entidades JPA
  - [ ] Implementación de repositorios (Adapters)
- [ ] Capa de Aplicación
  - [ ] Casos de uso
  - [ ] DTOs y Mappers
- [ ] Capa de Presentación
  - [ ] Controllers REST
  - [ ] Manejo de excepciones
  - [ ] Validaciones

### 🔜 Próximos Pasos
- [ ] Implementar entidades JPA
- [ ] Crear adaptadores de repositorio
- [ ] Implementar casos de uso principales
- [ ] Desarrollar endpoints REST
- [ ] Agregar seguridad (JWT)
- [ ] Documentación con Swagger/OpenAPI

---

## 👨‍💻 Desarrollador

**Snayber** - Control Financiero Personal

---

## 📄 Licencia

Proyecto privado.

