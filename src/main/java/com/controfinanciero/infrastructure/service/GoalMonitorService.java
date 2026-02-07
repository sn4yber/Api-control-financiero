package com.controfinanciero.infrastructure.service;

import com.controfinanciero.domain.model.MetaFinanciera;
import com.controfinanciero.domain.repository.MetaFinancieraRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 🎯 Servicio de Monitoreo de Metas
 * Verifica el progreso de las metas y genera notificaciones
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GoalMonitorService {

    private final MetaFinancieraRepository metaRepository;
    private final NotificationService notificationService;

    /**
     * Verifica el progreso de una meta después de agregar ahorro
     */
    public void verificarProgresoMeta(Long metaId) {
        MetaFinanciera meta = metaRepository.findById(metaId)
                .orElseThrow(() -> new RuntimeException("Meta no encontrada"));

        if (!meta.estaActiva()) {
            return; // Meta no activa
        }

        BigDecimal progreso = meta.getMontoActual();
        BigDecimal objetivo = meta.getMontoObjetivo();
        BigDecimal porcentaje = meta.calcularPorcentajeProgreso();

        double porcentajeDouble = porcentaje.doubleValue();

        log.info("🎯 Meta - Usuario: {}, Meta: {}, Progreso: {}/{} ({}%)",
                meta.getUsuarioId(), meta.getNombre(), progreso, objetivo, String.format("%.1f", porcentajeDouble));

        // Notificar si se completó la meta
        if (porcentajeDouble >= 100 && !meta.estaCompletada()) {
            notificationService.notificarMetaCompletada(
                    meta.getUsuarioId(),
                    meta.getNombre(),
                    progreso.doubleValue()
            );

            // Marcar meta como completada
            meta.completar();
            metaRepository.save(meta);
        }
        // Notificar en hitos importantes (25%, 50%, 75%)
        else if (estaEnHitoImportante(porcentajeDouble)) {
            notificationService.notificarProgresoMeta(
                    meta.getUsuarioId(),
                    meta.getNombre(),
                    progreso.doubleValue(),
                    objetivo.doubleValue(),
                    porcentajeDouble
            );
        }
    }

    /**
     * Determina si el porcentaje está en un hito importante
     */
    private boolean estaEnHitoImportante(double porcentaje) {
        // Notificar en 25%, 50%, 75%
        int porcentajeInt = (int) porcentaje;
        return porcentajeInt == 25 || porcentajeInt == 50 || porcentajeInt == 75;
    }
}

