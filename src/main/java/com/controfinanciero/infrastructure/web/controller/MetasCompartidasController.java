package com.controfinanciero.infrastructure.web.controller;

import com.controfinanciero.domain.model.Usuario;
import com.controfinanciero.infrastructure.persistence.entity.MetaColaboradorEntity;
import com.controfinanciero.infrastructure.security.service.AuthenticationService;
import com.controfinanciero.infrastructure.service.SharedGoalsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 🤝 Controller de Metas Compartidas
 */
@RestController
@RequestMapping("/api/metas-compartidas")
@RequiredArgsConstructor
public class MetasCompartidasController {

    private final SharedGoalsService sharedGoalsService;
    private final AuthenticationService authService;

    /**
     * POST /api/metas-compartidas/{metaId}/compartir
     * Compartir una meta con otro usuario por username
     */
    @PostMapping("/{metaId}/compartir")
    public ResponseEntity<?> compartirMeta(
            @PathVariable Long metaId,
            @RequestBody CompartirMetaRequest request) {

        Usuario usuario = authService.getCurrentUser();

        MetaColaboradorEntity colaborador = sharedGoalsService.compartirMetaPorUsername(
                metaId,
                usuario.getId(),
                usuario.getUsername(),
                request.usernameInvitado()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(colaborador);
    }

    /**
     * POST /api/metas-compartidas/{metaId}/aportar
     * Realizar un aporte a una meta compartida
     */
    @PostMapping("/{metaId}/aportar")
    public ResponseEntity<?> realizarAporte(
            @PathVariable Long metaId,
            @RequestBody AportarRequest request) {

        Usuario usuario = authService.getCurrentUser();

        sharedGoalsService.registrarAporte(
                metaId,
                usuario.getId(),
                request.monto()
        );

        return ResponseEntity.ok(new MensajeResponse(
                "Aporte registrado exitosamente",
                request.monto()
        ));
    }

    /**
     * GET /api/metas-compartidas/{metaId}/colaboradores
     * Obtener lista de colaboradores de una meta
     */
    @GetMapping("/{metaId}/colaboradores")
    public ResponseEntity<?> obtenerColaboradores(@PathVariable Long metaId) {
        List<MetaColaboradorEntity> colaboradores =
                sharedGoalsService.obtenerColaboradores(metaId);

        return ResponseEntity.ok(colaboradores);
    }

    /**
     * GET /api/metas-compartidas/{metaId}/aportes
     * Obtener historial de aportes de una meta compartida
     */
    @GetMapping("/{metaId}/aportes")
    public ResponseEntity<?> obtenerAportes(@PathVariable Long metaId) {
        var aportes = sharedGoalsService.obtenerAportesDetallados(metaId);
        return ResponseEntity.ok(aportes);
    }

    /**
     * GET /api/metas-compartidas/mis-metas
     * Obtener metas donde el usuario es colaborador
     */
    @GetMapping("/mis-metas")
    public ResponseEntity<?> obtenerMisMetasCompartidas() {
        Usuario usuario = authService.getCurrentUser();
        List<MetaColaboradorEntity> misColaboraciones =
                sharedGoalsService.obtenerMisColaboraciones(usuario.getId());

        return ResponseEntity.ok(misColaboraciones);
    }

    /**
     * POST /api/metas-compartidas/{metaId}/aceptar
     * Aceptar invitación a meta compartida
     */
    @PostMapping("/{metaId}/aceptar")
    public ResponseEntity<?> aceptarInvitacion(@PathVariable Long metaId) {
        Usuario usuario = authService.getCurrentUser();
        sharedGoalsService.aceptarInvitacion(metaId, usuario.getId());

        return ResponseEntity.ok(new MensajeResponse(
                "Invitación aceptada exitosamente",
                null
        ));
    }

    /**
     * DELETE /api/metas-compartidas/{metaId}/salir
     * Salir de una meta compartida
     */
    @DeleteMapping("/{metaId}/salir")
    public ResponseEntity<?> salirDeMeta(@PathVariable Long metaId) {
        Usuario usuario = authService.getCurrentUser();
        sharedGoalsService.salirDeMeta(metaId, usuario.getId());

        return ResponseEntity.ok(new MensajeResponse(
                "Has salido de la meta compartida",
                null
        ));
    }

    /**
     * DELETE /api/metas-compartidas/{metaId}/colaborador/{colaboradorId}
     * Eliminar colaborador de una meta (solo creador)
     */
    @DeleteMapping("/{metaId}/colaborador/{colaboradorId}")
    public ResponseEntity<?> eliminarColaborador(
            @PathVariable Long metaId,
            @PathVariable Long colaboradorId) {

        Usuario usuario = authService.getCurrentUser();
        sharedGoalsService.eliminarColaborador(metaId, usuario.getId(), colaboradorId);

        return ResponseEntity.ok(new MensajeResponse(
                "Colaborador eliminado exitosamente",
                null
        ));
    }

    // DTOs
    record CompartirMetaRequest(String usernameInvitado) {}

    record AportarRequest(BigDecimal monto, String descripcion) {}

    record MensajeResponse(String mensaje, BigDecimal monto) {}
}

