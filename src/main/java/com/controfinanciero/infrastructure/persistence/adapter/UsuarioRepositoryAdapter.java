package com.controfinanciero.infrastructure.persistence.adapter;

import com.controfinanciero.domain.model.Usuario;
import com.controfinanciero.domain.repository.UsuarioRepository;
import com.controfinanciero.infrastructure.persistence.entity.UsuarioEntity;
import com.controfinanciero.infrastructure.persistence.mapper.UsuarioEntityMapper;
import com.controfinanciero.infrastructure.persistence.repository.UsuarioJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adapter que implementa el port UsuarioRepository.
 * Conecta el dominio con la infraestructura de persistencia JPA.
 */
@Component
public class UsuarioRepositoryAdapter implements UsuarioRepository {

    private final UsuarioJpaRepository jpaRepository;

    public UsuarioRepositoryAdapter(UsuarioJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Usuario save(Usuario usuario) {
        UsuarioEntity entity = UsuarioEntityMapper.toEntity(usuario);
        UsuarioEntity savedEntity = jpaRepository.save(entity);
        return UsuarioEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return jpaRepository.findById(id)
                .map(UsuarioEntityMapper::toDomain);
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(UsuarioEntityMapper::toDomain);
    }

    @Override
    public Optional<Usuario> findByUsername(String username) {
        return jpaRepository.findByUsername(username)
                .map(UsuarioEntityMapper::toDomain);
    }

    @Override
    public List<Usuario> findAllActive() {
        return jpaRepository.findAll().stream()
                .map(UsuarioEntityMapper::toDomain)
                .filter(Usuario::estaActivo)
                .collect(Collectors.toList());
    }

    @Override
    public List<Usuario> findAll() {
        return jpaRepository.findAll().stream()
                .map(UsuarioEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByUsername(String username) {
        return jpaRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }
}
