package com.controfinanciero.infrastructure.service;

import com.controfinanciero.domain.model.MovimientoFinanciero;
import com.controfinanciero.domain.model.enums.TipoMovimiento;
import com.controfinanciero.domain.repository.MetaFinancieraRepository;
import com.controfinanciero.domain.repository.MovimientoFinancieroRepository;
import com.controfinanciero.domain.repository.PresupuestoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 💬 Servicio de Coach Financiero IA
 * Genera consejos personalizados basados en el comportamiento del usuario
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FinancialCoachService {

    private final MovimientoFinancieroRepository movimientoRepository;
    private final PresupuestoRepository presupuestoRepository;
    private final MetaFinancieraRepository metaRepository;

    /**
     * Genera el consejo del día personalizado
     */
    public ConsejoDelDia generarConsejoDelDia(Long usuarioId) {
        // Analizar últimos 30 días
        LocalDate hace30Dias = LocalDate.now().minusDays(30);
        List<MovimientoFinanciero> movimientos = movimientoRepository
                .findByUsuarioIdAndFechaMovimientoBetweenOrderByFechaMovimientoDesc(
                        usuarioId, hace30Dias, LocalDate.now());

        // Analizar patrones
        String consejo = analizarPatronesYGenerarConsejo(usuarioId, movimientos);
        String categoria = determinarCategoria(movimientos);
        double impacto = calcularImpacto(movimientos);
        double ahorroPotencial = calcularAhorroPotencial(movimientos);

        return new ConsejoDelDia(
                consejo,
                categoria,
                impacto > 500 ? "ALTO" : impacto > 200 ? "MEDIO" : "BAJO",
                ahorroPotencial,
                generarAccionSugerida(categoria, ahorroPotencial)
        );
    }

    /**
     * Analiza hábitos financieros del usuario
     */
    public AnalisisHabitos analizarHabitos(Long usuarioId) {
        LocalDate hace90Dias = LocalDate.now().minusDays(90);
        List<MovimientoFinanciero> movimientos = movimientoRepository
                .findByUsuarioIdAndFechaMovimientoBetweenOrderByFechaMovimientoDesc(
                        usuarioId, hace90Dias, LocalDate.now());

        // Separar por tipo
        var gastos = movimientos.stream()
                .filter(m -> m.getTipoMovimiento() == TipoMovimiento.EXPENSE)
                .collect(Collectors.toList());

        var ingresos = movimientos.stream()
                .filter(m -> m.getTipoMovimiento().esIngreso())
                .collect(Collectors.toList());

        // Calcular totales
        BigDecimal totalGastos = gastos.stream()
                .map(MovimientoFinanciero::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalIngresos = ingresos.stream()
                .map(MovimientoFinanciero::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Análisis por categoría
        Map<Long, BigDecimal> gastosPorCategoria = gastos.stream()
                .filter(g -> g.getCategoriaId() != null)
                .collect(Collectors.groupingBy(
                        MovimientoFinanciero::getCategoriaId,
                        Collectors.reducing(BigDecimal.ZERO,
                                MovimientoFinanciero::getMonto,
                                BigDecimal::add)
                ));

        // Identificar categoría problemática
        var categoriaProblematica = gastosPorCategoria.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);

        // Generar insights
        List<String> insights = new ArrayList<>();

        double tasaAhorro = totalIngresos.doubleValue() > 0 ?
                ((totalIngresos.subtract(totalGastos)).doubleValue() / totalIngresos.doubleValue() * 100) : 0;

        if (tasaAhorro < 10) {
            insights.add("⚠️ Tu tasa de ahorro es baja (" + String.format("%.1f%%", tasaAhorro) + "). Intenta ahorrar al menos 20% de tus ingresos.");
        } else if (tasaAhorro >= 20) {
            insights.add("✅ ¡Excelente! Estás ahorrando " + String.format("%.1f%%", tasaAhorro) + " de tus ingresos.");
        }

        if (categoriaProblematica != null) {
            double porcentaje = (categoriaProblematica.getValue().doubleValue() / totalGastos.doubleValue()) * 100;
            if (porcentaje > 30) {
                insights.add("📊 El " + String.format("%.0f%%", porcentaje) + " de tus gastos van a una sola categoría. Considera diversificar.");
            }
        }

        // Frecuencia de registro
        int diasConMovimientos = (int) movimientos.stream()
                .map(MovimientoFinanciero::getFechaMovimiento)
                .distinct()
                .count();

        if (diasConMovimientos < 30) {
            insights.add("📝 Has registrado movimientos solo " + diasConMovimientos + " días en los últimos 90. La constancia mejora el control.");
        }

        return new AnalisisHabitos(
                totalIngresos.doubleValue(),
                totalGastos.doubleValue(),
                tasaAhorro,
                gastosPorCategoria.size(),
                diasConMovimientos,
                insights,
                generarRecomendaciones(tasaAhorro, gastosPorCategoria)
        );
    }

    // Métodos auxiliares

    private String analizarPatronesYGenerarConsejo(Long usuarioId, List<MovimientoFinanciero> movimientos) {
        if (movimientos.isEmpty()) {
            return "👋 ¡Comienza registrando tus movimientos diarios para obtener consejos personalizados!";
        }

        // Análisis de gastos recientes
        var gastos = movimientos.stream()
                .filter(m -> m.getTipoMovimiento() == TipoMovimiento.EXPENSE)
                .collect(Collectors.toList());

        if (gastos.isEmpty()) {
            return "💡 No olvides registrar tus gastos diarios para mantener el control de tus finanzas.";
        }

        // Detectar categoría con más gastos
        Map<Long, Long> frecuenciaPorCategoria = gastos.stream()
                .filter(g -> g.getCategoriaId() != null)
                .collect(Collectors.groupingBy(
                        MovimientoFinanciero::getCategoriaId,
                        Collectors.counting()
                ));

        if (!frecuenciaPorCategoria.isEmpty()) {
            var categoriaTop = frecuenciaPorCategoria.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .get();

            return String.format("💰 Has realizado %d gastos en una categoría este mes. Considera establecer un presupuesto para controlar mejor estos gastos.",
                    categoriaTop.getValue());
        }

        return "✅ Mantén el buen trabajo registrando tus movimientos. La disciplina es clave para el éxito financiero.";
    }

    private String determinarCategoria(List<MovimientoFinanciero> movimientos) {
        var gastos = movimientos.stream()
                .filter(m -> m.getTipoMovimiento() == TipoMovimiento.EXPENSE && m.getCategoriaId() != null)
                .collect(Collectors.toList());

        if (gastos.isEmpty()) return "General";

        var categoriaFrecuente = gastos.stream()
                .collect(Collectors.groupingBy(
                        MovimientoFinanciero::getCategoriaId,
                        Collectors.counting()
                ))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(e -> "Categoría #" + e.getKey())
                .orElse("General");

        return categoriaFrecuente;
    }

    private double calcularImpacto(List<MovimientoFinanciero> movimientos) {
        return movimientos.stream()
                .filter(m -> m.getTipoMovimiento() == TipoMovimiento.EXPENSE)
                .map(MovimientoFinanciero::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .doubleValue();
    }

    private double calcularAhorroPotencial(List<MovimientoFinanciero> movimientos) {
        // Identificar gastos no esenciales (simplificado)
        long gastosNoEsenciales = movimientos.stream()
                .filter(m -> m.getTipoMovimiento() == TipoMovimiento.EXPENSE)
                .filter(m -> m.getDescripcion() != null &&
                        (m.getDescripcion().toLowerCase().contains("entret") ||
                         m.getDescripcion().toLowerCase().contains("restaur") ||
                         m.getDescripcion().toLowerCase().contains("comida")))
                .count();

        return gastosNoEsenciales * 100.0; // Estimación simple
    }

    private String generarAccionSugerida(String categoria, double ahorroPotencial) {
        if (ahorroPotencial > 500) {
            return "Reduce gastos en " + categoria + " y podrías ahorrar $" + String.format("%.0f", ahorroPotencial) + " este mes";
        } else if (ahorroPotencial > 200) {
            return "Considera limitar gastos en " + categoria + " para aumentar tu ahorro";
        }
        return "Mantén tus hábitos actuales, vas por buen camino";
    }

    private List<String> generarRecomendaciones(double tasaAhorro, Map<Long, BigDecimal> gastosPorCategoria) {
        List<String> recomendaciones = new ArrayList<>();

        if (tasaAhorro < 20) {
            recomendaciones.add("🎯 Establece como meta ahorrar el 20% de tus ingresos");
            recomendaciones.add("💳 Revisa suscripciones que no uses y cancélalas");
        }

        if (gastosPorCategoria.size() > 10) {
            recomendaciones.add("📊 Simplifica tus categorías para mejor control");
        }

        recomendaciones.add("📅 Programa revisiones semanales de tus gastos");
        recomendaciones.add("🎁 Considera el método de los 30 días para compras grandes");

        return recomendaciones;
    }

    // DTOs
    public record ConsejoDelDia(
            String consejo,
            String categoria,
            String nivelImpacto,
            double ahorroPotencial,
            String accionSugerida
    ) {}

    public record AnalisisHabitos(
            double ingresosPromedio,
            double gastosPromedio,
            double tasaAhorro,
            int categoriasActivas,
            int diasRegistrados,
            List<String> insights,
            List<String> recomendaciones
    ) {}
}

