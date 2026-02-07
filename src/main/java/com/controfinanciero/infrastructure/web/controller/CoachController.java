package com.controfinanciero.infrastructure.web.controller;

import com.controfinanciero.domain.model.Usuario;
import com.controfinanciero.infrastructure.security.service.AuthenticationService;
import com.controfinanciero.infrastructure.service.FinancialCoachService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 💬 Controller de Coach Financiero IA
 */
@RestController
@RequestMapping("/api/coach")
@RequiredArgsConstructor
public class CoachController {

    private final FinancialCoachService coachService;
    private final AuthenticationService authService;

    @GetMapping("/consejo-del-dia")
    public ResponseEntity<?> obtenerConsejo() {
        Usuario usuario = authService.getCurrentUser();
        var consejo = coachService.generarConsejoDelDia(usuario.getId());
        return ResponseEntity.ok(consejo);
    }

    @GetMapping("/analisis-habitos")
    public ResponseEntity<?> analizarHabitos() {
        Usuario usuario = authService.getCurrentUser();
        var analisis = coachService.analizarHabitos(usuario.getId());
        return ResponseEntity.ok(analisis);
    }
}

