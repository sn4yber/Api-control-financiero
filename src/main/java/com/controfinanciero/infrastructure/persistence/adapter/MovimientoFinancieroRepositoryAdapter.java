package com.controfinanciero.infrastructure.persistence.adapter;

import com.controfinanciero.domain.model.MovimientoFinanciero;
import com.controfinanciero.domain.model.enums.TipoMovimiento;
import com.controfinanciero.domain.repository.MovimientoFinancieroRepository;
import com.controfinanciero.infrastructure.persistence.entity.MovimientoFinancieroEntity;
import com.controfinanciero.infrastructure.persistence.mapper.MovimientoFinancieroEntityMapper;
import com.controfinanciero.infrastructure.persistence.repository.MovimientoFinancieroJpaRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adapter que implementa el port MovimientoFinancieroRepository.
 */
@Component
public class MovimientoFinancieroRepositoryAdapter implements MovimientoFinancieroRepository {

    private final MovimientoFinancieroJpaRepository jpaRepository;

    public MovimientoFinancieroRepositoryAdapter(MovimientoFinancieroJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public MovimientoFinanciero save(MovimientoFinanciero movimiento) {
        MovimientoFinancieroEntity entity = MovimientoFinancieroEntityMapper.toEntity(movimiento);
        MovimientoFinancieroEntity savedEntity = jpaRepository.save(entity);
        return MovimientoFinancieroEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<MovimientoFinanciero> findById(Long id) {
        return jpaRepository.findById(id)
                .map(MovimientoFinancieroEntityMapper::toDomain);
    }

    @Override
    public List<MovimientoFinanciero> findByUsuarioId(Long usuarioId) {
        return jpaRepository.findByUserIdOrderByMovementDateDesc(usuarioId).stream()
                .map(MovimientoFinancieroEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<MovimientoFinanciero> findByUsuarioIdAndFechaBetween(Long usuarioId, LocalDate fechaInicio, LocalDate fechaFin) {
        return jpaRepository.findByUserIdAndDateRange(usuarioId, fechaInicio, fechaFin).stream()
                .map(MovimientoFinancieroEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<MovimientoFinanciero> findByUsuarioIdAndTipo(Long usuarioId, TipoMovimiento tipo) {
        return jpaRepository.findByUserIdAndMovementType(usuarioId, tipo).stream()
                .map(MovimientoFinancieroEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<MovimientoFinanciero> findByUsuarioIdAndCategoriaId(Long usuarioId, Long categoriaId) {
        return jpaRepository.findByUserIdAndCategoryId(usuarioId, categoriaId).stream()
                .map(MovimientoFinancieroEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<MovimientoFinanciero> findByUsuarioIdAndMetaId(Long usuarioId, Long metaId) {
        // Filtrar por usuario y meta
        return jpaRepository.findByGoalId(metaId).stream()
                .map(MovimientoFinancieroEntityMapper::toDomain)
                .filter(m -> m.getUsuarioId().equals(usuarioId))
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal sumIngresosByUsuarioIdAndFechaBetween(Long usuarioId, LocalDate fechaInicio, LocalDate fechaFin) {
        return jpaRepository.findByUserIdAndDateRange(usuarioId, fechaInicio, fechaFin).stream()
                .filter(m -> m.getMovementType().esIngreso())
                .map(MovimientoFinancieroEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal sumGastosByUsuarioIdAndFechaBetween(Long usuarioId, LocalDate fechaInicio, LocalDate fechaFin) {
        return jpaRepository.findByUserIdAndDateRange(usuarioId, fechaInicio, fechaFin).stream()
                .filter(m -> m.getMovementType().esGasto())
                .map(MovimientoFinancieroEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal sumAhorrosByUsuarioIdAndFechaBetween(Long usuarioId, LocalDate fechaInicio, LocalDate fechaFin) {
        return jpaRepository.findByUserIdAndDateRange(usuarioId, fechaInicio, fechaFin).stream()
                .filter(m -> m.getMovementType().esAhorro())
                .map(MovimientoFinancieroEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public List<MovimientoFinanciero> findRecurrentesByUsuarioId(Long usuarioId) {
        return jpaRepository.findByUserIdAndIsRecurringTrue(usuarioId).stream()
                .map(MovimientoFinancieroEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public long countByUsuarioId(Long usuarioId) {
        return jpaRepository.findByUserIdOrderByMovementDateDesc(usuarioId).size();
    }
}

