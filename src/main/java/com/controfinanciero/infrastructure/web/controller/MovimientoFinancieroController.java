package com.controfinanciero.infrastructure.web.controller;

import com.controfinanciero.application.dto.CrearMovimientoFinancieroCommand;
import com.controfinanciero.application.dto.MovimientoFinancieroDTO;
import com.controfinanciero.application.usecase.movimiento.CrearMovimientoFinancieroUseCase;
import com.controfinanciero.application.usecase.movimiento.ObtenerMovimientosFinancierosUseCase;
import com.controfinanciero.domain.model.enums.TipoMovimiento;
import com.controfinanciero.domain.repository.MovimientoFinancieroRepository;
import com.controfinanciero.infrastructure.security.AuthenticatedUserService;
import com.controfinanciero.infrastructure.web.dto.request.CrearMovimientoFinancieroRequest;
import com.controfinanciero.infrastructure.web.dto.response.MovimientoFinancieroResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller para gestionar movimientos financieros
 * ✅ MEJORAS IMPLEMENTADAS:
 * - Sin userId en URLs (usa token JWT)
 * - Mejor manejo de errores en DELETE (409 en lugar de 500)
 * - Validación de ownership
 */
@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
public class MovimientoFinancieroController {

    private final CrearMovimientoFinancieroUseCase crearMovimientoUseCase;
    private final ObtenerMovimientosFinancierosUseCase obtenerMovimientosUseCase;
    private final MovimientoFinancieroRepository movimientoRepository;
    private final AuthenticatedUserService authenticatedUserService;

    /**
     * Crear movimiento para el usuario autenticado
     */
    @PostMapping
    public ResponseEntity<MovimientoFinancieroResponse> crearMovimiento(
            @Valid @RequestBody CrearMovimientoFinancieroRequest request) {

        Long userId = authenticatedUserService.getCurrentUserId();

        CrearMovimientoFinancieroCommand command = new CrearMovimientoFinancieroCommand(
                userId, // ✅ Usa el usuario autenticado
                request.tipoMovimiento(),
                request.monto(),
                request.descripcion(),
                request.fechaMovimiento(),
                request.categoriaId(),
                request.fuenteIngresoId(),
                request.metaId(),
                request.esRecurrente(),
                request.patronRecurrencia(),
                request.notas()
        );

        MovimientoFinancieroDTO dto = crearMovimientoUseCase.ejecutar(command);
        MovimientoFinancieroResponse response = toResponse(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * ✅ NUEVO: Obtener movimientos con paginación
     * GET /api/movimientos/paginated?page=0&size=20&sort=movementDate,desc
     */
    @GetMapping("/paginated")
    public ResponseEntity<Page<MovimientoFinancieroResponse>> obtenerMovimientosPaginados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "movementDate,desc") String[] sort,
            @RequestParam(required = false) TipoMovimiento tipo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(required = false) Long categoriaId) {

        Long userId = authenticatedUserService.getCurrentUserId();

        // Construir Sort
        Sort.Direction direction = sort.length > 1 && sort[1].equalsIgnoreCase("asc")
            ? Sort.Direction.ASC
            : Sort.Direction.DESC;
        String property = sort[0];
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, property));

        // Obtener repository para usar métodos paginados
        var jpaRepo = (com.controfinanciero.infrastructure.persistence.repository.MovimientoFinancieroJpaRepository)
                      movimientoRepository;

        Page<com.controfinanciero.infrastructure.persistence.entity.MovimientoFinancieroEntity> entityPage;

        // Aplicar filtros
        if (fechaInicio != null && fechaFin != null) {
            entityPage = jpaRepo.findByUserIdAndDateRange(userId, fechaInicio, fechaFin, pageable);
        } else if (tipo != null) {
            entityPage = jpaRepo.findByUserIdAndMovementType(userId, tipo, pageable);
        } else if (categoriaId != null) {
            entityPage = jpaRepo.findByUserIdAndCategoryId(userId, categoriaId, pageable);
        } else {
            entityPage = jpaRepo.findByUserId(userId, pageable);
        }

        // Convertir a DTOs y luego a Responses
        Page<MovimientoFinancieroResponse> responsePage = entityPage.map(entity -> {
            // Convertir entity a DTO manualmente (simplificado)
            MovimientoFinancieroDTO dto = new MovimientoFinancieroDTO(
                entity.getId(),
                entity.getUserId(),
                entity.getMovementType(),
                entity.getAmount(),
                entity.getDescription(),
                entity.getMovementDate(),
                entity.getCategoryId(),
                null, // categoriaNombre
                entity.getIncomeSourceId(),
                null, // fuenteIngresoNombre
                entity.getGoalId(),
                null, // metaNombre
                entity.getIsRecurring(),
                entity.getRecurrencePattern(),
                entity.getNotes(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
            );
            return toResponse(dto);
        });

        return ResponseEntity.ok(responsePage);
    }

    /**
     * Obtener movimientos del usuario autenticado (sin userId en URL)
     */
    @GetMapping
    public ResponseEntity<List<MovimientoFinancieroResponse>> obtenerMisMovimientos(
            @RequestParam(required = false) TipoMovimiento tipo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(required = false) Long categoriaId) {

        Long userId = authenticatedUserService.getCurrentUserId();

        List<MovimientoFinancieroDTO> dtos;

        if (fechaInicio != null && fechaFin != null) {
            dtos = obtenerMovimientosUseCase.ejecutarPorRangoFechas(userId, fechaInicio, fechaFin);
        } else if (tipo != null) {
            dtos = obtenerMovimientosUseCase.ejecutarPorTipo(userId, tipo);
        } else if (categoriaId != null) {
            dtos = obtenerMovimientosUseCase.ejecutarPorCategoria(userId, categoriaId);
        } else {
            dtos = obtenerMovimientosUseCase.ejecutar(userId);
        }

        List<MovimientoFinancieroResponse> responses = dtos.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    /**
     * Obtener movimiento por ID (con validación de ownership)
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerMovimientoPorId(@PathVariable Long id) {
        Long userId = authenticatedUserService.getCurrentUserId();

        var movimiento = movimientoRepository.findById(id)
                .orElse(null);

        if (movimiento == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Movimiento no encontrado"));
        }

        if (!movimiento.getUsuarioId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse("No tienes permiso para ver este movimiento"));
        }

        // Convertir a DTO (usando el caso de uso existente sería mejor)
        var dtos = obtenerMovimientosUseCase.ejecutar(userId);
        var dto = dtos.stream()
                .filter(d -> d.id().equals(id))
                .findFirst()
                .orElse(null);

        if (dto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Movimiento no encontrado"));
        }

        return ResponseEntity.ok(toResponse(dto));
    }

    /**
     * Eliminar movimiento con mejor manejo de errores (409 en lugar de 500)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarMovimiento(@PathVariable Long id) {
        Long userId = authenticatedUserService.getCurrentUserId();

        var movimiento = movimientoRepository.findById(id)
                .orElse(null);

        if (movimiento == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Movimiento no encontrado"));
        }

        // ✅ Validar ownership
        if (!movimiento.getUsuarioId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse("No tienes permiso para eliminar este movimiento"));
        }

        try {
            // ✅ Verificar si está vinculado a una meta activa
            if (movimiento.getMetaId() != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ErrorResponse(
                                "No se puede eliminar este movimiento porque está vinculado a una meta activa. " +
                                "Por favor, desvincúlalo primero."
                        ));
            }

            movimientoRepository.deleteById(id);
            return ResponseEntity.noContent().build();

        } catch (DataIntegrityViolationException e) {
            // ✅ Error 409 en lugar de 500
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse(
                            "No se puede eliminar este movimiento porque está vinculado a otros registros. " +
                            "Por favor, elimina las referencias primero."
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al eliminar el movimiento: " + e.getMessage()));
        }
    }

    private MovimientoFinancieroResponse toResponse(MovimientoFinancieroDTO dto) {
        return new MovimientoFinancieroResponse(
                dto.id(),
                dto.usuarioId(),
                dto.tipoMovimiento(),
                dto.monto(),
                dto.descripcion(),
                dto.fechaMovimiento(),
                dto.categoriaId(),
                dto.categoriaNombre(),
                dto.fuenteIngresoId(),
                dto.fuenteIngresoNombre(),
                dto.metaId(),
                dto.metaNombre(),
                dto.esRecurrente(),
                dto.patronRecurrencia(),
                dto.notas(),
                dto.createdAt(),
                dto.updatedAt()
        );
    }

    record ErrorResponse(String message) {}
}

