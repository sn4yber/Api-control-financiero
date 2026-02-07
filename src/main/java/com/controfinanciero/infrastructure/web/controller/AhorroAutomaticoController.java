package com.controfinanciero.infrastructure.web.controller;

import com.controfinanciero.domain.model.Usuario;
import com.controfinanciero.infrastructure.security.service.AuthenticationService;
import com.controfinanciero.infrastructure.service.AutomaticSavingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 💡 Controller de Ahorro Automático
 */
@RestController
@RequestMapping("/api/ahorro-automatico")
@RequiredArgsConstructor
public class AhorroAutomaticoController {

    private final AutomaticSavingsService automaticSavingsService;
    private final AuthenticationService authService;

    @PostMapping("/configurar")
    public ResponseEntity<?> configurar(@RequestBody ConfigurarRequest request) {
        Usuario usuario = authService.getCurrentUser();
        var config = automaticSavingsService.configurar(
                usuario.getId(),
                request.metaDestinoId,
                request.tipoRedondeo
        );
        return ResponseEntity.ok(config);
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<?> obtenerEstadisticas() {
        Usuario usuario = authService.getCurrentUser();
        var stats = automaticSavingsService.obtenerEstadisticas(usuario.getId());
        return ResponseEntity.ok(stats);
    }

    @PostMapping("/pausar")
    public ResponseEntity<?> pausar() {
        Usuario usuario = authService.getCurrentUser();
        automaticSavingsService.pausar(usuario.getId());
        return ResponseEntity.ok().build();
    }

    record ConfigurarRequest(Long metaDestinoId, String tipoRedondeo) {}
}

