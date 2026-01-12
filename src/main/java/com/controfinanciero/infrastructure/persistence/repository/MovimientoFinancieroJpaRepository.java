package com.controfinanciero.infrastructure.persistence.repository;

import com.controfinanciero.domain.model.enums.TipoMovimiento;
import com.controfinanciero.infrastructure.persistence.entity.MovimientoFinancieroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * JPA Repository para Movimiento Financiero.
 * Spring Data genera automáticamente la implementación.
 */
@Repository
public interface MovimientoFinancieroJpaRepository extends JpaRepository<MovimientoFinancieroEntity, Long> {

    /**
     * Busca todos los movimientos de un usuario.
     */
    List<MovimientoFinancieroEntity> findByUserIdOrderByMovementDateDesc(Long userId);

    /**
     * Busca movimientos de un usuario por tipo.
     */
    List<MovimientoFinancieroEntity> findByUserIdAndMovementType(Long userId, TipoMovimiento movementType);

    /**
     * Busca movimientos de un usuario en un rango de fechas.
     */
    @Query("SELECT m FROM MovimientoFinancieroEntity m WHERE m.userId = :userId " +
           "AND m.movementDate BETWEEN :startDate AND :endDate " +
           "ORDER BY m.movementDate DESC")
    List<MovimientoFinancieroEntity> findByUserIdAndDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    /**
     * Busca movimientos de un usuario por categoría.
     */
    List<MovimientoFinancieroEntity> findByUserIdAndCategoryId(Long userId, Long categoryId);

    /**
     * Busca movimientos vinculados a una meta.
     */
    List<MovimientoFinancieroEntity> findByGoalId(Long goalId);

    /**
     * Busca movimientos recurrentes de un usuario.
     */
    List<MovimientoFinancieroEntity> findByUserIdAndIsRecurringTrue(Long userId);
}

