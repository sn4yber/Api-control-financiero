package com.controfinanciero.infrastructure.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 💚 Health & Warmup Controller
 * Endpoint ultra-ligero para keep-alive y warmup
 */
@RestController
@RequestMapping("/api")
public class HealthController {

    private final LocalDateTime startupTime = LocalDateTime.now();

    /**
     * 🔥 Endpoint de WARMUP - Ultra rápido, sin DB
     * Úsalo desde tu frontend cada 10 minutos para evitar cold start
     */
    @GetMapping("/ping")
    public ResponseEntity<Map<String, Object>> ping() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "alive");
        response.put("timestamp", LocalDateTime.now());
        response.put("uptime_seconds", java.time.Duration.between(startupTime, LocalDateTime.now()).getSeconds());
        return ResponseEntity.ok(response);
    }

    /**
     * 🚀 Health Check básico (sin autenticación)
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "API Control Financiero");
        return ResponseEntity.ok(response);
    }
}

