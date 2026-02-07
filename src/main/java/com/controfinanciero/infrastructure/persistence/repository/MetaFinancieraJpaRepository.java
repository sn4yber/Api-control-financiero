package com.controfinanciero.infrastructure.persistence.repository;

import com.controfinanciero.domain.model.enums.EstadoMeta;
import com.controfinanciero.infrastructure.persistence.entity.MetaFinancieraEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * JPA Repository para Meta Financiera.
 * Spring Data genera automáticamente la implementación.
 */
@Repository
public interface MetaFinancieraJpaRepository extends JpaRepository<MetaFinancieraEntity, Long> {

    /**
     * Busca todas las metas de un usuario.
     */
    List<MetaFinancieraEntity> findByUserId(Long userId);

    /**
     * Busca metas de un usuario por estado.
     */
    List<MetaFinancieraEntity> findByUserIdAndStatus(Long userId, EstadoMeta status);

    /**
     * Busca metas activas de un usuario.
     */
    default List<MetaFinancieraEntity> findActiveByUserId(Long userId) {
        return findByUserIdAndStatus(userId, EstadoMeta.ACTIVE);
    }

    /**
     * Busca metas completadas de un usuario.
     */
    default List<MetaFinancieraEntity> findCompletedByUserId(Long userId) {
        return findByUserIdAndStatus(userId, EstadoMeta.COMPLETED);
    }

    /**
     * Busca metas que vencen antes de una fecha.
     */
    @Query("SELECT m FROM MetaFinancieraEntity m WHERE m.userId = :userId " +
           "AND m.status = 'ACTIVE' " +
           "AND m.targetDate IS NOT NULL " +
           "AND m.targetDate < :date " +
           "ORDER BY m.targetDate ASC")
    List<MetaFinancieraEntity> findActiveGoalsExpiringBefore(
            @Param("userId") Long userId,
            @Param("date") LocalDate date
    );
}

