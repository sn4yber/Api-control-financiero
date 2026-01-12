package com.controfinanciero.infrastructure.persistence.mapper;

import com.controfinanciero.domain.model.MovimientoFinanciero;
import com.controfinanciero.infrastructure.persistence.entity.MovimientoFinancieroEntity;

/**
 * Mapper para convertir entre MovimientoFinanciero (Domain) y MovimientoFinancieroEntity (Persistence).
 */
public class MovimientoFinancieroEntityMapper {

    /**
     * Convierte de Entity a Domain.
     */
    public static MovimientoFinanciero toDomain(MovimientoFinancieroEntity entity) {
        if (entity == null) {
            return null;
        }

        MovimientoFinanciero movimiento = new MovimientoFinanciero(
                entity.getUserId(),
                entity.getMovementType(),
                entity.getAmount(),
                entity.getDescription(),
                entity.getMovementDate()
        );

        // Usar reflexión para establecer campos privados
        try {
            var idField = MovimientoFinanciero.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(movimiento, entity.getId());

            var categoriaIdField = MovimientoFinanciero.class.getDeclaredField("categoriaId");
            categoriaIdField.setAccessible(true);
            categoriaIdField.set(movimiento, entity.getCategoryId());

            var fuenteIngresoIdField = MovimientoFinanciero.class.getDeclaredField("fuenteIngresoId");
            fuenteIngresoIdField.setAccessible(true);
            fuenteIngresoIdField.set(movimiento, entity.getIncomeSourceId());

            var metaIdField = MovimientoFinanciero.class.getDeclaredField("metaId");
            metaIdField.setAccessible(true);
            metaIdField.set(movimiento, entity.getGoalId());

            var esRecurrenteField = MovimientoFinanciero.class.getDeclaredField("esRecurrente");
            esRecurrenteField.setAccessible(true);
            esRecurrenteField.set(movimiento, entity.getIsRecurring());

            var patronRecurrenciaField = MovimientoFinanciero.class.getDeclaredField("patronRecurrencia");
            patronRecurrenciaField.setAccessible(true);
            patronRecurrenciaField.set(movimiento, entity.getRecurrencePattern());

            var notasField = MovimientoFinanciero.class.getDeclaredField("notas");
            notasField.setAccessible(true);
            notasField.set(movimiento, entity.getNotes());

            var createdAtField = MovimientoFinanciero.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(movimiento, entity.getCreatedAt());

            var updatedAtField = MovimientoFinanciero.class.getDeclaredField("updatedAt");
            updatedAtField.setAccessible(true);
            updatedAtField.set(movimiento, entity.getUpdatedAt());
        } catch (Exception e) {
            throw new RuntimeException("Error al mapear MovimientoFinancieroEntity a MovimientoFinanciero", e);
        }

        return movimiento;
    }

    /**
     * Convierte de Domain a Entity.
     */
    public static MovimientoFinancieroEntity toEntity(MovimientoFinanciero movimiento) {
        if (movimiento == null) {
            return null;
        }

        MovimientoFinancieroEntity entity = new MovimientoFinancieroEntity(
                movimiento.getUsuarioId(),
                movimiento.getTipoMovimiento(),
                movimiento.getMonto(),
                movimiento.getDescripcion(),
                movimiento.getFechaMovimiento()
        );

        if (movimiento.getId() != null) {
            entity.setId(movimiento.getId());
        }
        entity.setCategoryId(movimiento.getCategoriaId());
        entity.setIncomeSourceId(movimiento.getFuenteIngresoId());
        entity.setGoalId(movimiento.getMetaId());
        entity.setIsRecurring(movimiento.isEsRecurrente());
        entity.setRecurrencePattern(movimiento.getPatronRecurrencia());
        entity.setNotes(movimiento.getNotas());

        if (movimiento.getCreatedAt() != null) {
            entity.setCreatedAt(movimiento.getCreatedAt());
        }
        if (movimiento.getUpdatedAt() != null) {
            entity.setUpdatedAt(movimiento.getUpdatedAt());
        }

        return entity;
    }
}

