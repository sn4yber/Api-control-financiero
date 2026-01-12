package com.controfinanciero.infrastructure.persistence.adapter;

import com.controfinanciero.domain.model.ContextoFinanciero;
import com.controfinanciero.domain.repository.ContextoFinancieroRepository;
import com.controfinanciero.infrastructure.persistence.entity.ContextoFinancieroEntity;
import com.controfinanciero.infrastructure.persistence.mapper.ContextoFinancieroEntityMapper;
import com.controfinanciero.infrastructure.persistence.repository.ContextoFinancieroJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Adaptador que implementa ContextoFinancieroRepository.
 * Conecta el dominio con la infraestructura de persistencia.
 */
@Component
public class ContextoFinancieroRepositoryAdapter implements ContextoFinancieroRepository {

    private final ContextoFinancieroJpaRepository jpaRepository;

    public ContextoFinancieroRepositoryAdapter(ContextoFinancieroJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public ContextoFinanciero save(ContextoFinanciero contexto) {
        ContextoFinancieroEntity entity;

        if (contexto.getId() != null) {
            // Actualizar existente
            entity = jpaRepository.findById(contexto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Contexto no encontrado"));
            ContextoFinancieroEntityMapper.updateEntity(contexto, entity);
        } else {
            // Crear nuevo
            entity = ContextoFinancieroEntityMapper.toEntity(contexto);
        }

        ContextoFinancieroEntity savedEntity = jpaRepository.save(entity);
        return ContextoFinancieroEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<ContextoFinanciero> findById(Long id) {
        return jpaRepository.findById(id)
                .map(ContextoFinancieroEntityMapper::toDomain);
    }

    @Override
    public Optional<ContextoFinanciero> findByUsuarioId(Long usuarioId) {
        return jpaRepository.findByUserId(usuarioId)
                .map(ContextoFinancieroEntityMapper::toDomain);
    }

    @Override
    public boolean existsByUsuarioId(Long usuarioId) {
        return jpaRepository.existsByUserId(usuarioId);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}

