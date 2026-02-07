package com.controfinanciero.infrastructure.persistence.adapter;

import com.controfinanciero.domain.model.Categoria;
import com.controfinanciero.domain.model.enums.TipoCategoria;
import com.controfinanciero.domain.repository.CategoriaRepository;
import com.controfinanciero.infrastructure.persistence.entity.CategoriaEntity;
import com.controfinanciero.infrastructure.persistence.mapper.CategoriaEntityMapper;
import com.controfinanciero.infrastructure.persistence.repository.CategoriaJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador que implementa CategoriaRepository.
 * Conecta el dominio con la infraestructura de persistencia.
 */
@Component
public class CategoriaRepositoryAdapter implements CategoriaRepository {

    private final CategoriaJpaRepository jpaRepository;

    public CategoriaRepositoryAdapter(CategoriaJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Categoria save(Categoria categoria) {
        CategoriaEntity entity;

        if (categoria.getId() != null) {
            // Actualizar existente
            entity = jpaRepository.findById(categoria.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));
            CategoriaEntityMapper.updateEntity(categoria, entity);
        } else {
            // Crear nueva
            entity = CategoriaEntityMapper.toEntity(categoria);
        }

        CategoriaEntity savedEntity = jpaRepository.save(entity);
        return CategoriaEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Categoria> findById(Long id) {
        return jpaRepository.findById(id)
                .map(CategoriaEntityMapper::toDomain);
    }

    @Override
    public List<Categoria> findByUsuarioId(Long usuarioId) {
        return jpaRepository.findByUserId(usuarioId).stream()
                .map(CategoriaEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Categoria> findActivasByUsuarioId(Long usuarioId) {
        return jpaRepository.findByUserIdAndIsActive(usuarioId, true).stream()
                .map(CategoriaEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Categoria> findByUsuarioIdAndTipo(Long usuarioId, TipoCategoria tipo) {
        return jpaRepository.findByUserIdAndCategoryType(usuarioId, tipo).stream()
                .map(CategoriaEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Categoria> findByUsuarioIdAndNombre(Long usuarioId, String nombre) {
        return jpaRepository.findByUserIdAndName(usuarioId, nombre)
                .map(CategoriaEntityMapper::toDomain);
    }

    @Override
    public boolean existsByUsuarioIdAndNombre(Long usuarioId, String nombre) {
        return jpaRepository.existsByUserIdAndName(usuarioId, nombre);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public long countActivasByUsuarioId(Long usuarioId) {
        return jpaRepository.countByUserIdAndIsActiveTrue(usuarioId);
    }
}

