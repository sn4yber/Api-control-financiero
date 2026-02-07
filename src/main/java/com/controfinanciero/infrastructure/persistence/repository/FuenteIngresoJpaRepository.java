package com.controfinanciero.infrastructure.persistence.repository;

import com.controfinanciero.domain.model.enums.TipoFuente;
import com.controfinanciero.infrastructure.persistence.entity.FuenteIngresoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA Repository para Fuente de Ingreso.
 * Spring Data genera automáticamente la implementación.
 */
@Repository
public interface FuenteIngresoJpaRepository extends JpaRepository<FuenteIngresoEntity, Long> {

    /**
     * Busca todas las fuentes de ingreso de un usuario.
     */
    List<FuenteIngresoEntity> findByUserId(Long userId);

    /**
     * Busca fuentes de ingreso activas de un usuario.
     */
    List<FuenteIngresoEntity> findByUserIdAndIsActiveTrue(Long userId);

    /**
     * Busca fuentes de ingreso por usuario y estado activo.
     */
    List<FuenteIngresoEntity> findByUserIdAndIsActive(Long userId, boolean isActive);

    /**
     * Busca fuentes de ingreso por tipo.
     */
    List<FuenteIngresoEntity> findByUserIdAndSourceType(Long userId, TipoFuente sourceType);

    /**
     * Busca solo ingresos reales (excluye préstamos).
     */
    List<FuenteIngresoEntity> findByUserIdAndIsRealIncomeTrue(Long userId);
}
