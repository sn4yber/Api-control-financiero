package com.controfinanciero.infrastructure.service;

import com.controfinanciero.domain.model.MovimientoFinanciero;
import com.controfinanciero.domain.repository.MovimientoFinancieroRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 🧠 Servicio de Inteligencia Financiera
 * Predicciones, detección de anomalías y recomendaciones personalizadas
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FinancialIntelligenceService {

    private final MovimientoFinancieroRepository movimientoRepo;

    /**
     * 📈 Predice el gasto probable del mes actual basado en historial
     */
    public Map<String, Object> predecirGastosMesActual(Long usuarioId) {
        log.info("🧠 Generando predicción de gastos para usuario #{}", usuarioId);

        LocalDate hoy = LocalDate.now();
        LocalDate inicioMesActual = hoy.withDayOfMonth(1);

        // Obtener gastos de los últimos 6 meses
        LocalDate hace6Meses = hoy.minusMonths(6);
        List<MovimientoFinanciero> movimientosHistoricos = movimientoRepo
                .findByUsuarioIdAndFechaMovimientoBetweenOrderByFechaMovimientoDesc(
                        usuarioId, hace6Meses, hoy);

        // Filtrar solo gastos y calcular promedio mensual
        BigDecimal sumaGastos = BigDecimal.ZERO;
        int mesesContados = 6;

        for (MovimientoFinanciero m : movimientosHistoricos) {
            if (m.getTipoMovimiento().name().equals("EXPENSE")) {
                sumaGastos = sumaGastos.add(m.getMonto());
            }
        }

        BigDecimal promedioMensual = sumaGastos.divide(BigDecimal.valueOf(mesesContados), 2, RoundingMode.HALF_UP);

        // Obtener gasto actual del mes
        List<MovimientoFinanciero> movimientosMesActual = movimientoRepo
                .findByUsuarioIdAndFechaMovimientoBetweenOrderByFechaMovimientoDesc(
                        usuarioId, inicioMesActual, hoy);

        BigDecimal gastoMesActual = movimientosMesActual.stream()
                .filter(m -> m.getTipoMovimiento().name().equals("EXPENSE"))
                .map(MovimientoFinanciero::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calcular proyección
        int diasTranscurridos = hoy.getDayOfMonth();
        int diasTotalesMes = hoy.lengthOfMonth();
        BigDecimal tasaDiaria = gastoMesActual.divide(BigDecimal.valueOf(diasTranscurridos), 2, RoundingMode.HALF_UP);
        BigDecimal proyeccionFinMes = tasaDiaria.multiply(BigDecimal.valueOf(diasTotalesMes));

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("promedioMensualHistorico", promedioMensual);
        resultado.put("gastoActualMes", gastoMesActual);
        resultado.put("proyeccionFinMes", proyeccionFinMes);
        resultado.put("diasTranscurridos", diasTranscurridos);
        resultado.put("diasTotalesMes", diasTotalesMes);
        resultado.put("mensaje", generarMensajePrediccion(promedioMensual, proyeccionFinMes));

        log.info("✅ Predicción generada: Promedio histórico ${}, Proyección ${}", promedioMensual, proyeccionFinMes);

        return resultado;
    }

    /**
     * 🚨 Detecta movimientos anómalos (gastos inusualmente altos)
     */
    public List<MovimientoFinanciero> detectarAnomalias(Long usuarioId) {
        log.info("🚨 Detectando anomalías para usuario #{}", usuarioId);

        LocalDate hace3Meses = LocalDate.now().minusMonths(3);
        List<MovimientoFinanciero> movimientos = movimientoRepo
                .findByUsuarioIdAndFechaMovimientoBetweenOrderByFechaMovimientoDesc(
                        usuarioId, hace3Meses, LocalDate.now());

        // Calcular promedio y desviación estándar de gastos
        List<BigDecimal> gastosHistoricos = movimientos.stream()
                .filter(m -> m.getTipoMovimiento().name().equals("EXPENSE"))
                .map(MovimientoFinanciero::getMonto)
                .toList();

        if (gastosHistoricos.isEmpty()) {
            return List.of();
        }

        BigDecimal promedio = gastosHistoricos.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(gastosHistoricos.size()), 2, RoundingMode.HALF_UP);

        // Calcular desviación estándar (simplificada)
        double sumaCuadrados = gastosHistoricos.stream()
                .mapToDouble(g -> Math.pow(g.subtract(promedio).doubleValue(), 2))
                .sum();
        double desviacionEstandar = Math.sqrt(sumaCuadrados / gastosHistoricos.size());

        // Umbral: Promedio + 2 desviaciones estándar
        BigDecimal umbralAnomalia = promedio.add(BigDecimal.valueOf(desviacionEstandar * 2));

        // Filtrar movimientos anómalos
        List<MovimientoFinanciero> anomalias = movimientos.stream()
                .filter(m -> m.getTipoMovimiento().name().equals("EXPENSE"))
                .filter(m -> m.getMonto().compareTo(umbralAnomalia) > 0)
                .toList();

        log.info("🚨 Detectadas {} anomalías. Umbral: ${}", anomalias.size(), umbralAnomalia);

        return anomalias;
    }

    /**
     * 💡 Genera recomendaciones personalizadas
     */
    public List<String> generarRecomendaciones(Long usuarioId) {
        log.info("💡 Generando recomendaciones para usuario #{}", usuarioId);

        Map<String, Object> prediccion = predecirGastosMesActual(usuarioId);
        List<MovimientoFinanciero> anomalias = detectarAnomalias(usuarioId);

        // TODO: Implementar lógica más sofisticada
        List<String> recomendaciones = new java.util.ArrayList<>();

        BigDecimal proyeccion = (BigDecimal) prediccion.get("proyeccionFinMes");
        BigDecimal promedio = (BigDecimal) prediccion.get("promedioMensualHistorico");

        if (proyeccion.compareTo(promedio.multiply(BigDecimal.valueOf(1.2))) > 0) {
            recomendaciones.add("⚠️ Tus gastos proyectados superan tu promedio histórico en más del 20%. Considera revisar tus gastos.");
        }

        if (!anomalias.isEmpty()) {
            recomendaciones.add(String.format("🚨 Detectamos %d gastos inusualmente altos en los últimos 3 meses. Revisa si son necesarios.", anomalias.size()));
        }

        if (recomendaciones.isEmpty()) {
            recomendaciones.add("✅ ¡Vas por buen camino! Tus gastos están dentro de lo normal.");
        }

        log.info("💡 {} recomendaciones generadas", recomendaciones.size());

        return recomendaciones;
    }

    private String generarMensajePrediccion(BigDecimal promedioHistorico, BigDecimal proyeccion) {
        if (proyeccion.compareTo(promedioHistorico) > 0) {
            BigDecimal diferencia = proyeccion.subtract(promedioHistorico);
            BigDecimal porcentaje = diferencia.divide(promedioHistorico, 2, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            return String.format("⚠️ Proyectas gastar $%s más que tu promedio histórico (+%s%%)",
                    diferencia, porcentaje.intValue());
        } else {
            return "✅ Tus gastos proyectados están dentro de tu promedio histórico";
        }
    }
}

