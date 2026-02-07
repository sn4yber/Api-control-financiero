package com.controfinanciero.infrastructure.service;

import com.controfinanciero.domain.model.MovimientoFinanciero;
import com.controfinanciero.domain.model.enums.TipoMovimiento;
import com.controfinanciero.domain.repository.MovimientoFinancieroRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 📊 Servicio de Análisis de Tendencias Financieras
 * Analiza patrones de gasto, predicciones y comparaciones entre períodos
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TrendAnalysisService {

    private final MovimientoFinancieroRepository movimientoRepository;

    /**
     * Analiza tendencias de gasto por categoría en los últimos meses
     */
    public TrendAnalysisResult analizarTendencias(Long usuarioId, int mesesAtras) {
        LocalDate hoy = LocalDate.now();
        LocalDate fechaInicio = hoy.minusMonths(mesesAtras);

        List<MovimientoFinanciero> movimientos = movimientoRepository
                .findByUsuarioIdAndFechaMovimientoBetweenOrderByFechaMovimientoDesc(
                        usuarioId, fechaInicio, hoy);

        // Separar por tipo
        List<MovimientoFinanciero> ingresos = movimientos.stream()
                .filter(m -> m.getTipoMovimiento().esIngreso())
                .collect(Collectors.toList());

        List<MovimientoFinanciero> gastos = movimientos.stream()
                .filter(m -> m.getTipoMovimiento() == TipoMovimiento.EXPENSE)
                .collect(Collectors.toList());

        // Calcular promedios mensuales
        BigDecimal promedioIngresosMensual = calcularPromedioMensual(ingresos, mesesAtras);
        BigDecimal promedioGastosMensual = calcularPromedioMensual(gastos, mesesAtras);

        // Calcular tendencia (crecimiento/decrecimiento)
        TendenciaInfo tendenciaIngresos = calcularTendencia(ingresos, mesesAtras);
        TendenciaInfo tendenciaGastos = calcularTendencia(gastos, mesesAtras);

        // Análisis por categoría
        Map<String, BigDecimal> gastosPorCategoria = analizarPorCategoria(gastos);

        // Predicción para próximo mes
        BigDecimal prediccionIngresos = predecirProximoMes(ingresos);
        BigDecimal prediccionGastos = predecirProximoMes(gastos);

        // Categoría con mayor gasto
        Map.Entry<String, BigDecimal> categoriaTopGasto = gastosPorCategoria.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);

        log.info("📊 Análisis de tendencias completado para usuario {}: {} meses analizados",
                usuarioId, mesesAtras);

        return new TrendAnalysisResult(
                promedioIngresosMensual,
                promedioGastosMensual,
                tendenciaIngresos,
                tendenciaGastos,
                gastosPorCategoria,
                prediccionIngresos,
                prediccionGastos,
                categoriaTopGasto != null ? categoriaTopGasto.getKey() : "N/A",
                categoriaTopGasto != null ? categoriaTopGasto.getValue() : BigDecimal.ZERO,
                mesesAtras
        );
    }

    /**
     * Compara dos períodos de tiempo
     */
    public ComparacionPeriodos compararPeriodos(Long usuarioId,
                                                 LocalDate inicio1, LocalDate fin1,
                                                 LocalDate inicio2, LocalDate fin2) {

        List<MovimientoFinanciero> periodo1 = movimientoRepository
                .findByUsuarioIdAndFechaMovimientoBetweenOrderByFechaMovimientoDesc(
                        usuarioId, inicio1, fin1);

        List<MovimientoFinanciero> periodo2 = movimientoRepository
                .findByUsuarioIdAndFechaMovimientoBetweenOrderByFechaMovimientoDesc(
                        usuarioId, inicio2, fin2);

        BigDecimal ingresos1 = calcularTotalPorTipo(periodo1, true);
        BigDecimal gastos1 = calcularTotalPorTipo(periodo1, false);
        BigDecimal balance1 = ingresos1.subtract(gastos1);

        BigDecimal ingresos2 = calcularTotalPorTipo(periodo2, true);
        BigDecimal gastos2 = calcularTotalPorTipo(periodo2, false);
        BigDecimal balance2 = ingresos2.subtract(gastos2);

        // Calcular diferencias porcentuales
        double variacionIngresos = calcularVariacionPorcentual(ingresos1, ingresos2);
        double variacionGastos = calcularVariacionPorcentual(gastos1, gastos2);
        double variacionBalance = calcularVariacionPorcentual(balance1, balance2);

        return new ComparacionPeriodos(
                ingresos1, gastos1, balance1,
                ingresos2, gastos2, balance2,
                variacionIngresos, variacionGastos, variacionBalance
        );
    }

    /**
     * Detecta patrones inusuales de gasto
     */
    public List<PatronInusual> detectarPatronesInusuales(Long usuarioId, int diasAtras) {
        LocalDate fechaInicio = LocalDate.now().minusDays(diasAtras);
        List<MovimientoFinanciero> movimientos = movimientoRepository
                .findByUsuarioIdAndFechaMovimientoBetweenOrderByFechaMovimientoDesc(
                        usuarioId, fechaInicio, LocalDate.now());

        List<MovimientoFinanciero> gastos = movimientos.stream()
                .filter(m -> m.getTipoMovimiento() == TipoMovimiento.EXPENSE)
                .collect(Collectors.toList());

        if (gastos.isEmpty()) {
            return Collections.emptyList();
        }

        // Calcular promedio y desviación estándar
        BigDecimal promedio = gastos.stream()
                .map(MovimientoFinanciero::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(gastos.size()), 2, RoundingMode.HALF_UP);

        BigDecimal desviacion = calcularDesviacionEstandar(gastos, promedio);
        BigDecimal umbralSuperior = promedio.add(desviacion.multiply(BigDecimal.valueOf(2)));

        // Detectar gastos inusuales (más de 2 desviaciones estándar)
        List<PatronInusual> patrones = new ArrayList<>();
        for (MovimientoFinanciero gasto : gastos) {
            if (gasto.getMonto().compareTo(umbralSuperior) > 0) {
                patrones.add(new PatronInusual(
                        gasto.getId(),
                        gasto.getDescripcion(),
                        gasto.getMonto(),
                        gasto.getFechaMovimiento(),
                        "Gasto inusualmente alto",
                        promedio
                ));
            }
        }

        log.info("🚨 Detectados {} patrones inusuales para usuario {}", patrones.size(), usuarioId);
        return patrones;
    }

    // Métodos auxiliares

    private BigDecimal calcularPromedioMensual(List<MovimientoFinanciero> movimientos, int meses) {
        if (movimientos.isEmpty() || meses == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal total = movimientos.stream()
                .map(MovimientoFinanciero::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return total.divide(BigDecimal.valueOf(meses), 2, RoundingMode.HALF_UP);
    }

    private TendenciaInfo calcularTendencia(List<MovimientoFinanciero> movimientos, int meses) {
        if (movimientos.isEmpty() || meses < 2) {
            return new TendenciaInfo("ESTABLE", 0.0, "Datos insuficientes");
        }

        // Dividir en primera mitad y segunda mitad
        int mitad = movimientos.size() / 2;
        List<MovimientoFinanciero> primeraM = movimientos.subList(0, mitad);
        List<MovimientoFinanciero> segundaMitad = movimientos.subList(mitad, movimientos.size());

        BigDecimal totalPrimera = primeraM.stream()
                .map(MovimientoFinanciero::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalSegunda = segundaMitad.stream()
                .map(MovimientoFinanciero::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        double variacion = calcularVariacionPorcentual(totalPrimera, totalSegunda);

        String tipo;
        String descripcion;
        if (variacion > 5) {
            tipo = "CRECIENTE";
            descripcion = String.format("Incremento del %.1f%%", variacion);
        } else if (variacion < -5) {
            tipo = "DECRECIENTE";
            descripcion = String.format("Reducción del %.1f%%", Math.abs(variacion));
        } else {
            tipo = "ESTABLE";
            descripcion = "Sin cambios significativos";
        }

        return new TendenciaInfo(tipo, variacion, descripcion);
    }

    private Map<String, BigDecimal> analizarPorCategoria(List<MovimientoFinanciero> gastos) {
        Map<Long, BigDecimal> porCategoriaId = gastos.stream()
                .filter(g -> g.getCategoriaId() != null)
                .collect(Collectors.groupingBy(
                        MovimientoFinanciero::getCategoriaId,
                        Collectors.reducing(BigDecimal.ZERO,
                                MovimientoFinanciero::getMonto,
                                BigDecimal::add)
                ));

        // Convertir IDs a nombres (simplificado)
        return porCategoriaId.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> "Categoría #" + e.getKey(),
                        Map.Entry::getValue
                ));
    }

    private BigDecimal predecirProximoMes(List<MovimientoFinanciero> movimientos) {
        if (movimientos.isEmpty()) {
            return BigDecimal.ZERO;
        }

        // Predicción simple: promedio de los últimos 3 meses
        int mesesParaPrediccion = Math.min(3, movimientos.size());
        BigDecimal total = movimientos.stream()
                .limit(mesesParaPrediccion)
                .map(MovimientoFinanciero::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return total.divide(BigDecimal.valueOf(mesesParaPrediccion), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal calcularTotalPorTipo(List<MovimientoFinanciero> movimientos, boolean esIngreso) {
        return movimientos.stream()
                .filter(m -> esIngreso ? m.getTipoMovimiento().esIngreso()
                                       : m.getTipoMovimiento() == TipoMovimiento.EXPENSE)
                .map(MovimientoFinanciero::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private double calcularVariacionPorcentual(BigDecimal valorAnterior, BigDecimal valorNuevo) {
        if (valorAnterior.compareTo(BigDecimal.ZERO) == 0) {
            return valorNuevo.compareTo(BigDecimal.ZERO) > 0 ? 100.0 : 0.0;
        }

        return valorNuevo.subtract(valorAnterior)
                .divide(valorAnterior, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
    }

    private BigDecimal calcularDesviacionEstandar(List<MovimientoFinanciero> movimientos, BigDecimal promedio) {
        if (movimientos.size() < 2) {
            return BigDecimal.ZERO;
        }

        double varianza = movimientos.stream()
                .map(MovimientoFinanciero::getMonto)
                .map(m -> m.subtract(promedio).pow(2))
                .map(BigDecimal::doubleValue)
                .reduce(0.0, Double::sum) / movimientos.size();

        return BigDecimal.valueOf(Math.sqrt(varianza));
    }

    // DTOs internos

    public record TrendAnalysisResult(
            BigDecimal promedioIngresosMensual,
            BigDecimal promedioGastosMensual,
            TendenciaInfo tendenciaIngresos,
            TendenciaInfo tendenciaGastos,
            Map<String, BigDecimal> gastosPorCategoria,
            BigDecimal prediccionIngresos,
            BigDecimal prediccionGastos,
            String categoriaTopGasto,
            BigDecimal montoTopGasto,
            int mesesAnalizados
    ) {}

    public record TendenciaInfo(
            String tipo,  // CRECIENTE, DECRECIENTE, ESTABLE
            double porcentajeVariacion,
            String descripcion
    ) {}

    public record ComparacionPeriodos(
            BigDecimal ingresosPeriodo1,
            BigDecimal gastosPeriodo1,
            BigDecimal balancePeriodo1,
            BigDecimal ingresosPeriodo2,
            BigDecimal gastosPeriodo2,
            BigDecimal balancePeriodo2,
            double variacionIngresos,
            double variacionGastos,
            double variacionBalance
    ) {}

    public record PatronInusual(
            Long movimientoId,
            String descripcion,
            BigDecimal monto,
            LocalDate fecha,
            String razon,
            BigDecimal promedioNormal
    ) {}
}

