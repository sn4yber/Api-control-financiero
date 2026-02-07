package com.controfinanciero.infrastructure.persistence.mapper;

import com.controfinanciero.domain.model.MetaFinanciera;
import com.controfinanciero.infrastructure.persistence.entity.MetaFinancieraEntity;

/**
 * Mapper para convertir entre MetaFinanciera (Domain) y MetaFinancieraEntity (Persistence).
 */
public class MetaFinancieraEntityMapper {

    /**
     * Convierte de Entity a Domain.
     */
    public static MetaFinanciera toDomain(MetaFinancieraEntity entity) {
        if (entity == null) {
            return null;
        }

        MetaFinanciera meta = new MetaFinanciera(
                entity.getUserId(),
                entity.getName(),
                entity.getTargetAmount(),
                entity.getTargetDate(),
                entity.getPriority()
        );

        // Usar reflexión para establecer campos privados
        try {
            var idField = MetaFinanciera.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(meta, entity.getId());

            var descripcionField = MetaFinanciera.class.getDeclaredField("descripcion");
            descripcionField.setAccessible(true);
            descripcionField.set(meta, entity.getDescription());

            var montoActualField = MetaFinanciera.class.getDeclaredField("montoActual");
            montoActualField.setAccessible(true);
            montoActualField.set(meta, entity.getCurrentAmount());

            var estadoField = MetaFinanciera.class.getDeclaredField("estado");
            estadoField.setAccessible(true);
            estadoField.set(meta, entity.getStatus());

            var createdAtField = MetaFinanciera.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(meta, entity.getCreatedAt());

            var updatedAtField = MetaFinanciera.class.getDeclaredField("updatedAt");
            updatedAtField.setAccessible(true);
            updatedAtField.set(meta, entity.getUpdatedAt());

            var completedAtField = MetaFinanciera.class.getDeclaredField("completedAt");
            completedAtField.setAccessible(true);
            completedAtField.set(meta, entity.getCompletedAt());
        } catch (Exception e) {
            throw new RuntimeException("Error al mapear MetaFinancieraEntity a MetaFinanciera", e);
        }

        return meta;
    }

    /**
     * Convierte de Domain a Entity.
     */
    public static MetaFinancieraEntity toEntity(MetaFinanciera meta) {
        if (meta == null) {
            return null;
        }

        MetaFinancieraEntity entity = new MetaFinancieraEntity(
                meta.getUsuarioId(),
                meta.getNombre(),
                meta.getMontoObjetivo(),
                meta.getFechaObjetivo(),
                meta.getPrioridad()
        );

        // Establecer campos adicionales
        if (meta.getId() != null) {
            entity.setId(meta.getId());
        }
        entity.setDescription(meta.getDescripcion());
        entity.setCurrentAmount(meta.getMontoActual());
        entity.setStatus(meta.getEstado());
        entity.setCreatedAt(meta.getCreatedAt());
        entity.setUpdatedAt(meta.getUpdatedAt());
        entity.setCompletedAt(meta.getCompletedAt());

        return entity;
    }

    /**
     * Actualiza una entidad existente con datos del dominio.
     */
    public static void updateEntity(MetaFinanciera meta, MetaFinancieraEntity entity) {
        if (meta == null || entity == null) {
            return;
        }

        entity.setName(meta.getNombre());
        entity.setDescription(meta.getDescripcion());
        entity.setTargetAmount(meta.getMontoObjetivo());
        entity.setCurrentAmount(meta.getMontoActual());
        entity.setTargetDate(meta.getFechaObjetivo());
        entity.setPriority(meta.getPrioridad());
        entity.setStatus(meta.getEstado());
        entity.setUpdatedAt(meta.getUpdatedAt());
        entity.setCompletedAt(meta.getCompletedAt());
    }
}

