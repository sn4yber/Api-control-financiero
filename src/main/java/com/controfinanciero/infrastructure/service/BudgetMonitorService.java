package com.controfinanciero.infrastructure.service;

import com.controfinanciero.domain.repository.PresupuestoRepository;
import com.controfinanciero.infrastructure.persistence.entity.PresupuestoEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * 🚨 Servicio de Monitoreo de Presupuestos
 * Verifica automáticamente si los usuarios exceden sus presupuestos
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BudgetMonitorService {

    private final PresupuestoRepository presupuestoRepository;
    private final NotificationService notificationService;

    /**
     * Verifica el presupuesto de una categoría después de agregar un gasto
     */
    public void verificarPresupuestoDespuesDeGasto(Long usuarioId, Long categoriaId, BigDecimal montoGasto) {
        // Obtener periodo actual (formato: "2026-02")
        String periodoActual = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));

        // Buscar presupuesto activo para esta categoría en el periodo actual
        Optional<PresupuestoEntity> presupuestoOpt = presupuestoRepository
                .findByUsuarioIdAndCategoriaIdAndPeriodo(usuarioId, categoriaId, periodoActual);

        if (presupuestoOpt.isEmpty() || !presupuestoOpt.get().getActivo()) {
            return; // No hay presupuesto configurado
        }

        PresupuestoEntity presupuesto = presupuestoOpt.get();

        // Obtener datos del presupuesto
        Double gastado = presupuesto.getGastoActual().doubleValue();
        Double limite = presupuesto.getLimiteMensual().doubleValue();
        double porcentaje = (gastado / limite) * 100;

        log.info("💰 Presupuesto - Usuario: {}, Categoría: {}, Gastado: {}/{} ({}%)",
                usuarioId, categoriaId, gastado, limite, String.format("%.1f", porcentaje));

        // Alerta si se excede el 100%
        if (porcentaje >= 100) {
            String categoriaNombre = presupuesto.getCategoriaNombre() != null
                    ? presupuesto.getCategoriaNombre()
                    : "Categoría #" + categoriaId;

            notificationService.alertarPresupuestoExcedido(
                    usuarioId, categoriaNombre, gastado, limite, porcentaje
            );
        }
        // Alerta si está entre 80% y 99%
        else if (porcentaje >= 80) {
            String categoriaNombre = presupuesto.getCategoriaNombre() != null
                    ? presupuesto.getCategoriaNombre()
                    : "Categoría #" + categoriaId;

            notificationService.alertarPresupuestoCercaLimite(
                    usuarioId, categoriaNombre, gastado, limite, porcentaje
            );
        }
    }
}

