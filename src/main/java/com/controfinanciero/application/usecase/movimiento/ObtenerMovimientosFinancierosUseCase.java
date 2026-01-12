package com.controfinanciero.application.usecase.movimiento;

import com.controfinanciero.application.dto.MovimientoFinancieroDTO;
import com.controfinanciero.domain.model.*;
import com.controfinanciero.domain.model.enums.TipoMovimiento;
import com.controfinanciero.domain.repository.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Caso de uso: Obtener movimientos financieros
 */
public class ObtenerMovimientosFinancierosUseCase {

    private final MovimientoFinancieroRepository movimientoRepository;
    private final CategoriaRepository categoriaRepository;
    private final FuenteIngresoRepository fuenteIngresoRepository;
    private final MetaFinancieraRepository metaRepository;

    public ObtenerMovimientosFinancierosUseCase(
            MovimientoFinancieroRepository movimientoRepository,
            CategoriaRepository categoriaRepository,
            FuenteIngresoRepository fuenteIngresoRepository,
            MetaFinancieraRepository metaRepository) {
        this.movimientoRepository = movimientoRepository;
        this.categoriaRepository = categoriaRepository;
        this.fuenteIngresoRepository = fuenteIngresoRepository;
        this.metaRepository = metaRepository;
    }

    public List<MovimientoFinancieroDTO> ejecutar(Long usuarioId) {
        List<MovimientoFinanciero> movimientos = movimientoRepository.findByUsuarioId(usuarioId);
        return movimientos.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<MovimientoFinancieroDTO> ejecutarPorTipo(Long usuarioId, TipoMovimiento tipo) {
        List<MovimientoFinanciero> movimientos = movimientoRepository.findByUsuarioIdAndTipo(usuarioId, tipo);
        return movimientos.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<MovimientoFinancieroDTO> ejecutarPorRangoFechas(Long usuarioId, LocalDate fechaInicio, LocalDate fechaFin) {
        List<MovimientoFinanciero> movimientos = movimientoRepository.findByUsuarioIdAndFechaBetween(usuarioId, fechaInicio, fechaFin);
        return movimientos.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<MovimientoFinancieroDTO> ejecutarPorCategoria(Long usuarioId, Long categoriaId) {
        List<MovimientoFinanciero> movimientos = movimientoRepository.findByUsuarioIdAndCategoriaId(usuarioId, categoriaId);
        return movimientos.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private MovimientoFinancieroDTO toDTO(MovimientoFinanciero movimiento) {
        String categoriaNombre = null;
        if (movimiento.getCategoriaId() != null) {
            categoriaNombre = categoriaRepository.findById(movimiento.getCategoriaId())
                    .map(Categoria::getNombre)
                    .orElse(null);
        }

        String fuenteNombre = null;
        if (movimiento.getFuenteIngresoId() != null) {
            fuenteNombre = fuenteIngresoRepository.findById(movimiento.getFuenteIngresoId())
                    .map(FuenteIngreso::getNombre)
                    .orElse(null);
        }

        String metaNombre = null;
        if (movimiento.getMetaId() != null) {
            metaNombre = metaRepository.findById(movimiento.getMetaId())
                    .map(MetaFinanciera::getNombre)
                    .orElse(null);
        }

        return new MovimientoFinancieroDTO(
                movimiento.getId(),
                movimiento.getUsuarioId(),
                movimiento.getTipoMovimiento(),
                movimiento.getMonto(),
                movimiento.getDescripcion(),
                movimiento.getFechaMovimiento(),
                movimiento.getCategoriaId(),
                categoriaNombre,
                movimiento.getFuenteIngresoId(),
                fuenteNombre,
                movimiento.getMetaId(),
                metaNombre,
                movimiento.isEsRecurrente(),
                movimiento.getPatronRecurrencia(),
                movimiento.getNotas(),
                movimiento.getCreatedAt(),
                movimiento.getUpdatedAt()
        );
    }
}

