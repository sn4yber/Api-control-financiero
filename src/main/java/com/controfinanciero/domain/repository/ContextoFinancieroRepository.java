package com.controfinanciero.domain.repository;

import com.controfinanciero.domain.model.ContextoFinanciero;

import java.util.Optional;

/**
 * Port: Repositorio de Contexto Financiero.
 * Define las operaciones de persistencia para la entidad ContextoFinanciero.
 */
public interface ContextoFinancieroRepository {

    /**
     * Guarda un nuevo contexto financiero o actualiza uno existente.
     */
    ContextoFinanciero save(ContextoFinanciero contexto);

    /**
     * Busca un contexto financiero por su ID.
     */
    Optional<ContextoFinanciero> findById(Long id);

    /**
     * Busca un contexto financiero por el ID del usuario.
     */
    Optional<ContextoFinanciero> findByUsuarioId(Long usuarioId);

    /**
     * Verifica si existe un contexto para el usuario.
     */
    boolean existsByUsuarioId(Long usuarioId);

    /**
     * Elimina un contexto financiero por su ID.
     */
    void deleteById(Long id);
}

