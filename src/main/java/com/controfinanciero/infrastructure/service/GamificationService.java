package com.controfinanciero.infrastructure.service;

import com.controfinanciero.domain.model.enums.TipoLogro;
import com.controfinanciero.infrastructure.persistence.entity.LogroUsuarioEntity;
import com.controfinanciero.infrastructure.persistence.entity.RachaAhorroEntity;
import com.controfinanciero.infrastructure.persistence.repository.LogroUsuarioRepository;
import com.controfinanciero.infrastructure.persistence.repository.RachaAhorroRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * 🎮 Servicio de Gamificación
 * Gestión de logros, badges y rachas de ahorro
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GamificationService {

    private final LogroUsuarioRepository logroRepository;
    private final RachaAhorroRepository rachaRepository;
    private final NotificationService notificationService;

    /**
     * Registra actividad y actualiza racha
     */
    @Transactional
    public void registrarActividad(Long usuarioId) {
        RachaAhorroEntity racha = rachaRepository.findByUsuarioId(usuarioId)
                .orElseGet(() -> {
                    RachaAhorroEntity nueva = new RachaAhorroEntity();
                    nueva.setUsuarioId(usuarioId);
                    nueva.setRachaActual(0);
                    nueva.setRachaMaxima(0);
                    nueva.setDiasConsecutivos(0);
                    return nueva;
                });

        // Verificar si debe reiniciarse la racha
        if (racha.debeReiniciarse()) {
            racha.reiniciarRacha();
        }

        // Incrementar racha
        LocalDate hoy = LocalDate.now();
        if (racha.getUltimaActividad() == null || !racha.getUltimaActividad().equals(hoy)) {
            racha.incrementarRacha();
            rachaRepository.save(racha);

            // Verificar logros de racha
            verificarLogrosRacha(usuarioId, racha.getRachaActual());
        }
    }

    /**
     * Desbloquea un logro para el usuario
     */
    @Transactional
    public LogroUsuarioEntity desbloquearLogro(Long usuarioId, TipoLogro tipoLogro) {
        // Verificar si ya tiene el logro
        var logroExistente = logroRepository.findByUsuarioIdAndTipoLogro(usuarioId, tipoLogro);
        if (logroExistente.isPresent()) {
            return logroExistente.get();
        }

        // Crear nuevo logro
        LogroUsuarioEntity logro = new LogroUsuarioEntity();
        logro.setUsuarioId(usuarioId);
        logro.setTipoLogro(tipoLogro);
        logro.setReclamado(false);
        logro.setProgresoActual(1);
        logro.setProgresoRequerido(1);

        LogroUsuarioEntity guardado = logroRepository.save(logro);

        // Notificar al usuario
        notificationService.crearNotificacion(
                usuarioId,
                "LOGRO_DESBLOQUEADO",
                tipoLogro.getEmoji() + " ¡Logro Desbloqueado!",
                String.format("Has desbloqueado: %s - %s",
                        tipoLogro.getNombre(),
                        tipoLogro.getDescripcion())
        );

        log.info("🎮 Logro desbloqueado: {} para usuario {}", tipoLogro.getNombre(), usuarioId);
        return guardado;
    }

    /**
     * Verifica y desbloquea logros relacionados con rachas
     */
    private void verificarLogrosRacha(Long usuarioId, int rachaActual) {
        if (rachaActual == 7) {
            desbloquearLogro(usuarioId, TipoLogro.RACHA_7_DIAS);
        }
        if (rachaActual == 30) {
            desbloquearLogro(usuarioId, TipoLogro.RACHA_30_DIAS);
        }
        if (rachaActual == 90) {
            desbloquearLogro(usuarioId, TipoLogro.DISCIPLINA_FINANCIERA);
        }
    }

    /**
     * Obtiene todos los logros del usuario
     */
    public List<LogroUsuarioEntity> obtenerLogrosUsuario(Long usuarioId) {
        return logroRepository.findByUsuarioId(usuarioId);
    }

    /**
     * Obtiene logros no reclamados
     */
    public List<LogroUsuarioEntity> obtenerLogrosNoReclamados(Long usuarioId) {
        return logroRepository.findByUsuarioIdAndReclamadoFalse(usuarioId);
    }

    /**
     * Marca un logro como reclamado
     */
    @Transactional
    public void reclamarLogro(Long logroId) {
        logroRepository.findById(logroId).ifPresent(logro -> {
            logro.setReclamado(true);
            logroRepository.save(logro);
        });
    }

    /**
     * Obtiene la racha actual del usuario
     */
    public RachaAhorroEntity obtenerRacha(Long usuarioId) {
        return rachaRepository.findByUsuarioId(usuarioId)
                .orElseGet(() -> {
                    RachaAhorroEntity nueva = new RachaAhorroEntity();
                    nueva.setUsuarioId(usuarioId);
                    nueva.setRachaActual(0);
                    nueva.setRachaMaxima(0);
                    nueva.setDiasConsecutivos(0);
                    return rachaRepository.save(nueva);
                });
    }

    /**
     * Verifica logro de primera meta
     */
    @Transactional
    public void verificarPrimeraMeta(Long usuarioId) {
        desbloquearLogro(usuarioId, TipoLogro.PRIMERA_META);
    }

    /**
     * Verifica logro de ahorrador ninja (3 metas en un mes)
     */
    @Transactional
    public void verificarAhorradorNinja(Long usuarioId, int metasCompletadas) {
        if (metasCompletadas >= 3) {
            desbloquearLogro(usuarioId, TipoLogro.AHORRADOR_NINJA);
        }
    }

    /**
     * Verifica logro de meta millonaria
     */
    @Transactional
    public void verificarMetaMillonaria(Long usuarioId, double montoMeta) {
        if (montoMeta >= 1000000) {
            desbloquearLogro(usuarioId, TipoLogro.META_MILLONARIA);
        }
    }

    /**
     * DTOs
     */
    public record EstadisticasGamificacion(
            int logrosDesbloqueados,
            int logrosTotales,
            int logrosNoReclamados,
            int rachaActual,
            int rachaMaxima,
            int diasConsecutivos,
            double porcentajeCompletado
    ) {}

    public EstadisticasGamificacion obtenerEstadisticas(Long usuarioId) {
        var logros = logroRepository.findByUsuarioId(usuarioId);
        var noReclamados = logroRepository.findByUsuarioIdAndReclamadoFalse(usuarioId);
        var racha = obtenerRacha(usuarioId);

        int logrosTotales = TipoLogro.values().length;
        double porcentaje = (logros.size() * 100.0) / logrosTotales;

        return new EstadisticasGamificacion(
                logros.size(),
                logrosTotales,
                noReclamados.size(),
                racha.getRachaActual(),
                racha.getRachaMaxima(),
                racha.getDiasConsecutivos(),
                porcentaje
        );
    }
}

