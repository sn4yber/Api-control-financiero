package com.controfinanciero.application.usecase.categoria;

import com.controfinanciero.application.dto.CategoriaDTO;
import com.controfinanciero.domain.model.Categoria;
import com.controfinanciero.domain.repository.CategoriaRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Caso de uso: Obtener Categorías.
 * Obtiene categorías de diferentes formas.
 */
public class ObtenerCategoriasUseCase {

    private final CategoriaRepository categoriaRepository;

    public ObtenerCategoriasUseCase(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public CategoriaDTO ejecutarPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe categoría con ID " + id));

        return mapearADTO(categoria);
    }

    public List<CategoriaDTO> ejecutarPorUsuario(Long usuarioId) {
        List<Categoria> categorias = categoriaRepository.findByUsuarioId(usuarioId);
        return categorias.stream()
                .map(this::mapearADTO)
                .collect(Collectors.toList());
    }

    public List<CategoriaDTO> ejecutarActivasPorUsuario(Long usuarioId) {
        List<Categoria> categorias = categoriaRepository.findActivasByUsuarioId(usuarioId);
        return categorias.stream()
                .map(this::mapearADTO)
                .collect(Collectors.toList());
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

