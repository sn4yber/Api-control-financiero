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
     * Compartir una meta con otro usuario
     */
    @PostMapping("/{metaId}/compartir")
    public ResponseEntity<?> compartirMeta(
            @PathVariable Long metaId,
            @RequestBody CompartirMetaRequest request) {

        Usuario usuario = authService.getCurrentUser();

        MetaColaboradorEntity colaborador = sharedGoalsService.compartirMeta(
                metaId,
                usuario.getId(),
                request.usuarioInvitadoId()
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

    // DTOs
    record CompartirMetaRequest(Long usuarioInvitadoId) {}

    record AportarRequest(BigDecimal monto, String descripcion) {}

    record MensajeResponse(String mensaje, BigDecimal monto) {}
}

