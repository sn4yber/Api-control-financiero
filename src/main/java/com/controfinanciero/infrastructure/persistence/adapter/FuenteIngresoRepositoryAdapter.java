package com.controfinanciero.infrastructure.persistence.adapter;

import com.controfinanciero.domain.model.FuenteIngreso;
import com.controfinanciero.domain.model.enums.TipoFuente;
import com.controfinanciero.domain.repository.FuenteIngresoRepository;
import com.controfinanciero.infrastructure.persistence.entity.FuenteIngresoEntity;
import com.controfinanciero.infrastructure.persistence.mapper.FuenteIngresoEntityMapper;
import com.controfinanciero.infrastructure.persistence.repository.FuenteIngresoJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador que implementa FuenteIngresoRepository.
 * Conecta el dominio con la infraestructura de persistencia.
 */
@Component
public class FuenteIngresoRepositoryAdapter implements FuenteIngresoRepository {

    private final FuenteIngresoJpaRepository jpaRepository;

    public FuenteIngresoRepositoryAdapter(FuenteIngresoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public FuenteIngreso save(FuenteIngreso fuenteIngreso) {
        FuenteIngresoEntity entity;

        if (fuenteIngreso.getId() != null) {
            // Actualizar existente
            entity = jpaRepository.findById(fuenteIngreso.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Fuente de ingreso no encontrada"));
            FuenteIngresoEntityMapper.updateEntity(fuenteIngreso, entity);
        } else {
            // Crear nueva
            entity = FuenteIngresoEntityMapper.toEntity(fuenteIngreso);
        }

        FuenteIngresoEntity savedEntity = jpaRepository.save(entity);
        return FuenteIngresoEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<FuenteIngreso> findById(Long id) {
        return jpaRepository.findById(id)
                .map(FuenteIngresoEntityMapper::toDomain);
    }

    @Override
    public List<FuenteIngreso> findByUsuarioId(Long usuarioId) {
        return jpaRepository.findByUserId(usuarioId).stream()
                .map(FuenteIngresoEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<FuenteIngreso> findActivasByUsuarioId(Long usuarioId) {
        return jpaRepository.findByUserIdAndIsActiveTrue(usuarioId).stream()
                .map(FuenteIngresoEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<FuenteIngreso> findByUsuarioIdAndActiva(Long usuarioId, boolean activa) {
        return jpaRepository.findByUserIdAndIsActive(usuarioId, activa).stream()
                .map(FuenteIngresoEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<FuenteIngreso> findByUsuarioIdAndTipo(Long usuarioId, TipoFuente tipo) {
        return jpaRepository.findByUserIdAndSourceType(usuarioId, tipo).stream()
                .map(FuenteIngresoEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<FuenteIngreso> findIngresosRealesByUsuarioId(Long usuarioId) {
        return jpaRepository.findByUserIdAndIsRealIncomeTrue(usuarioId).stream()
                .map(FuenteIngresoEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<FuenteIngreso> findByUsuarioIdAndNombre(Long usuarioId, String nombre) {
        // Este método necesita ser implementado en el JpaRepository si se necesita
        // Por ahora retornamos vacío
        return Optional.empty();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByUsuarioIdAndNombre(Long usuarioId, String nombre) {
        // Este método necesita ser implementado en el JpaRepository si se necesita
        return false;
    }

    @Override
    public long countActivasByUsuarioId(Long usuarioId) {
        return jpaRepository.findByUserIdAndIsActiveTrue(usuarioId).size();
    }
}

