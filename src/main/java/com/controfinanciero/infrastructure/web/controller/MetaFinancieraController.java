package com.controfinanciero.infrastructure.web.controller;

import com.controfinanciero.application.dto.CrearMetaFinancieraCommand;
import com.controfinanciero.application.dto.MetaFinancieraDTO;
import com.controfinanciero.application.usecase.Meta.CrearMetaFinancieraUseCase;
import com.controfinanciero.application.usecase.Meta.ObtenerMetasFinancierasUseCase;
import com.controfinanciero.domain.model.enums.EstadoMeta;
import com.controfinanciero.infrastructure.web.dto.request.CrearMetaFinancieraRequest;
import com.controfinanciero.infrastructure.web.dto.response.MetaFinancieraResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller para gestionar metas financieras
 */
@RestController
@RequestMapping("/api/metas")
public class MetaFinancieraController {

    private final CrearMetaFinancieraUseCase crearMetaUseCase;
    private final ObtenerMetasFinancierasUseCase obtenerMetasUseCase;

    public MetaFinancieraController(
            CrearMetaFinancieraUseCase crearMetaUseCase,
            ObtenerMetasFinancierasUseCase obtenerMetasUseCase) {
        this.crearMetaUseCase = crearMetaUseCase;
        this.obtenerMetasUseCase = obtenerMetasUseCase;
    }

    @PostMapping
    public ResponseEntity<MetaFinancieraResponse> crearMeta(
            @Valid @RequestBody CrearMetaFinancieraRequest request) {

        CrearMetaFinancieraCommand command = new CrearMetaFinancieraCommand(
                request.usuarioId(),
                request.nombre(),
                request.descripcion(),
                request.montoObjetivo(),
                request.fechaObjetivo(),
                request.prioridad(),
                null // Estado se asigna automáticamente como ACTIVE
        );

        MetaFinancieraDTO dto = crearMetaUseCase.ejecutar(command);
        MetaFinancieraResponse response = toResponse(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<MetaFinancieraResponse>> obtenerMetasPorUsuario(
            @PathVariable Long usuarioId,
            @RequestParam(required = false) EstadoMeta estado) {

        List<MetaFinancieraDTO> dtos = estado != null
                ? obtenerMetasUseCase.ejecutarPorEstado(usuarioId, estado)
                : obtenerMetasUseCase.ejecutar(usuarioId);

        List<MetaFinancieraResponse> responses = dtos.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    private MetaFinancieraResponse toResponse(MetaFinancieraDTO dto) {
        return new MetaFinancieraResponse(
                dto.id(),
                dto.usuarioId(),
                dto.nombre(),
                dto.descripcion(),
                dto.montoObjetivo(),
                dto.montoActual(),
                dto.fechaObjetivo(),
                dto.prioridad(),
                dto.estado(),
                dto.createdAt(),
                dto.updatedAt(),
                dto.completedAt()
        );
    }
}
