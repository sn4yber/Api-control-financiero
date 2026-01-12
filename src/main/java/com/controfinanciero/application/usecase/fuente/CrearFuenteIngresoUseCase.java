package com.controfinanciero.application.usecase.fuente;

import com.controfinanciero.application.dto.CrearFuenteIngresoCommand;
import com.controfinanciero.application.dto.FuenteIngresoDTO;
import com.controfinanciero.domain.exception.UsuarioNoEncontradoException;
import com.controfinanciero.domain.model.FuenteIngreso;
import com.controfinanciero.domain.model.Usuario;
import com.controfinanciero.domain.repository.FuenteIngresoRepository;
import com.controfinanciero.domain.repository.UsuarioRepository;

/**
 * Caso de uso: Crear una nueva fuente de ingreso
 */
public class CrearFuenteIngresoUseCase {

    private final FuenteIngresoRepository fuenteIngresoRepository;
    private final UsuarioRepository usuarioRepository;

    public CrearFuenteIngresoUseCase(
            FuenteIngresoRepository fuenteIngresoRepository,
            UsuarioRepository usuarioRepository) {
        this.fuenteIngresoRepository = fuenteIngresoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public FuenteIngresoDTO ejecutar(CrearFuenteIngresoCommand command) {
        // Validar que el usuario existe
        Usuario usuario = usuarioRepository.findById(command.usuarioId())
                .orElseThrow(() -> new UsuarioNoEncontradoException(command.usuarioId()));

        // Crear la fuente de ingreso usando el constructor
        FuenteIngreso fuente = new FuenteIngreso(
                command.usuarioId(),
                command.nombre(),
                command.tipoFuente()
        );

        // Setear valores opcionales
        if (command.descripcion() != null) {
            fuente.actualizarInformacion(command.nombre(), command.descripcion());
        }

        // Si se especifica que debe estar inactiva, desactivarla
        if (command.activa() != null && !command.activa()) {
            fuente.desactivar();
        }

        // Guardar
        FuenteIngreso guardada = fuenteIngresoRepository.save(fuente);

        // Retornar DTO
        return toDTO(guardada);
    }

    private FuenteIngresoDTO toDTO(FuenteIngreso fuente) {
        return new FuenteIngresoDTO(
                fuente.getId(),
                fuente.getUsuarioId(),
                fuente.getNombre(),
                fuente.getDescripcion(),
                fuente.getTipoFuente(),
                fuente.isEsIngresoReal(),
                fuente.isActiva(),
                fuente.getCreatedAt(),
                fuente.getUpdatedAt()
        );
    }
}

