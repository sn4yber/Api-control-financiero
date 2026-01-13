# 🚨 ERROR COMÚN: URL de Base de Datos Incorrecta

## ❌ ERROR

Si ves este error en Render:
```
Driver org.postgresql.Driver claims to not accept jdbcUrl, psql 'postgresql://...
```

## 🔍 CAUSA

Copiaste **literalmente** el comando de conexión de Neon, incluyendo:
- La palabra `psql`
- Las comillas `'`
- Todo el comando completo

## ✅ SOLUCIÓN

### Paso 1: Ve a Render Dashboard
1. Abre tu Web Service
2. Click en **"Environment"** (menú izquierdo)

### Paso 2: Corrige la variable DATABASE_URL

**❌ INCORRECTO** (lo que probablemente pusiste):
```
psql 'postgresql://neondb_owner:npg_5OimKyqF9sIX@ep-dawn-unit-adn7096y-pooler.c-2.us-east-1.aws.neon.tech/neondb?sslmode=require&channel_binding=require'
```

**✅ CORRECTO** (lo que DEBES poner):
```
postgresql://neondb_owner:npg_5OimKyqF9sIX@ep-dawn-unit-adn7096y-pooler.c-2.us-east-1.aws.neon.tech/neondb?sslmode=require
```

### Paso 3: Guarda y espera

1. Click en **"Save Changes"**
2. Render redeslegará automáticamente (~5 minutos)
3. Verifica en los logs que inicie correctamente

---

## 📝 REGLAS PARA DATABASE_URL

### ✅ Debe incluir:
- `postgresql://` al inicio
- `usuario:password@host/database`
- `?sslmode=require` al final

### ❌ NO debe incluir:
- ❌ La palabra `psql`
- ❌ Comillas simples `'` o dobles `"`
- ❌ `&channel_binding=require` (causa problemas)
- ❌ Espacios en blanco

---

## 🎯 CÓMO COPIAR CORRECTAMENTE DESDE NEON

Cuando Neon te da esta línea:
```bash
psql 'postgresql://user:pass@host/db?params'
```

**Solo copia** lo que está DENTRO de las comillas:
```
postgresql://user:pass@host/db?params
```

Y **elimina** `&channel_binding=require` si aparece.

---

## 🧪 PROBAR LA URL LOCALMENTE

Puedes probar que la URL es correcta en tu máquina:

```bash
# En WSL/Linux/Mac
psql "postgresql://neondb_owner:npg_5OimKyqF9sIX@ep-dawn-unit-adn7096y-pooler.c-2.us-east-1.aws.neon.tech/neondb?sslmode=require"

# O en Java/Spring Boot
export DATABASE_URL="postgresql://neondb_owner:npg_5OimKyqF9sIX@ep-dawn-unit-adn7096y-pooler.c-2.us-east-1.aws.neon.tech/neondb?sslmode=require"
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

Si funciona localmente, funcionará en Render.

---

## 🔄 OTRAS VARIABLES DE ENTORNO

Asegúrate de tener TODAS estas variables en Render:

```
DATABASE_URL=postgresql://neondb_owner:npg_5OimKyqF9sIX@ep-dawn-unit-adn7096y-pooler.c-2.us-east-1.aws.neon.tech/neondb?sslmode=require

SPRING_PROFILES_ACTIVE=prod

JAVA_OPTS=-Xms256m -Xmx512m
```

---

## ✅ CHECKLIST DESPUÉS DE CORREGIR

- [ ] DATABASE_URL sin `psql` al inicio
- [ ] DATABASE_URL sin comillas
- [ ] DATABASE_URL sin `&channel_binding=require`
- [ ] SPRING_PROFILES_ACTIVE configurado a `prod`
- [ ] Health Check Path configurado: `/actuator/health`
- [ ] Render redeslegando automáticamente
- [ ] Logs muestran: "Started ControlFinacieroApplication"
- [ ] `/actuator/health` responde con `{"status":"UP"}`

---

¡Con estos cambios tu API debería desplegar correctamente! 🚀

