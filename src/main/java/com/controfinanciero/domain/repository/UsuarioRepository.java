package com.controfinanciero.domain.repository;

import com.controfinanciero.domain.model.Usuario;

import java.util.List;
import java.util.Optional;

/**
 * Port: Repositorio de Usuario.
 * Define las operaciones de persistencia para la entidad Usuario.
 * La implementación estará en la capa de infraestructura.
 */
public interface UsuarioRepository {

    /**
     * Guarda un nuevo usuario o actualiza uno existente.
     */
    Usuario save(Usuario usuario);

    /**
     * Busca un usuario por su ID.
     */
    Optional<Usuario> findById(Long id);

    /**
     * Busca un usuario por su username.
     */
    Optional<Usuario> findByUsername(String username);

    /**
     * Busca un usuario por su email.
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Lista todos los usuarios activos.
     */
    List<Usuario> findAllActive();

    /**
     * Lista todos los usuarios activos (alias para compatibilidad con JPA).
     */
    default List<Usuario> findByActiveTrue() {
        return findAllActive();
    }

    /**
     * Lista todos los usuarios (activos e inactivos).
     */
    List<Usuario> findAll();

    /**
     * Verifica si existe un usuario con ese username.
     */
    boolean existsByUsername(String username);

    /**
     * Verifica si existe un usuario con ese email.
     */
    boolean existsByEmail(String email);

    /**
     * Elimina un usuario por su ID.
     */
    void deleteById(Long id);

    /**
     * Cuenta la cantidad total de usuarios.
     */
    long count();
}

