package com.controfinanciero.infrastructure.scheduler;

import com.controfinanciero.domain.model.Usuario;
import com.controfinanciero.domain.model.enums.TipoMovimiento;
import com.controfinanciero.domain.repository.*;
import com.controfinanciero.infrastructure.persistence.entity.*;
import com.controfinanciero.infrastructure.persistence.repository.MovimientoFinancieroJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

/**
 * 🤖 MOTOR DE AUTOMATIZACIÓN
 * Ejecuta tareas programadas: movimientos recurrentes, alertas, reportes
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AutomationScheduler {

    private final MovimientoRecurrenteRepository movimientoRecurrenteRepo;
    private final MovimientoFinancieroJpaRepository movimientoFinancieroJpaRepo;
    private final PresupuestoRepository presupuestoRepo;
    private final NotificacionRepository notificacionRepo;
    private final UsuarioRepository usuarioRepo;

    /**
     * 🔄 Ejecuta movimientos recurrentes cada día a las 00:00
     * Cron: Cada día a medianoche
     */
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void procesarMovimientosRecurrentes() {
        log.info("🔄 Iniciando procesamiento de movimientos recurrentes...");

        LocalDate hoy = LocalDate.now();
        List<MovimientoRecurrenteEntity> pendientes = movimientoRecurrenteRepo.findPendientesDeEjecucion(hoy);

        log.info("📋 Encontrados {} movimientos recurrentes pendientes", pendientes.size());

        for (MovimientoRecurrenteEntity recurrente : pendientes) {
            try {
                // Convertir el tipo de movimiento de String a enum
                TipoMovimiento tipoMovimiento = TipoMovimiento.valueOf(recurrente.getTipoMovimiento());

                // Crear el movimiento financiero automáticamente
                MovimientoFinancieroEntity movimiento = new MovimientoFinancieroEntity(
                        recurrente.getUsuarioId(),
                        tipoMovimiento,
                        recurrente.getMonto(),
                        "🤖 AUTOMÁTICO: " + recurrente.getDescripcion(),
                        hoy
                );
                movimiento.setCategoryId(recurrente.getCategoriaId());
                movimiento.setIncomeSourceId(recurrente.getFuenteIngresoId());
                movimiento.setGoalId(recurrente.getMetaId());
                movimiento.setIsRecurring(false);
                movimiento.setNotes("Generado automáticamente desde movimiento recurrente #" + recurrente.getId());

                // Usar JPA repository directamente
                movimientoFinancieroJpaRepo.save(movimiento);

                // Actualizar próxima ejecución
                recurrente.setUltimaEjecucion(LocalDateTime.now());
                calcularProximaEjecucion(recurrente);
                movimientoRecurrenteRepo.save(recurrente);

                // Crear notificación
                crearNotificacion(
                        recurrente.getUsuarioId(),
                        "MOVIMIENTO_AUTOMATICO",
                        "Movimiento automático registrado",
                        String.format("Se ha registrado automáticamente: %s por $%s",
                                recurrente.getDescripcion(), recurrente.getMonto())
                );

                log.info("✅ Movimiento recurrente ejecutado: {} - ${}", recurrente.getDescripcion(), recurrente.getMonto());

            } catch (Exception e) {
                log.error("❌ Error al procesar movimiento recurrente #{}: {}", recurrente.getId(), e.getMessage(), e);
            }
        }

        log.info("✅ Procesamiento de movimientos recurrentes completado");
    }

    /**
     * Calcula la próxima ejecución de un movimiento recurrente
     */
    private void calcularProximaEjecucion(MovimientoRecurrenteEntity recurrente) {
        LocalDate ultimaFecha = recurrente.getUltimaEjecucion().toLocalDate();

        switch (recurrente.getFrecuencia()) {
            case "DIARIA":
                recurrente.setProximaEjecucion(ultimaFecha.plusDays(1));
                break;
            case "SEMANAL":
                recurrente.setProximaEjecucion(ultimaFecha.plusWeeks(1));
                break;
            case "QUINCENAL":
                recurrente.setProximaEjecucion(ultimaFecha.plusDays(15));
                break;
            case "MENSUAL":
                recurrente.setProximaEjecucion(ultimaFecha.plusMonths(1));
                break;
            case "ANUAL":
                recurrente.setProximaEjecucion(ultimaFecha.plusYears(1));
                break;
            default:
                recurrente.setProximaEjecucion(ultimaFecha.plusMonths(1));
        }

        // Si hay fecha fin y la próxima ejecución la supera, desactivar
        if (recurrente.getFechaFin() != null && recurrente.getProximaEjecucion().isAfter(recurrente.getFechaFin())) {
            recurrente.setActivo(false);
        }
    }

    /**
     * 🚨 Verifica presupuestos y envía alertas cada día a las 20:00
     * Cron: Cada día a las 8pm
     */
    @Scheduled(cron = "0 0 20 * * *")
    @Transactional
    public void verificarPresupuestos() {
        log.info("🚨 Verificando presupuestos...");

        List<PresupuestoEntity> presupuestosParaAlertar = presupuestoRepo.findPresupuestosParaAlertar();

        log.info("📊 Encontrados {} presupuestos que requieren alerta", presupuestosParaAlertar.size());

        for (PresupuestoEntity presupuesto : presupuestosParaAlertar) {
            try {
                BigDecimal porcentaje = calcularPorcentajeUso(presupuesto);
                boolean excedido = presupuesto.getGastoActual().compareTo(presupuesto.getLimiteMensual()) >= 0;

                String titulo = excedido
                        ? "⚠️ Presupuesto excedido"
                        : "⚠️ Cerca del límite de presupuesto";

                String mensaje = String.format(
                        "Has gastado $%s de $%s en %s (%s%%). %s",
                        presupuesto.getGastoActual(),
                        presupuesto.getLimiteMensual(),
                        presupuesto.getCategoriaNombre(),
                        porcentaje.intValue(),
                        excedido ? "¡Has superado tu límite!" : "Estás cerca de tu límite."
                );

                crearNotificacion(
                        presupuesto.getUsuarioId(),
                        "PRESUPUESTO_ALERTA",
                        titulo,
                        mensaje
                );

                presupuesto.setAlertaEnviada(true);
                presupuestoRepo.save(presupuesto);

                log.info("🚨 Alerta enviada para presupuesto #{}: {} ({}%)",
                        presupuesto.getId(), presupuesto.getCategoriaNombre(), porcentaje.intValue());

            } catch (Exception e) {
                log.error("❌ Error al verificar presupuesto #{}: {}", presupuesto.getId(), e.getMessage(), e);
            }
        }

        log.info("✅ Verificación de presupuestos completada");
    }

    /**
     * Calcula el porcentaje de uso del presupuesto
     */
    private BigDecimal calcularPorcentajeUso(PresupuestoEntity presupuesto) {
        if (presupuesto.getLimiteMensual() == null || presupuesto.getLimiteMensual().compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return presupuesto.getGastoActual()
                .divide(presupuesto.getLimiteMensual(), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    /**
     * 📧 Envía resumen mensual el día 1 de cada mes a las 08:00
     * Cron: Día 1 de cada mes a las 8am
     */
    @Scheduled(cron = "0 0 8 1 * *")
    @Transactional
    public void enviarResumenMensual() {
        log.info("📧 Iniciando envío de resúmenes mensuales...");

        YearMonth mesAnterior = YearMonth.now().minusMonths(1);

        List<Usuario> usuarios = usuarioRepo.findByActiveTrue();

        log.info("👥 Enviando resumen a {} usuarios", usuarios.size());

        for (Usuario usuario : usuarios) {
            try {
                // Aquí se integraría con el servicio de Email
                // Por ahora, creamos una notificación
                crearNotificacion(
                        usuario.getId(),
                        "RESUMEN_MENSUAL",
                        "📊 Resumen financiero de " + mesAnterior.getMonth(),
                        "Tu resumen mensual está disponible. Revisa tus ingresos, gastos y progreso de metas."
                );

                log.info("📧 Resumen enviado a usuario #{}: {}", usuario.getId(), usuario.getEmail());

            } catch (Exception e) {
                log.error("❌ Error al enviar resumen a usuario #{}: {}", usuario.getId(), e.getMessage(), e);
            }
        }

        log.info("✅ Envío de resúmenes mensuales completado");
    }

    /**
     * 🧹 Limpia notificaciones leídas antiguas cada semana (domingo a las 02:00)
     * Cron: Cada domingo a las 2am
     */
    @Scheduled(cron = "0 0 2 * * SUN")
    @Transactional
    public void limpiarNotificacionesAntiguas() {
        log.info("🧹 Limpiando notificaciones antiguas...");

        List<Usuario> usuarios = usuarioRepo.findByActiveTrue();

        for (Usuario usuario : usuarios) {
            try {
                notificacionRepo.deleteByUsuarioIdAndLeidaTrue(usuario.getId());
            } catch (Exception e) {
                log.error("❌ Error al limpiar notificaciones del usuario #{}: {}", usuario.getId(), e.getMessage(), e);
            }
        }

        log.info("✅ Limpieza de notificaciones completada");
    }

    /**
     * Helper para crear notificaciones
     */
    private void crearNotificacion(Long usuarioId, String tipo, String titulo, String mensaje) {
        NotificacionEntity notificacion = new NotificacionEntity();
        notificacion.setUsuarioId(usuarioId);
        notificacion.setTipo(tipo);
        notificacion.setTitulo(titulo);
        notificacion.setMensaje(mensaje);
        notificacion.setLeida(false);
        notificacion.setFechaEnvio(LocalDateTime.now());

        notificacionRepo.save(notificacion);
    }
}

