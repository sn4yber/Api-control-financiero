package com.controfinanciero.application.usecase.categoria;

import com.controfinanciero.application.dto.CategoriaDTO;
import com.controfinanciero.application.dto.CrearCategoriaCommand;
import com.controfinanciero.domain.exception.UsuarioNoEncontradoException;
import com.controfinanciero.domain.model.Categoria;
import com.controfinanciero.domain.repository.CategoriaRepository;
import com.controfinanciero.domain.repository.UsuarioRepository;

/**
 * Caso de uso: Crear Categoría.
 * Crea una nueva categoría para un usuario.
 */
public class CrearCategoriaUseCase {

    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;

    public CrearCategoriaUseCase(
            CategoriaRepository categoriaRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.categoriaRepository = categoriaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public CategoriaDTO ejecutar(CrearCategoriaCommand command) {
        // Validar que el usuario existe
        usuarioRepository.findById(command.usuarioId())
                .orElseThrow(() -> new UsuarioNoEncontradoException(command.usuarioId()));

        // Validar que no exista una categoría con el mismo nombre
        if (categoriaRepository.existsByUsuarioIdAndNombre(command.usuarioId(), command.nombre())) {
            throw new IllegalArgumentException("Ya existe una categoría con el nombre: " + command.nombre());
        }

        // Crear la categoría
        Categoria categoria = new Categoria(
                command.usuarioId(),
                command.nombre(),
                command.tipoCategoria()
        );

        // Configurar información adicional
        if (command.descripcion() != null) {
            categoria.actualizarInformacion(command.nombre(), command.descripcion());
        }

        // Configurar apariencia
        if (command.colorHex() != null || command.icono() != null) {
            categoria.cambiarApariencia(command.colorHex(), command.icono());
        }

        // Guardar
        Categoria categoriaGuardada = categoriaRepository.save(categoria);

        // Convertir a DTO
        return mapearADTO(categoriaGuardada);
    }

    private CategoriaDTO mapearADTO(Categoria categoria) {
        return new CategoriaDTO(
                categoria.getId(),
                categoria.getUsuarioId(),
                categoria.getNombre(),
                categoria.getDescripcion(),
                categoria.getColorHex(),
                categoria.getIcono(),
                categoria.getTipoCategoria(),
                categoria.estaActiva(),
                categoria.getCreatedAt(),
                categoria.getUpdatedAt()
        );
    }
}

