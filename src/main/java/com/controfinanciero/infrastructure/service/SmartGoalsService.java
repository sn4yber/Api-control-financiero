package com.controfinanciero.infrastructure.service;

import com.controfinanciero.domain.model.MetaFinanciera;
import com.controfinanciero.domain.model.MovimientoFinanciero;
import com.controfinanciero.domain.model.enums.TipoMovimiento;
import com.controfinanciero.domain.repository.MetaFinancieraRepository;
import com.controfinanciero.domain.repository.MovimientoFinancieroRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * 🎯 Servicio de Metas Inteligentes
 * Proyecciones, recomendaciones y análisis predictivo de metas financieras
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SmartGoalsService {

    private final MetaFinancieraRepository metaRepository;
    private final MovimientoFinancieroRepository movimientoRepository;

    /**
     * Analiza una meta y genera proyecciones inteligentes
     */
    public MetaInteligente analizarMeta(Long metaId) {
        MetaFinanciera meta = metaRepository.findById(metaId)
                .orElseThrow(() -> new RuntimeException("Meta no encontrada"));

        if (!meta.estaActiva()) {
            throw new RuntimeException("La meta no está activa");
        }

        // Datos básicos
        BigDecimal montoActual = meta.getMontoActual();
        BigDecimal montoObjetivo = meta.getMontoObjetivo();
        BigDecimal montoRestante = montoObjetivo.subtract(montoActual);
        BigDecimal porcentajeAvance = meta.calcularPorcentajeProgreso();

        // Calcular días transcurridos y restantes
        long diasTranscurridos = ChronoUnit.DAYS.between(
                meta.getCreatedAt().toLocalDate(),
                LocalDate.now()
        );

        long diasRestantes = meta.getFechaObjetivo() != null ?
                ChronoUnit.DAYS.between(LocalDate.now(), meta.getFechaObjetivo()) : 0;

        // Calcular velocidad de ahorro (dinero por día)
        BigDecimal velocidadAhorro = diasTranscurridos > 0 ?
                montoActual.divide(BigDecimal.valueOf(diasTranscurridos), 2, RoundingMode.HALF_UP) :
                BigDecimal.ZERO;

        // Proyección de fecha de completación
        LocalDate fechaProyectada = null;
        if (velocidadAhorro.compareTo(BigDecimal.ZERO) > 0) {
            long diasNecesarios = montoRestante
                    .divide(velocidadAhorro, 0, RoundingMode.UP)
                    .longValue();
            fechaProyectada = LocalDate.now().plusDays(diasNecesarios);
        }

        // Calcular ahorro diario requerido para cumplir en tiempo
        BigDecimal ahorroRequeridoDiario = BigDecimal.ZERO;
        if (diasRestantes > 0) {
            ahorroRequeridoDiario = montoRestante
                    .divide(BigDecimal.valueOf(diasRestantes), 2, RoundingMode.HALF_UP);
        }

        // Determinar estado de la meta
        EstadoProyeccion estadoProyeccion = determinarEstado(
                velocidadAhorro,
                ahorroRequeridoDiario,
                fechaProyectada,
                meta.getFechaObjetivo()
        );

        // Generar recomendaciones
        List<String> recomendaciones = generarRecomendaciones(
                meta,
                velocidadAhorro,
                ahorroRequeridoDiario,
                estadoProyeccion
        );

        // Calcular probabilidad de éxito
        double probabilidadExito = calcularProbabilidadExito(
                velocidadAhorro,
                ahorroRequeridoDiario,
                diasRestantes
        );

        log.info("🎯 Meta analizada: {} - Estado: {} - Probabilidad: {}%",
                meta.getNombre(), estadoProyeccion, String.format("%.0f", probabilidadExito));

        return new MetaInteligente(
                meta.getId(),
                meta.getNombre(),
                montoActual,
                montoObjetivo,
                montoRestante,
                porcentajeAvance,
                diasTranscurridos,
                diasRestantes,
                velocidadAhorro,
                ahorroRequeridoDiario,
                fechaProyectada,
                estadoProyeccion,
                probabilidadExito,
                recomendaciones
        );
    }

    /**
     * Analiza todas las metas activas del usuario
     */
    public List<MetaInteligente> analizarTodasLasMetas(Long usuarioId) {
        List<MetaFinanciera> metas = metaRepository.findByUsuarioIdAndEstado(
                usuarioId,
                com.controfinanciero.domain.model.enums.EstadoMeta.ACTIVE
        );

        List<MetaInteligente> analisis = new ArrayList<>();
        for (MetaFinanciera meta : metas) {
            try {
                analisis.add(analizarMeta(meta.getId()));
            } catch (Exception e) {
                log.error("Error analizando meta {}: {}", meta.getId(), e.getMessage());
            }
        }

        return analisis;
    }

    /**
     * Recomienda monto óptimo para ahorrar mensualmente
     */
    public RecomendacionAhorro recomendarAhorroMensual(Long usuarioId) {
        // Obtener ingresos promedio de últimos 3 meses
        LocalDate hace3Meses = LocalDate.now().minusMonths(3);
        List<MovimientoFinanciero> ingresos = movimientoRepository
                .findByUsuarioIdAndFechaMovimientoBetweenOrderByFechaMovimientoDesc(
                        usuarioId, hace3Meses, LocalDate.now()
                ).stream()
                .filter(m -> m.getTipoMovimiento().esIngreso())
                .toList();

        BigDecimal totalIngresos = ingresos.stream()
                .map(MovimientoFinanciero::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal ingresoMensualPromedio = totalIngresos.divide(
                BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP
        );

        // Regla 50/30/20: 20% para ahorro
        BigDecimal ahorroRecomendado = ingresoMensualPromedio
                .multiply(BigDecimal.valueOf(0.20))
                .setScale(2, RoundingMode.HALF_UP);

        // Ahorro mínimo (10%)
        BigDecimal ahorroMinimo = ingresoMensualPromedio
                .multiply(BigDecimal.valueOf(0.10))
                .setScale(2, RoundingMode.HALF_UP);

        // Ahorro óptimo (30%)
        BigDecimal ahorroOptimo = ingresoMensualPromedio
                .multiply(BigDecimal.valueOf(0.30))
                .setScale(2, RoundingMode.HALF_UP);

        return new RecomendacionAhorro(
                ingresoMensualPromedio,
                ahorroMinimo,
                ahorroRecomendado,
                ahorroOptimo,
                "Basado en la regla 50/30/20: 50% necesidades, 30% deseos, 20% ahorro"
        );
    }

    // Métodos auxiliares

    private EstadoProyeccion determinarEstado(
            BigDecimal velocidadActual,
            BigDecimal velocidadRequerida,
            LocalDate fechaProyectada,
            LocalDate fechaObjetivo) {

        if (velocidadActual.compareTo(BigDecimal.ZERO) == 0) {
            return EstadoProyeccion.SIN_PROGRESO;
        }

        if (fechaObjetivo == null) {
            return EstadoProyeccion.EN_CAMINO;
        }

        if (fechaProyectada != null && fechaProyectada.isBefore(fechaObjetivo)) {
            return EstadoProyeccion.ADELANTADO;
        }

        if (velocidadActual.compareTo(velocidadRequerida) >= 0) {
            return EstadoProyeccion.EN_CAMINO;
        }

        if (velocidadActual.compareTo(velocidadRequerida.multiply(BigDecimal.valueOf(0.7))) < 0) {
            return EstadoProyeccion.EN_RIESGO;
        }

        return EstadoProyeccion.LENTO;
    }

    private List<String> generarRecomendaciones(
            MetaFinanciera meta,
            BigDecimal velocidadActual,
            BigDecimal velocidadRequerida,
            EstadoProyeccion estado) {

        List<String> recomendaciones = new ArrayList<>();

        switch (estado) {
            case SIN_PROGRESO:
                recomendaciones.add("⚠️ No has realizado aportes a esta meta aún");
                recomendaciones.add("💡 Comienza con pequeños aportes regulares");
                break;

            case EN_RIESGO:
                BigDecimal diferencia = velocidadRequerida.subtract(velocidadActual);
                recomendaciones.add(String.format(
                        "🚨 Necesitas aumentar tu ahorro en $%.2f por día",
                        diferencia
                ));
                recomendaciones.add("💡 Considera reducir gastos no esenciales");
                recomendaciones.add("💡 Busca fuentes de ingreso adicionales");
                break;

            case LENTO:
                recomendaciones.add("⚠️ Estás avanzando más lento de lo necesario");
                recomendaciones.add(String.format(
                        "💡 Intenta ahorrar $%.2f por día en lugar de $%.2f",
                        velocidadRequerida, velocidadActual
                ));
                break;

            case EN_CAMINO:
                recomendaciones.add("✅ Vas por buen camino, mantén el ritmo");
                recomendaciones.add("💡 Automatiza tus ahorros para no olvidar");
                break;

            case ADELANTADO:
                recomendaciones.add("🎉 ¡Excelente! Vas adelantado");
                recomendaciones.add("💡 Podrías alcanzar tu meta antes de lo esperado");
                break;
        }

        return recomendaciones;
    }

    private double calcularProbabilidadExito(
            BigDecimal velocidadActual,
            BigDecimal velocidadRequerida,
            long diasRestantes) {

        if (diasRestantes <= 0 || velocidadRequerida.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }

        double ratio = velocidadActual
                .divide(velocidadRequerida, 4, RoundingMode.HALF_UP)
                .doubleValue();

        // Ajustar probabilidad según tiempo restante
        double factorTiempo = Math.min(1.0, diasRestantes / 30.0);
        double probabilidad = Math.min(100.0, ratio * 100 * factorTiempo);

        return Math.max(0.0, probabilidad);
    }

    // DTOs

    public record MetaInteligente(
            Long metaId,
            String nombre,
            BigDecimal montoActual,
            BigDecimal montoObjetivo,
            BigDecimal montoRestante,
            BigDecimal porcentajeAvance,
            long diasTranscurridos,
            long diasRestantes,
            BigDecimal velocidadAhorroDiaria,
            BigDecimal ahorroRequeridoDiario,
            LocalDate fechaProyectadaCompletacion,
            EstadoProyeccion estado,
            double probabilidadExito,
            List<String> recomendaciones
    ) {}

    public record RecomendacionAhorro(
            BigDecimal ingresoMensualPromedio,
            BigDecimal ahorroMinimo,
            BigDecimal ahorroRecomendado,
            BigDecimal ahorroOptimo,
            String explicacion
    ) {}

    public enum EstadoProyeccion {
        SIN_PROGRESO,
        EN_RIESGO,
        LENTO,
        EN_CAMINO,
        ADELANTADO
    }
}

