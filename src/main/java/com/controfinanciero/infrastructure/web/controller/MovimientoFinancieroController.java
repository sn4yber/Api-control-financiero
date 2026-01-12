package com.controfinanciero.infrastructure.web.controller;

import com.controfinanciero.application.dto.CrearMovimientoFinancieroCommand;
import com.controfinanciero.application.dto.MovimientoFinancieroDTO;
import com.controfinanciero.application.usecase.movimiento.CrearMovimientoFinancieroUseCase;
import com.controfinanciero.application.usecase.movimiento.ObtenerMovimientosFinancierosUseCase;
import com.controfinanciero.domain.model.enums.TipoMovimiento;
import com.controfinanciero.infrastructure.web.dto.request.CrearMovimientoFinancieroRequest;
import com.controfinanciero.infrastructure.web.dto.response.MovimientoFinancieroResponse;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller para gestionar movimientos financieros
 */
@RestController
@RequestMapping("/api/movimientos")
public class MovimientoFinancieroController {

    private final CrearMovimientoFinancieroUseCase crearMovimientoUseCase;
    private final ObtenerMovimientosFinancierosUseCase obtenerMovimientosUseCase;

    public MovimientoFinancieroController(
            CrearMovimientoFinancieroUseCase crearMovimientoUseCase,
            ObtenerMovimientosFinancierosUseCase obtenerMovimientosUseCase) {
        this.crearMovimientoUseCase = crearMovimientoUseCase;
        this.obtenerMovimientosUseCase = obtenerMovimientosUseCase;
    }

    @PostMapping
    public ResponseEntity<MovimientoFinancieroResponse> crearMovimiento(
            @Valid @RequestBody CrearMovimientoFinancieroRequest request) {

        CrearMovimientoFinancieroCommand command = new CrearMovimientoFinancieroCommand(
                request.usuarioId(),
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

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<MovimientoFinancieroResponse>> obtenerMovimientosPorUsuario(
            @PathVariable Long usuarioId,
            @RequestParam(required = false) TipoMovimiento tipo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(required = false) Long categoriaId) {

        List<MovimientoFinancieroDTO> dtos;

        if (fechaInicio != null && fechaFin != null) {
            dtos = obtenerMovimientosUseCase.ejecutarPorRangoFechas(usuarioId, fechaInicio, fechaFin);
        } else if (tipo != null) {
            dtos = obtenerMovimientosUseCase.ejecutarPorTipo(usuarioId, tipo);
        } else if (categoriaId != null) {
            dtos = obtenerMovimientosUseCase.ejecutarPorCategoria(usuarioId, categoriaId);
        } else {
            dtos = obtenerMovimientosUseCase.ejecutar(usuarioId);
        }

        List<MovimientoFinancieroResponse> responses = dtos.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
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
}

