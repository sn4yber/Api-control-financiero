package com.controfinanciero.infrastructure.persistence.repository;

import com.controfinanciero.domain.model.enums.TipoCategoria;
import com.controfinanciero.infrastructure.persistence.entity.CategoriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA Repository para Categoría.
 * Spring Data genera automáticamente la implementación.
 */
@Repository
public interface CategoriaJpaRepository extends JpaRepository<CategoriaEntity, Long> {

    /**
     * Busca todas las categorías de un usuario.
     */
    List<CategoriaEntity> findByUserId(Long userId);

    /**
     * Busca categorías activas de un usuario.
     */
    List<CategoriaEntity> findByUserIdAndIsActiveTrue(Long userId);

    /**
     * Busca categorías de un usuario filtrando por activas.
     */
    List<CategoriaEntity> findByUserIdAndIsActive(Long userId, Boolean isActive);

    /**
     * Busca categorías de un usuario por tipo.
     */
    List<CategoriaEntity> findByUserIdAndCategoryType(Long userId, TipoCategoria categoryType);

    /**
     * Busca una categoría por usuario y nombre.
     */
    java.util.Optional<CategoriaEntity> findByUserIdAndName(Long userId, String name);

    /**
     * Verifica si existe una categoría con ese nombre para el usuario.
     */
    boolean existsByUserIdAndName(Long userId, String name);

    /**
     * Cuenta categorías activas de un usuario.
     */
    long countByUserIdAndIsActiveTrue(Long userId);
}

