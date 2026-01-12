package com.controfinanciero.application.usecase.movimiento;

import com.controfinanciero.application.dto.CrearMovimientoFinancieroCommand;
import com.controfinanciero.application.dto.MovimientoFinancieroDTO;
import com.controfinanciero.domain.exception.UsuarioNoEncontradoException;
import com.controfinanciero.domain.model.*;
import com.controfinanciero.domain.repository.*;

/**
 * Caso de uso: Crear un nuevo movimiento financiero
 */
public class CrearMovimientoFinancieroUseCase {

    private final MovimientoFinancieroRepository movimientoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final FuenteIngresoRepository fuenteIngresoRepository;
    private final MetaFinancieraRepository metaRepository;

    public CrearMovimientoFinancieroUseCase(
            MovimientoFinancieroRepository movimientoRepository,
            UsuarioRepository usuarioRepository,
            CategoriaRepository categoriaRepository,
            FuenteIngresoRepository fuenteIngresoRepository,
            MetaFinancieraRepository metaRepository) {
        this.movimientoRepository = movimientoRepository;
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
        this.fuenteIngresoRepository = fuenteIngresoRepository;
        this.metaRepository = metaRepository;
    }

    public MovimientoFinancieroDTO ejecutar(CrearMovimientoFinancieroCommand command) {
        // Validar que el usuario existe
        Usuario usuario = usuarioRepository.findById(command.usuarioId())
                .orElseThrow(() -> new UsuarioNoEncontradoException(command.usuarioId()));

        // Crear el movimiento con el constructor correcto
        MovimientoFinanciero movimiento = new MovimientoFinanciero(
                command.usuarioId(),
                command.tipoMovimiento(),
                command.monto(),
                command.descripcion(),
                command.fechaMovimiento()
        );

        // Setear valores opcionales
        if (command.categoriaId() != null) {
            movimiento.asignarCategoria(command.categoriaId());
        }

        if (command.fuenteIngresoId() != null) {
            movimiento.asignarFuenteIngreso(command.fuenteIngresoId());
        }

        if (command.metaId() != null) {
            movimiento.vincularAMeta(command.metaId());
        }

        if (command.esRecurrente() != null && command.esRecurrente() && command.patronRecurrencia() != null) {
            movimiento.configurarRecurrencia(command.patronRecurrencia());
        }

        if (command.notas() != null) {
            movimiento.agregarNotas(command.notas());
        }

        // Guardar
        MovimientoFinanciero guardado = movimientoRepository.save(movimiento);

        // Si está vinculado a una meta y es de tipo SAVINGS, actualizar el progreso
        if (guardado.getMetaId() != null && command.tipoMovimiento().name().equals("SAVINGS")) {
            metaRepository.findById(guardado.getMetaId()).ifPresent(meta -> {
                meta.agregarMonto(guardado.getMonto());
                metaRepository.save(meta);
            });
        }

        // Retornar DTO con información enriquecida
        return toDTO(guardado);
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

