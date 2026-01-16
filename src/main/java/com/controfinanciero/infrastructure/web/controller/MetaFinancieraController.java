package com.controfinanciero.infrastructure.web.controller;

import com.controfinanciero.application.dto.CrearMetaFinancieraCommand;
import com.controfinanciero.application.dto.MetaFinancieraDTO;
import com.controfinanciero.application.usecase.Meta.CrearMetaFinancieraUseCase;
import com.controfinanciero.application.usecase.Meta.ObtenerMetasFinancierasUseCase;
import com.controfinanciero.domain.model.enums.EstadoMeta;
import com.controfinanciero.domain.repository.MetaFinancieraRepository;
import com.controfinanciero.infrastructure.security.AuthenticatedUserService;
import com.controfinanciero.infrastructure.web.dto.request.CrearMetaFinancieraRequest;
import com.controfinanciero.infrastructure.web.dto.response.MetaFinancieraResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller para gestionar metas financieras
 * ✅ Sin userId en URLs (usa token JWT)
 */
@RestController
@RequestMapping("/api/metas")
@RequiredArgsConstructor
public class MetaFinancieraController {

    private final CrearMetaFinancieraUseCase crearMetaUseCase;
    private final ObtenerMetasFinancierasUseCase obtenerMetasUseCase;
    private final MetaFinancieraRepository metaRepository;
    private final AuthenticatedUserService authenticatedUserService;

    @PostMapping
    public ResponseEntity<MetaFinancieraResponse> crearMeta(@Valid @RequestBody CrearMetaFinancieraRequest request) {
        Long userId = authenticatedUserService.getCurrentUserId();

        CrearMetaFinancieraCommand command = new CrearMetaFinancieraCommand(
                userId,
                request.nombre(),
                request.descripcion(),
                request.montoObjetivo(),
                request.fechaObjetivo(),
                request.prioridad(),
                EstadoMeta.ACTIVE
        );

        MetaFinancieraDTO dto = crearMetaUseCase.ejecutar(command);
        MetaFinancieraResponse response = toResponse(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<MetaFinancieraResponse>> obtenerMisMetas() {
        Long userId = authenticatedUserService.getCurrentUserId();

        List<MetaFinancieraDTO> dtos = obtenerMetasUseCase.ejecutar(userId);
        List<MetaFinancieraResponse> responses = dtos.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerMetaPorId(@PathVariable Long id) {
        Long userId = authenticatedUserService.getCurrentUserId();

        var meta = metaRepository.findById(id).orElse(null);

        if (meta == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Meta no encontrada"));
        }

        if (!meta.getUsuarioId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse("No tienes permiso para ver esta meta"));
        }

        var dto = new MetaFinancieraDTO(
                meta.getId(),
                meta.getUsuarioId(),
                meta.getNombre(),
                meta.getDescripcion(),
                meta.getMontoObjetivo(),
                meta.getMontoActual(),
                meta.getFechaObjetivo(),
                meta.getPrioridad(),
                meta.getEstado(),
                meta.getCreatedAt(),
                meta.getUpdatedAt(),
                meta.getCompletedAt()
        );

        return ResponseEntity.ok(toResponse(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarMeta(@PathVariable Long id) {
        Long userId = authenticatedUserService.getCurrentUserId();

        var meta = metaRepository.findById(id).orElse(null);

        if (meta == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Meta no encontrada"));
        }

        if (!meta.getUsuarioId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse("No tienes permiso para eliminar esta meta"));
        }

        try {
            metaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse(
                            "No se puede eliminar esta meta porque tiene movimientos de ahorro asociados. " +
                            "Por favor, elimina los movimientos primero."
                    ));
        }
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

    record ErrorResponse(String message) {}
}

