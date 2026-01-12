package com.controfinanciero.infrastructure.persistence.mapper;

import com.controfinanciero.domain.model.FuenteIngreso;
import com.controfinanciero.infrastructure.persistence.entity.FuenteIngresoEntity;

/**
 * Mapper para convertir entre FuenteIngreso (Domain) y FuenteIngresoEntity (Persistence).
 */
public class FuenteIngresoEntityMapper {

    /**
     * Convierte de Entity a Domain.
     */
    public static FuenteIngreso toDomain(FuenteIngresoEntity entity) {
        if (entity == null) {
            return null;
        }

        FuenteIngreso fuente = new FuenteIngreso(
                entity.getUserId(),
                entity.getName(),
                entity.getSourceType()
        );

        // Usar reflexión para establecer campos privados
        try {
            var idField = FuenteIngreso.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(fuente, entity.getId());

            var descripcionField = FuenteIngreso.class.getDeclaredField("descripcion");
            descripcionField.setAccessible(true);
            descripcionField.set(fuente, entity.getDescription());

            var esIngresoRealField = FuenteIngreso.class.getDeclaredField("esIngresoReal");
            esIngresoRealField.setAccessible(true);
            esIngresoRealField.set(fuente, entity.getIsRealIncome());

            var activaField = FuenteIngreso.class.getDeclaredField("activa");
            activaField.setAccessible(true);
            activaField.set(fuente, entity.getIsActive());

            var createdAtField = FuenteIngreso.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(fuente, entity.getCreatedAt());

            var updatedAtField = FuenteIngreso.class.getDeclaredField("updatedAt");
            updatedAtField.setAccessible(true);
            updatedAtField.set(fuente, entity.getUpdatedAt());
        } catch (Exception e) {
            throw new RuntimeException("Error al mapear FuenteIngresoEntity a FuenteIngreso", e);
        }

        return fuente;
    }

    /**
     * Convierte de Domain a Entity.
     */
    public static FuenteIngresoEntity toEntity(FuenteIngreso fuente) {
        if (fuente == null) {
            return null;
        }

        FuenteIngresoEntity entity = new FuenteIngresoEntity(
                fuente.getUsuarioId(),
                fuente.getNombre(),
                fuente.getTipoFuente()
        );

        // Establecer campos adicionales
        if (fuente.getId() != null) {
            entity.setId(fuente.getId());
        }
        entity.setDescription(fuente.getDescripcion());
        entity.setIsRealIncome(fuente.isEsIngresoReal());
        entity.setIsActive(fuente.isActiva());
        entity.setCreatedAt(fuente.getCreatedAt());
        entity.setUpdatedAt(fuente.getUpdatedAt());

        return entity;
    }

    /**
     * Actualiza una entidad existente con datos del dominio.
     */
    public static void updateEntity(FuenteIngreso fuente, FuenteIngresoEntity entity) {
        if (fuente == null || entity == null) {
            return;
        }

        entity.setName(fuente.getNombre());
        entity.setDescription(fuente.getDescripcion());
        entity.setSourceType(fuente.getTipoFuente());
        entity.setIsRealIncome(fuente.isEsIngresoReal());
        entity.setIsActive(fuente.isActiva());
        entity.setUpdatedAt(fuente.getUpdatedAt());
    }
}

