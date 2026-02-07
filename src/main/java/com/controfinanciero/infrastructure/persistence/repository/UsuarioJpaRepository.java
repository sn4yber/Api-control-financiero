package com.controfinanciero.infrastructure.persistence.repository;

import com.controfinanciero.infrastructure.persistence.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * JPA Repository para Usuario.
 * Spring Data genera automáticamente la implementación.
 */
@Repository
public interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity, Long> {

    /**
     * Busca un usuario por email.
     */
    Optional<UsuarioEntity> findByEmail(String email);

    /**
     * Busca un usuario por username.
     */
    Optional<UsuarioEntity> findByUsername(String username);

    /**
     * Verifica si existe un usuario con ese email.
     */
    boolean existsByEmail(String email);

    /**
     * Verifica si existe un usuario con ese username.
     */
    boolean existsByUsername(String username);

    /**
     * Lista todos los usuarios activos.
     * Spring Data JPA genera automáticamente la query basada en el nombre del método.
     */
    List<UsuarioEntity> findByActiveTrue();
}

