package com.controfinanciero.infrastructure.web.controller;

import com.controfinanciero.application.dto.CrearFuenteIngresoCommand;
import com.controfinanciero.application.dto.FuenteIngresoDTO;
import com.controfinanciero.application.usecase.fuente.CrearFuenteIngresoUseCase;
import com.controfinanciero.application.usecase.fuente.ObtenerFuentesIngresoUseCase;
import com.controfinanciero.infrastructure.security.AuthenticatedUserService;
import com.controfinanciero.infrastructure.web.dto.request.CrearFuenteIngresoRequest;
import com.controfinanciero.infrastructure.web.dto.response.FuenteIngresoResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller para gestionar fuentes de ingreso
 */
@RestController
@RequestMapping("/api/fuentes-ingreso")
public class FuenteIngresoController {

    private final CrearFuenteIngresoUseCase crearFuenteIngresoUseCase;
    private final ObtenerFuentesIngresoUseCase obtenerFuentesIngresoUseCase;
    private final AuthenticatedUserService authenticatedUserService;

    public FuenteIngresoController(
            CrearFuenteIngresoUseCase crearFuenteIngresoUseCase,
            ObtenerFuentesIngresoUseCase obtenerFuentesIngresoUseCase,
            AuthenticatedUserService authenticatedUserService) {
        this.crearFuenteIngresoUseCase = crearFuenteIngresoUseCase;
        this.obtenerFuentesIngresoUseCase = obtenerFuentesIngresoUseCase;
        this.authenticatedUserService = authenticatedUserService;
    }

    @PostMapping
    public ResponseEntity<FuenteIngresoResponse> crearFuenteIngreso(
            @Valid @RequestBody CrearFuenteIngresoRequest request) {

        CrearFuenteIngresoCommand command = new CrearFuenteIngresoCommand(
                request.usuarioId(),
                request.nombre(),
                request.descripcion(),
                request.tipoFuente(),
                request.esIngresoReal(),
                request.activa()
        );

        FuenteIngresoDTO dto = crearFuenteIngresoUseCase.ejecutar(command);
        FuenteIngresoResponse response = toResponse(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/fuentes-ingreso - Obtener fuentes de ingreso del usuario autenticado
     */
    @GetMapping
    public ResponseEntity<List<FuenteIngresoResponse>> obtenerMisFuentesIngreso(
            @RequestParam(required = false, defaultValue = "false") Boolean soloActivas) {

        Long usuarioId = authenticatedUserService.getCurrentUserId();

        List<FuenteIngresoDTO> dtos = soloActivas
                ? obtenerFuentesIngresoUseCase.ejecutarActivas(usuarioId)
                : obtenerFuentesIngresoUseCase.ejecutar(usuarioId);

        List<FuenteIngresoResponse> responses = dtos.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<FuenteIngresoResponse>> obtenerFuentesPorUsuario(
            @PathVariable Long usuarioId,
            @RequestParam(required = false, defaultValue = "false") Boolean soloActivas) {

        List<FuenteIngresoDTO> dtos = soloActivas
                ? obtenerFuentesIngresoUseCase.ejecutarActivas(usuarioId)
                : obtenerFuentesIngresoUseCase.ejecutar(usuarioId);

        List<FuenteIngresoResponse> responses = dtos.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    private FuenteIngresoResponse toResponse(FuenteIngresoDTO dto) {
        return new FuenteIngresoResponse(
                dto.id(),
                dto.usuarioId(),
                dto.nombre(),
                dto.descripcion(),
                dto.tipoFuente(),
                dto.esIngresoReal(),
                dto.activa(),
                dto.createdAt(),
                dto.updatedAt()
        );
    }
}

