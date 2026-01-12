package com.controfinanciero.infrastructure.persistence.repository;

import com.controfinanciero.infrastructure.persistence.entity.ContextoFinancieroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para ContextoFinancieroEntity.
 */
@Repository
public interface ContextoFinancieroJpaRepository extends JpaRepository<ContextoFinancieroEntity, Long> {

    /**
     * Busca un contexto financiero por el ID del usuario.
     */
    Optional<ContextoFinancieroEntity> findByUserId(Long userId);

    /**
     * Verifica si existe un contexto para el usuario.
     */
    boolean existsByUserId(Long userId);
}

