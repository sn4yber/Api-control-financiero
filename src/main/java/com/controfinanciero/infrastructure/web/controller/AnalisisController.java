package com.controfinanciero.infrastructure.web.controller;

import com.controfinanciero.domain.model.Usuario;
import com.controfinanciero.infrastructure.security.service.AuthenticationService;
import com.controfinanciero.infrastructure.service.TrendAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * 📊 Controller de Análisis de Tendencias
 * Endpoints para análisis financiero avanzado
 */
@RestController
@RequestMapping("/api/analisis")
@RequiredArgsConstructor
public class AnalisisController {

    private final TrendAnalysisService trendAnalysisService;
    private final AuthenticationService authService;

    /**
     * GET /api/analisis/tendencias
     * Analiza tendencias de los últimos N meses
     */
    @GetMapping("/tendencias")
    public ResponseEntity<TrendAnalysisService.TrendAnalysisResult> analizarTendencias(
            @RequestParam(defaultValue = "6") int meses) {

        Usuario usuario = authService.getCurrentUser();

        TrendAnalysisService.TrendAnalysisResult resultado =
                trendAnalysisService.analizarTendencias(usuario.getId(), meses);

        return ResponseEntity.ok(resultado);
    }

    /**
     * GET /api/analisis/comparar-periodos
     * Compara dos períodos de tiempo
     */
    @GetMapping("/comparar-periodos")
    public ResponseEntity<TrendAnalysisService.ComparacionPeriodos> compararPeriodos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio1,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin1,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio2,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin2) {

        Usuario usuario = authService.getCurrentUser();

        TrendAnalysisService.ComparacionPeriodos comparacion =
                trendAnalysisService.compararPeriodos(usuario.getId(), inicio1, fin1, inicio2, fin2);

        return ResponseEntity.ok(comparacion);
    }

    /**
     * GET /api/analisis/patrones-inusuales
     * Detecta gastos inusuales en los últimos N días
     */
    @GetMapping("/patrones-inusuales")
    public ResponseEntity<?> detectarPatrones(
            @RequestParam(defaultValue = "30") int dias) {

        Usuario usuario = authService.getCurrentUser();

        var patrones = trendAnalysisService.detectarPatronesInusuales(usuario.getId(), dias);

        return ResponseEntity.ok(new PatronesResponse(
                patrones.size(),
                patrones
        ));
    }

    record PatronesResponse(
            int totalDetectados,
            java.util.List<TrendAnalysisService.PatronInusual> patrones
    ) {}
}

