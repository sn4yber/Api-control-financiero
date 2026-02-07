package com.controfinanciero.infrastructure.web.controller;

import com.controfinanciero.domain.model.Usuario;
import com.controfinanciero.domain.repository.NotificacionRepository;
import com.controfinanciero.infrastructure.persistence.entity.NotificacionEntity;
import com.controfinanciero.infrastructure.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 🔔 Controlador de Notificaciones
 * Gestión de alertas y notificaciones del sistema
 */
@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionRepository notificacionRepo;
    private final AuthenticationService authService;

    /**
     * 📋 GET /api/notificaciones
     * Obtiene todas las notificaciones del usuario
     */
    @GetMapping
    public ResponseEntity<List<NotificacionEntity>> obtenerNotificaciones() {
        Usuario usuario = authService.getCurrentUser();
        List<NotificacionEntity> notificaciones = notificacionRepo
                .findByUsuarioIdOrderByCreatedAtDesc(usuario.getId());
        return ResponseEntity.ok(notificaciones);
    }

    /**
     * 🔴 GET /api/notificaciones/no-leidas
     * Obtiene solo las notificaciones no leídas
     */
    @GetMapping("/no-leidas")
    public ResponseEntity<List<NotificacionEntity>> obtenerNoLeidas() {
        Usuario usuario = authService.getCurrentUser();
        List<NotificacionEntity> notificaciones = notificacionRepo
                .findByUsuarioIdAndLeidaFalseOrderByCreatedAtDesc(usuario.getId());
        return ResponseEntity.ok(notificaciones);
    }

    /**
     * 🔵 GET /api/notificaciones/contador
     * Obtiene el contador de notificaciones no leídas
     */
    @GetMapping("/contador")
    public ResponseEntity<Map<String, Long>> obtenerContador() {
        Usuario usuario = authService.getCurrentUser();
        long contador = notificacionRepo.countNoLeidasByUsuarioId(usuario.getId());

        Map<String, Long> response = new HashMap<>();
        response.put("noLeidas", contador);

        return ResponseEntity.ok(response);
    }

    /**
     * ✅ PUT /api/notificaciones/{id}/marcar-leida
     * Marca una notificación como leída
     */
    @PutMapping("/{id}/marcar-leida")
    public ResponseEntity<NotificacionEntity> marcarComoLeida(@PathVariable Long id) {
        Usuario usuario = authService.getCurrentUser();

        NotificacionEntity notificacion = notificacionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));

        if (!notificacion.getUsuarioId().equals(usuario.getId())) {
            throw new RuntimeException("No autorizado");
        }

        notificacion.setLeida(true);
        notificacionRepo.save(notificacion);

        return ResponseEntity.ok(notificacion);
    }

    /**
     * ✅ PUT /api/notificaciones/marcar-todas-leidas
     * Marca todas las notificaciones como leídas
     */
    @PutMapping("/marcar-todas-leidas")
    public ResponseEntity<Map<String, String>> marcarTodasComoLeidas() {
        Usuario usuario = authService.getCurrentUser();

        List<NotificacionEntity> notificaciones = notificacionRepo
                .findByUsuarioIdAndLeidaFalseOrderByCreatedAtDesc(usuario.getId());

        notificaciones.forEach(n -> {
            n.setLeida(true);
            notificacionRepo.save(n);
        });

        Map<String, String> response = new HashMap<>();
        response.put("mensaje", String.format("%d notificaciones marcadas como leídas", notificaciones.size()));

        return ResponseEntity.ok(response);
    }

    /**
     * 🗑️ DELETE /api/notificaciones/{id}
     * Elimina una notificación
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarNotificacion(@PathVariable Long id) {
        Usuario usuario = authService.getCurrentUser();

        NotificacionEntity notificacion = notificacionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));

        if (!notificacion.getUsuarioId().equals(usuario.getId())) {
            throw new RuntimeException("No autorizado");
        }

        notificacionRepo.delete(notificacion);

        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Notificación eliminada");

        return ResponseEntity.ok(response);
    }
}

