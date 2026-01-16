package com.controfinanciero.infrastructure.web.controller;

import com.controfinanciero.application.dto.CategoriaDTO;
import com.controfinanciero.application.dto.CrearCategoriaCommand;
import com.controfinanciero.application.usecase.categoria.CrearCategoriaUseCase;
import com.controfinanciero.application.usecase.categoria.ObtenerCategoriasUseCase;
import com.controfinanciero.domain.repository.CategoriaRepository;
import com.controfinanciero.infrastructure.security.AuthenticatedUserService;
import com.controfinanciero.infrastructure.web.dto.request.CrearCategoriaRequest;
import com.controfinanciero.infrastructure.web.dto.response.CategoriaResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller para gestionar categorías
 * ✅ Sin userId en URLs (usa token JWT)
 */
@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CrearCategoriaUseCase crearCategoriaUseCase;
    private final ObtenerCategoriasUseCase obtenerCategoriasUseCase;
    private final CategoriaRepository categoriaRepository;
    private final AuthenticatedUserService authenticatedUserService;

    @PostMapping
    public ResponseEntity<CategoriaResponse> crearCategoria(@Valid @RequestBody CrearCategoriaRequest request) {
        Long userId = authenticatedUserService.getCurrentUserId();

        CrearCategoriaCommand command = new CrearCategoriaCommand(
                userId,
                request.nombre(),
                request.descripcion(),
                request.tipoCategoria(),
                request.colorHex(),
                request.icono()
        );

        CategoriaDTO dto = crearCategoriaUseCase.ejecutar(command);
        CategoriaResponse response = toResponse(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> obtenerMisCategorias() {
        Long userId = authenticatedUserService.getCurrentUserId();

        List<CategoriaDTO> dtos = obtenerCategoriasUseCase.ejecutarPorUsuario(userId);
        List<CategoriaResponse> responses = dtos.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerCategoriaPorId(@PathVariable Long id) {
        Long userId = authenticatedUserService.getCurrentUserId();

        var categoria = categoriaRepository.findById(id).orElse(null);

        if (categoria == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Categoría no encontrada"));
        }

        if (!categoria.getUsuarioId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse("No tienes permiso para ver esta categoría"));
        }

        var dto = new CategoriaDTO(
                categoria.getId(),
                categoria.getUsuarioId(),
                categoria.getNombre(),
                categoria.getDescripcion(),
                categoria.getColorHex(),
                categoria.getIcono(),
                categoria.getTipoCategoria(),
                categoria.isActiva(),
                categoria.getCreatedAt(),
                categoria.getUpdatedAt()
        );

        return ResponseEntity.ok(toResponse(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCategoria(@PathVariable Long id) {
        Long userId = authenticatedUserService.getCurrentUserId();

        var categoria = categoriaRepository.findById(id).orElse(null);

        if (categoria == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Categoría no encontrada"));
        }

        if (!categoria.getUsuarioId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse("No tienes permiso para eliminar esta categoría"));
        }

        try {
            categoriaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse(
                            "No se puede eliminar esta categoría porque tiene movimientos asociados. " +
                            "Por favor, reasigna los movimientos a otra categoría primero."
                    ));
        }
    }

    private CategoriaResponse toResponse(CategoriaDTO dto) {
        return new CategoriaResponse(
                dto.id(),
                dto.usuarioId(),
                dto.nombre(),
                dto.descripcion(),
                dto.colorHex(),
                dto.icono(),
                dto.tipoCategoria(),
                dto.activa(),
                dto.createdAt(),
                dto.updatedAt()
        );
    }

    record ErrorResponse(String message) {}
}

