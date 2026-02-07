package com.controfinanciero.infrastructure.web.controller;

import com.controfinanciero.domain.model.MovimientoFinanciero;
import com.controfinanciero.domain.model.Usuario;
import com.controfinanciero.infrastructure.security.service.AuthenticationService;
import com.controfinanciero.infrastructure.service.FinancialIntelligenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 🧠 Controlador de Inteligencia Financiera
 * Predicciones, anomalías y recomendaciones personalizadas
 */
@RestController
@RequestMapping("/api/inteligencia")
@RequiredArgsConstructor
public class InteligenciaController {

    private final FinancialIntelligenceService intelligenceService;
    private final AuthenticationService authService;

    /**
     * 📈 GET /api/inteligencia/prediccion
     * Predice el gasto probable del mes actual
     */
    @GetMapping("/prediccion")
    public ResponseEntity<Map<String, Object>> obtenerPrediccion() {
        Usuario usuario = authService.getCurrentUser();
        Map<String, Object> prediccion = intelligenceService.predecirGastosMesActual(usuario.getId());
        return ResponseEntity.ok(prediccion);
    }

    /**
     * 🚨 GET /api/inteligencia/anomalias
     * Detecta movimientos anómalos (gastos inusualmente altos)
     */
    @GetMapping("/anomalias")
    public ResponseEntity<List<MovimientoFinanciero>> detectarAnomalias() {
        Usuario usuario = authService.getCurrentUser();
        List<MovimientoFinanciero> anomalias = intelligenceService.detectarAnomalias(usuario.getId());
        return ResponseEntity.ok(anomalias);
    }

    /**
     * 💡 GET /api/inteligencia/recomendaciones
     * Genera recomendaciones personalizadas de ahorro
     */
    @GetMapping("/recomendaciones")
    public ResponseEntity<Map<String, Object>> obtenerRecomendaciones() {
        Usuario usuario = authService.getCurrentUser();
        List<String> recomendaciones = intelligenceService.generarRecomendaciones(usuario.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("recomendaciones", recomendaciones);
        response.put("total", recomendaciones.size());

        return ResponseEntity.ok(response);
    }

    /**
     * 📊 GET /api/inteligencia/dashboard
     * Dashboard completo con todas las métricas de inteligencia
     */
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> obtenerDashboard() {
        Usuario usuario = authService.getCurrentUser();

        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("prediccion", intelligenceService.predecirGastosMesActual(usuario.getId()));
        dashboard.put("anomalias", intelligenceService.detectarAnomalias(usuario.getId()));
        dashboard.put("recomendaciones", intelligenceService.generarRecomendaciones(usuario.getId()));

        return ResponseEntity.ok(dashboard);
    }
}

