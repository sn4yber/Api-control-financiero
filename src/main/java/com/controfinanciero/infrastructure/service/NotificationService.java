package com.controfinanciero.infrastructure.service;

import com.controfinanciero.domain.repository.NotificacionRepository;
import com.controfinanciero.infrastructure.persistence.entity.NotificacionEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 🔔 Servicio de Notificaciones
 * Genera y gestiona notificaciones automáticas del sistema
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificacionRepository notificacionRepository;

    /**
     * Tipos de notificación
     */
    public static class TipoNotificacion {
        public static final String PRESUPUESTO_EXCEDIDO = "PRESUPUESTO_EXCEDIDO";
        public static final String PRESUPUESTO_CERCA_LIMITE = "PRESUPUESTO_CERCA_LIMITE";
        public static final String META_PROGRESO = "META_PROGRESO";
        public static final String META_COMPLETADA = "META_COMPLETADA";
        public static final String RECORDATORIO_PAGO = "RECORDATORIO_PAGO";
        public static final String MOVIMIENTO_INUSUAL = "MOVIMIENTO_INUSUAL";
        public static final String RESUMEN_SEMANAL = "RESUMEN_SEMANAL";
        public static final String RESUMEN_MENSUAL = "RESUMEN_MENSUAL";
    }

    /**
     * Crea una notificación genérica
     */
    @Transactional
    public NotificacionEntity crearNotificacion(
            Long usuarioId,
            String tipo,
            String titulo,
            String mensaje
    ) {
        NotificacionEntity notificacion = new NotificacionEntity();
        notificacion.setUsuarioId(usuarioId);
        notificacion.setTipo(tipo);
        notificacion.setTitulo(titulo);
        notificacion.setMensaje(mensaje);
        notificacion.setLeida(false);
        notificacion.setFechaEnvio(LocalDateTime.now());

        NotificacionEntity saved = notificacionRepository.save(notificacion);
        log.info("📬 Notificación creada - Usuario: {}, Tipo: {}", usuarioId, tipo);

        return saved;
    }

    /**
     * 🚨 Alerta: Presupuesto excedido
     */
    @Transactional
    public void alertarPresupuestoExcedido(
            Long usuarioId,
            String categoriaNombre,
            Double gastado,
            Double limite,
            Double porcentaje
    ) {
        String titulo = String.format("⚠️ Presupuesto Excedido: %s", categoriaNombre);
        String mensaje = String.format(
                "Has excedido tu presupuesto de %s.\n" +
                "Gastado: $%.2f de $%.2f (%.0f%%)\n" +
                "Te recomendamos reducir gastos en esta categoría.",
                categoriaNombre, gastado, limite, porcentaje
        );

        crearNotificacion(usuarioId, TipoNotificacion.PRESUPUESTO_EXCEDIDO, titulo, mensaje);
    }

    /**
     * ⚠️ Alerta: Cerca del límite de presupuesto (80%)
     */
    @Transactional
    public void alertarPresupuestoCercaLimite(
            Long usuarioId,
            String categoriaNombre,
            Double gastado,
            Double limite,
            Double porcentaje
    ) {
        String titulo = String.format("⚠️ Presupuesto en %.0f%%: %s", porcentaje, categoriaNombre);
        String mensaje = String.format(
                "Estás cerca del límite de tu presupuesto de %s.\n" +
                "Gastado: $%.2f de $%.2f (%.0f%%)\n" +
                "Quedan $%.2f disponibles.",
                categoriaNombre, gastado, limite, porcentaje, (limite - gastado)
        );

        crearNotificacion(usuarioId, TipoNotificacion.PRESUPUESTO_CERCA_LIMITE, titulo, mensaje);
    }

    /**
     * 🎯 Notificación: Progreso de meta
     */
    @Transactional
    public void notificarProgresoMeta(
            Long usuarioId,
            String metaNombre,
            Double ahorrado,
            Double objetivo,
            Double porcentaje
    ) {
        String titulo = String.format("🎯 Progreso de Meta: %s", metaNombre);
        String mensaje = String.format(
                "¡Buen trabajo! Has alcanzado el %.0f%% de tu meta '%s'.\n" +
                "Ahorrado: $%.2f de $%.2f\n" +
                "Te faltan $%.2f para completarla.",
                porcentaje, metaNombre, ahorrado, objetivo, (objetivo - ahorrado)
        );

        crearNotificacion(usuarioId, TipoNotificacion.META_PROGRESO, titulo, mensaje);
    }

    /**
     * 🎉 Notificación: Meta completada
     */
    @Transactional
    public void notificarMetaCompletada(
            Long usuarioId,
            String metaNombre,
            Double montoAhorrado
    ) {
        String titulo = String.format("🎉 ¡Meta Completada!: %s", metaNombre);
        String mensaje = String.format(
                "¡Felicidades! Has completado tu meta '%s'.\n" +
                "Total ahorrado: $%.2f\n" +
                "¡Excelente trabajo con tu disciplina financiera!",
                metaNombre, montoAhorrado
        );

        crearNotificacion(usuarioId, TipoNotificacion.META_COMPLETADA, titulo, mensaje);
    }

    /**
     * 🔔 Recordatorio: Pago recurrente próximo
     */
    @Transactional
    public void recordatorioMovimientoRecurrente(
            Long usuarioId,
            String descripcion,
            Double monto,
            String proximaFecha
    ) {
        String titulo = String.format("🔔 Recordatorio: %s", descripcion);
        String mensaje = String.format(
                "Tienes un pago recurrente próximo:\n" +
                "Concepto: %s\n" +
                "Monto: $%.2f\n" +
                "Fecha: %s\n" +
                "Asegúrate de tener fondos disponibles.",
                descripcion, monto, proximaFecha
        );

        crearNotificacion(usuarioId, TipoNotificacion.RECORDATORIO_PAGO, titulo, mensaje);
    }

    /**
     * 🚨 Alerta: Movimiento inusual detectado
     */
    @Transactional
    public void alertarMovimientoInusual(
            Long usuarioId,
            String descripcion,
            Double monto,
            String razon
    ) {
        String titulo = "🚨 Movimiento Inusual Detectado";
        String mensaje = String.format(
                "Se ha detectado un movimiento inusual:\n" +
                "Descripción: %s\n" +
                "Monto: $%.2f\n" +
                "Razón: %s\n" +
                "Si no reconoces esta transacción, revisa tu cuenta.",
                descripcion, monto, razon
        );

        crearNotificacion(usuarioId, TipoNotificacion.MOVIMIENTO_INUSUAL, titulo, mensaje);
    }

    /**
     * 📊 Resumen semanal
     */
    @Transactional
    public void enviarResumenSemanal(
            Long usuarioId,
            Double totalIngresos,
            Double totalGastos,
            Double balance
    ) {
        String titulo = "📊 Resumen Semanal";
        String mensaje = String.format(
                "Resumen de tu semana financiera:\n" +
                "Ingresos: $%.2f\n" +
                "Gastos: $%.2f\n" +
                "Balance: $%.2f\n" +
                "%s",
                totalIngresos, totalGastos, balance,
                balance > 0 ? "¡Excelente gestión! 💚" : "Considera reducir gastos. 💡"
        );

        crearNotificacion(usuarioId, TipoNotificacion.RESUMEN_SEMANAL, titulo, mensaje);
    }

    /**
     * 📊 Resumen mensual
     */
    @Transactional
    public void enviarResumenMensual(
            Long usuarioId,
            Double totalIngresos,
            Double totalGastos,
            Double totalAhorro,
            Double balance
    ) {
        String titulo = "📊 Resumen Mensual";
        String mensaje = String.format(
                "Resumen de tu mes financiero:\n" +
                "Ingresos: $%.2f\n" +
                "Gastos: $%.2f\n" +
                "Ahorro: $%.2f\n" +
                "Balance: $%.2f\n" +
                "Tasa de ahorro: %.1f%%",
                totalIngresos, totalGastos, totalAhorro, balance,
                totalIngresos > 0 ? (totalAhorro / totalIngresos * 100) : 0
        );

        crearNotificacion(usuarioId, TipoNotificacion.RESUMEN_MENSUAL, titulo, mensaje);
    }
}

