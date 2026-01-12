package com.controfinanciero.infrastructure.web.controller;

import com.controfinanciero.application.dto.ContextoFinancieroDTO;
import com.controfinanciero.application.dto.CrearContextoFinancieroCommand;
import com.controfinanciero.application.usecase.contexto.CrearContextoFinancieroUseCase;
import com.controfinanciero.application.usecase.contexto.ObtenerContextoFinancieroUseCase;
import com.controfinanciero.infrastructure.web.dto.request.CrearContextoFinancieroRequest;
import com.controfinanciero.infrastructure.web.dto.response.ContextoFinancieroResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST para operaciones de Contexto Financiero.
 */
@RestController
@RequestMapping("/api/contextos-financieros")
public class ContextoFinancieroController {

    private final CrearContextoFinancieroUseCase crearContextoUseCase;
    private final ObtenerContextoFinancieroUseCase obtenerContextoUseCase;

    public ContextoFinancieroController(
            CrearContextoFinancieroUseCase crearContextoUseCase,
            ObtenerContextoFinancieroUseCase obtenerContextoUseCase
    ) {
        this.crearContextoUseCase = crearContextoUseCase;
        this.obtenerContextoUseCase = obtenerContextoUseCase;
    }

    /**
     * POST /api/contextos-financieros - Crear contexto financiero
     */
    @PostMapping
    public ResponseEntity<ContextoFinancieroResponse> crearContexto(
            @Valid @RequestBody CrearContextoFinancieroRequest request
    ) {
        // Convertir request a command
        CrearContextoFinancieroCommand command = new CrearContextoFinancieroCommand(
                request.usuarioId(),
                request.tipoIngreso(),
                request.tieneIngresoVariable(),
                request.porcentajeAhorroDeseado(),
                request.periodoAnalisis(),
                request.diasPeriodoPersonalizado()
        );

        // Ejecutar caso de uso
        ContextoFinancieroDTO dto = crearContextoUseCase.ejecutar(command);

        // Convertir a response
        ContextoFinancieroResponse response = mapearAResponse(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/contextos-financieros/usuario/{usuarioId} - Obtener contexto por usuario
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<ContextoFinancieroResponse> obtenerPorUsuario(
            @PathVariable Long usuarioId
    ) {
        ContextoFinancieroDTO dto = obtenerContextoUseCase.ejecutarPorUsuarioId(usuarioId);
        ContextoFinancieroResponse response = mapearAResponse(dto);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/contextos-financieros/{id} - Obtener contexto por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContextoFinancieroResponse> obtenerPorId(
            @PathVariable Long id
    ) {
        ContextoFinancieroDTO dto = obtenerContextoUseCase.ejecutarPorId(id);
        ContextoFinancieroResponse response = mapearAResponse(dto);
        return ResponseEntity.ok(response);
    }

    private ContextoFinancieroResponse mapearAResponse(ContextoFinancieroDTO dto) {
        return new ContextoFinancieroResponse(
                dto.id(),
                dto.usuarioId(),
                dto.tipoIngreso(),
                dto.tieneIngresoVariable(),
                dto.porcentajeAhorroDeseado(),
                dto.periodoAnalisis(),
                dto.diasPeriodoPersonalizado(),
                dto.codigoMoneda(),
                dto.createdAt(),
                dto.updatedAt()
        );
    }
}
