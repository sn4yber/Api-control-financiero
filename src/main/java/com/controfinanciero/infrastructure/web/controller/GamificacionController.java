package com.controfinanciero.infrastructure.web.controller;
import com.controfinanciero.domain.model.Usuario;
import com.controfinanciero.infrastructure.security.service.AuthenticationService;
import com.controfinanciero.infrastructure.service.GamificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/**
 * 🎮 Controller de Gamificación
 */
@RestController
@RequestMapping("/api/gamificacion")
@RequiredArgsConstructor
public class GamificacionController {
    private final GamificationService gamificationService;
    private final AuthenticationService authService;
    @GetMapping("/logros")
    public ResponseEntity<?> obtenerLogros() {
        Usuario usuario = authService.getCurrentUser();
        var logros = gamificationService.obtenerLogrosUsuario(usuario.getId());
        return ResponseEntity.ok(logros);
    }
    @GetMapping("/racha")
    public ResponseEntity<?> obtenerRacha() {
        Usuario usuario = authService.getCurrentUser();
        var racha = gamificationService.obtenerRacha(usuario.getId());
        return ResponseEntity.ok(racha);
    }
    @GetMapping("/estadisticas")
    public ResponseEntity<?> obtenerEstadisticas() {
        Usuario usuario = authService.getCurrentUser();
        var stats = gamificationService.obtenerEstadisticas(usuario.getId());
        return ResponseEntity.ok(stats);
    }
    @PostMapping("/logros/{id}/reclamar")
    public ResponseEntity<?> reclamarLogro(@PathVariable Long id) {
        gamificationService.reclamarLogro(id);
        return ResponseEntity.ok().build();
    }
}
