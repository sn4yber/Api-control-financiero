package com.controfinanciero.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad de dominio: Notificación
 * Representa alertas y notificaciones para el usuario
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notificacion {
    private Long id;
    private Long usuarioId;
    private String tipo; // PRESUPUESTO_ALERTA, PRESUPUESTO_EXCEDIDO, MOVIMIENTO_AUTOMATICO, META_ALCANZADA, RESUMEN_MENSUAL
    private String titulo;
    private String mensaje;
    private Boolean leida;
    private LocalDateTime fechaEnvio;
    private LocalDateTime createdAt;

    /**
     * Constructor para crear notificación de presupuesto
     */
    public static Notificacion crearAlertaPresupuesto(Long usuarioId, String categoriaNombre, String porcentaje) {
        Notificacion notificacion = new Notificacion();
        notificacion.setUsuarioId(usuarioId);
        notificacion.setTipo("PRESUPUESTO_ALERTA");
        notificacion.setTitulo("⚠️ Presupuesto cerca del límite");
        notificacion.setMensaje(String.format("Has usado el %s%% del presupuesto de %s", porcentaje, categoriaNombre));
        notificacion.setLeida(false);
        notificacion.setFechaEnvio(LocalDateTime.now());
        notificacion.setCreatedAt(LocalDateTime.now());
        return notificacion;
    }

    /**
     * Constructor para crear notificación de presupuesto excedido
     */
    public static Notificacion crearPresupuestoExcedido(Long usuarioId, String categoriaNombre) {
        Notificacion notificacion = new Notificacion();
        notificacion.setUsuarioId(usuarioId);
        notificacion.setTipo("PRESUPUESTO_EXCEDIDO");
        notificacion.setTitulo("🚨 Presupuesto excedido");
        notificacion.setMensaje(String.format("Has excedido el presupuesto de %s", categoriaNombre));
        notificacion.setLeida(false);
        notificacion.setFechaEnvio(LocalDateTime.now());
        notificacion.setCreatedAt(LocalDateTime.now());
        return notificacion;
    }

    /**
     * Constructor para crear notificación de movimiento automático
     */
    public static Notificacion crearMovimientoAutomatico(Long usuarioId, String descripcion, String monto) {
        Notificacion notificacion = new Notificacion();
        notificacion.setUsuarioId(usuarioId);
        notificacion.setTipo("MOVIMIENTO_AUTOMATICO");
        notificacion.setTitulo("🔄 Movimiento automático ejecutado");
        notificacion.setMensaje(String.format("%s por %s", descripcion, monto));
        notificacion.setLeida(false);
        notificacion.setFechaEnvio(LocalDateTime.now());
        notificacion.setCreatedAt(LocalDateTime.now());
        return notificacion;
    }

    /**
     * Constructor para crear notificación de meta alcanzada
     */
    public static Notificacion crearMetaAlcanzada(Long usuarioId, String nombreMeta) {
        Notificacion notificacion = new Notificacion();
        notificacion.setUsuarioId(usuarioId);
        notificacion.setTipo("META_ALCANZADA");
        notificacion.setTitulo("🎉 ¡Meta alcanzada!");
        notificacion.setMensaje(String.format("Has completado tu meta: %s", nombreMeta));
        notificacion.setLeida(false);
        notificacion.setFechaEnvio(LocalDateTime.now());
        notificacion.setCreatedAt(LocalDateTime.now());
        return notificacion;
    }

    /**
     * Marca la notificación como leída
     */
    public void marcarComoLeida() {
        this.leida = true;
    }
}

