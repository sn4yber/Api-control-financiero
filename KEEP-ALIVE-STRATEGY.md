# 🔥 Keep-Alive Strategy for Render Free Tier

## ❌ **PROBLEMA**
Render Free Tier hiberna tu servicio después de 15 minutos de inactividad, causando **cold starts de 5-6 minutos**.

## ✅ **SOLUCIÓN IMPLEMENTADA**

### 1. **Endpoint Ultra-Ligero `/api/ping`**
- No requiere autenticación
- No accede a la base de datos
- Responde en < 50ms
- Mantiene el servicio "caliente"

### 2. **Integración en tu Frontend**

#### **React/Angular/Vue - Servicio Keep-Alive**

```typescript
// services/keepAlive.service.ts
const API_URL = 'https://api-control-financiero.onrender.com';
const PING_INTERVAL = 10 * 60 * 1000; // 10 minutos

class KeepAliveService {
  private intervalId: number | null = null;

  /**
   * 🔥 Inicia el keep-alive automático
   * Llama a /api/ping cada 10 minutos
   */
  start() {
    if (this.intervalId) return; // Ya está corriendo

    console.log('🔥 Keep-Alive iniciado');
    
    // Primera llamada inmediata
    this.ping();

    // Luego cada 10 minutos
    this.intervalId = window.setInterval(() => {
      this.ping();
    }, PING_INTERVAL);
  }

  /**
   * 🛑 Detiene el keep-alive
   */
  stop() {
    if (this.intervalId) {
      clearInterval(this.intervalId);
      this.intervalId = null;
      console.log('🛑 Keep-Alive detenido');
    }
  }

  /**
   * 💚 Hace ping al servidor
   */
  private async ping() {
    try {
      const response = await fetch(`${API_URL}/api/ping`, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' }
      });
      
      if (response.ok) {
        const data = await response.json();
        console.log('✅ Ping exitoso:', data.uptime_seconds, 'segundos activo');
      }
    } catch (error) {
      console.warn('⚠️ Ping falló (probablemente cold start):', error);
    }
  }
}

export const keepAliveService = new KeepAliveService();
```

#### **Inicializar en tu App Principal**

```typescript
// App.tsx / main.ts
import { keepAliveService } from './services/keepAlive.service';

// Al iniciar la app
useEffect(() => {
  keepAliveService.start();
  
  return () => {
    keepAliveService.stop();
  };
}, []);
```

### 3. **Alternativa: CRON Job Externo (GRATIS)**

Si no quieres implementarlo en el frontend, usa **cron-job.org** (gratis):

1. Registrate en https://cron-job.org
2. Crea un nuevo job:
   - URL: `https://api-control-financiero.onrender.com/api/ping`
   - Schedule: Cada 10 minutos (`*/10 * * * *`)
   - Method: GET
3. ✅ Activa el job

### 4. **Monitoreo con UptimeRobot (GRATIS)**

Configura un monitor en https://uptimerobot.com:
- URL: `https://api-control-financiero.onrender.com/api/health`
- Interval: 5 minutos
- Tipo: HTTP(s)

## 📊 **RESULTADOS ESPERADOS**

| Métrica | Antes | Después |
|---------|-------|---------|
| Cold Start | 5-6 min | 30-45 seg |
| Warm Response | 200-500ms | 50-150ms |
| Hibernación | Cada 15 min | Nunca (con keep-alive) |

## 🚀 **PRÓXIMOS PASOS**

1. Despliega el backend con `git push`
2. Implementa el servicio de keep-alive en tu frontend
3. (Opcional) Configura cron-job.org como respaldo
4. Monitorea con UptimeRobot

## 📝 **NOTAS**

- El endpoint `/api/ping` NO consume recursos de DB
- Compatible con el plan Free de Render (750 horas/mes)
- Si superas las 750 horas, considera cron-job.org solo de lunes a viernes

