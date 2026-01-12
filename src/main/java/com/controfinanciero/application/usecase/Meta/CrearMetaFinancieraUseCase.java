package com.controfinanciero.application.usecase.Meta;
import com.controfinanciero.application.dto.CrearMetaFinancieraCommand;
import com.controfinanciero.application.dto.MetaFinancieraDTO;
import com.controfinanciero.domain.exception.UsuarioNoEncontradoException;
import com.controfinanciero.domain.model.MetaFinanciera;
import com.controfinanciero.domain.model.Usuario;
import com.controfinanciero.domain.repository.MetaFinancieraRepository;
import com.controfinanciero.domain.repository.UsuarioRepository;

/**
 * Caso de uso: Crear una nueva meta financiera
 */
public class CrearMetaFinancieraUseCase {
    private  final MetaFinancieraRepository metaRepository;
    private final UsuarioRepository usuarioRepository;

    public CrearMetaFinancieraUseCase(MetaFinancieraRepository metaRepository, UsuarioRepository usuarioRepository) {
        this.metaRepository = metaRepository;
        this.usuarioRepository = usuarioRepository;
    }
    public MetaFinancieraDTO ejecutar(CrearMetaFinancieraCommand command){
        // Validar que el usuario existe
        Usuario usuario = usuarioRepository.findById(command.usuarioId())
                .orElseThrow(() -> new UsuarioNoEncontradoException(command.usuarioId()));

        // Crear la meta usando el constructor
        MetaFinanciera meta = new MetaFinanciera(
                command.usuarioId(),
                command.nombre(),
                command.montoObjetivo(),
                command.fechaObjetivo(),
                command.prioridad()
        );

        // Setear valores opcionales
        if (command.descripcion() != null) {
            meta.actualizarInformacion(command.nombre(), command.descripcion());
        }

        // Guardar
        MetaFinanciera guardada = metaRepository.save(meta);

        // Retornar DTO
        return toDTO(guardada);
    }
    private MetaFinancieraDTO toDTO(MetaFinanciera meta){
        return new MetaFinancieraDTO(
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
    }
}
