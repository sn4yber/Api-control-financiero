package com.controfinanciero.domain.repository;

import com.controfinanciero.domain.model.Categoria;
import com.controfinanciero.domain.model.enums.TipoCategoria;

import java.util.List;
import java.util.Optional;

/**
 * Port: Repositorio de Categoría.
 * Define las operaciones de persistencia para categorías.
 * La implementación estará en la capa de infraestructura.
 */
public interface CategoriaRepository {

    /**
     * Guarda una nueva categoría o actualiza una existente.
     */
    Categoria save(Categoria categoria);

    /**
     * Busca una categoría por su ID.
     */
    Optional<Categoria> findById(Long id);

    /**
     * Lista todas las categorías de un usuario.
     */
    List<Categoria> findByUsuarioId(Long usuarioId);

    /**
     * Lista categorías activas de un usuario.
     */
    List<Categoria> findActivasByUsuarioId(Long usuarioId);

    /**
     * Lista categorías de un usuario por tipo.
     */
    List<Categoria> findByUsuarioIdAndTipo(Long usuarioId, TipoCategoria tipo);

    /**
     * Busca una categoría por nombre de usuario.
     */
    Optional<Categoria> findByUsuarioIdAndNombre(Long usuarioId, String nombre);

    /**
     * Verifica si existe una categoría con ese nombre para un usuario.
     */
    boolean existsByUsuarioIdAndNombre(Long usuarioId, String nombre);

    /**
     * Elimina una categoría por su ID.
     */
    void deleteById(Long id);

    /**
     * Cuenta categorías activas de un usuario.
     */
    long countActivasByUsuarioId(Long usuarioId);
}
