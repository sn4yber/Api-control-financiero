package com.controfinanciero.infrastructure.persistence.adapter;

import com.controfinanciero.domain.model.MetaFinanciera;
import com.controfinanciero.domain.model.enums.EstadoMeta;
import com.controfinanciero.domain.model.enums.Prioridad;
import com.controfinanciero.domain.repository.MetaFinancieraRepository;
import com.controfinanciero.infrastructure.persistence.entity.MetaFinancieraEntity;
import com.controfinanciero.infrastructure.persistence.mapper.MetaFinancieraEntityMapper;
import com.controfinanciero.infrastructure.persistence.repository.MetaFinancieraJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador que implementa MetaFinancieraRepository.
 * Conecta el dominio con la infraestructura de persistencia.
 */
@Component
public class MetaFinancieraRepositoryAdapter implements MetaFinancieraRepository {

    private final MetaFinancieraJpaRepository jpaRepository;

    public MetaFinancieraRepositoryAdapter(MetaFinancieraJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public MetaFinanciera save(MetaFinanciera meta) {
        MetaFinancieraEntity entity;

        if (meta.getId() != null) {
            // Actualizar existente
            entity = jpaRepository.findById(meta.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Meta financiera no encontrada"));
            MetaFinancieraEntityMapper.updateEntity(meta, entity);
        } else {
            // Crear nueva
            entity = MetaFinancieraEntityMapper.toEntity(meta);
        }

        MetaFinancieraEntity savedEntity = jpaRepository.save(entity);
        return MetaFinancieraEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<MetaFinanciera> findById(Long id) {
        return jpaRepository.findById(id)
                .map(MetaFinancieraEntityMapper::toDomain);
    }

    @Override
    public List<MetaFinanciera> findByUsuarioId(Long usuarioId) {
        // ✅ Ahora incluye metas propias Y metas compartidas donde es colaborador activo
        return jpaRepository.findAllMetasIncludingShared(usuarioId).stream()
                .map(MetaFinancieraEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<MetaFinanciera> findByUsuarioIdAndEstado(Long usuarioId, EstadoMeta estado) {
        return jpaRepository.findByUserIdAndStatus(usuarioId, estado).stream()
                .map(MetaFinancieraEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<MetaFinanciera> findActivasByUsuarioId(Long usuarioId) {
        return jpaRepository.findActiveByUserId(usuarioId).stream()
                .map(MetaFinancieraEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<MetaFinanciera> findCompletadasByUsuarioId(Long usuarioId) {
        return jpaRepository.findCompletedByUserId(usuarioId).stream()
                .map(MetaFinancieraEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<MetaFinanciera> findByUsuarioIdAndPrioridad(Long usuarioId, Prioridad prioridad) {
        // Este método necesita ser implementado en el JpaRepository
        // Por ahora filtramos en memoria
        return findByUsuarioId(usuarioId).stream()
                .filter(meta -> meta.getPrioridad() == prioridad)
                .collect(Collectors.toList());
    }

    @Override
    public List<MetaFinanciera> findByUsuarioIdOrderByPrioridadDesc(Long usuarioId) {
        // Este método necesita ser implementado en el JpaRepository
        // Por ahora ordenamos en memoria
        return findByUsuarioId(usuarioId).stream()
                .sorted((m1, m2) -> m2.getPrioridad().compareTo(m1.getPrioridad()))
                .collect(Collectors.toList());
    }

    @Override
    public List<MetaFinanciera> findByUsuarioIdOrderByFechaObjetivo(Long usuarioId) {
        // Este método necesita ser implementado en el JpaRepository
        // Por ahora ordenamos en memoria
        return findByUsuarioId(usuarioId).stream()
                .filter(meta -> meta.getFechaObjetivo() != null)
                .sorted((m1, m2) -> m1.getFechaObjetivo().compareTo(m2.getFechaObjetivo()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public long countActivasByUsuarioId(Long usuarioId) {
        return jpaRepository.findActiveByUserId(usuarioId).size();
    }

    @Override
    public long countCompletadasByUsuarioId(Long usuarioId) {
        return jpaRepository.findCompletedByUserId(usuarioId).size();
    }
}

