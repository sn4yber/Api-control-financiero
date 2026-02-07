package com.controfinanciero.infrastructure.persistence.mapper;

import com.controfinanciero.domain.model.Categoria;
import com.controfinanciero.infrastructure.persistence.entity.CategoriaEntity;

/**
 * Mapper para convertir entre Categoria (Domain) y CategoriaEntity (Persistence).
 */
public class CategoriaEntityMapper {

    /**
     * Convierte de Entity a Domain.
     */
    public static Categoria toDomain(CategoriaEntity entity) {
        if (entity == null) {
            return null;
        }

        Categoria categoria = new Categoria(
                entity.getUserId(),
                entity.getName(),
                entity.getCategoryType()
        );

        // Usar reflexión para establecer campos privados
        try {
            var idField = Categoria.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(categoria, entity.getId());

            var descripcionField = Categoria.class.getDeclaredField("descripcion");
            descripcionField.setAccessible(true);
            descripcionField.set(categoria, entity.getDescription());

            var colorHexField = Categoria.class.getDeclaredField("colorHex");
            colorHexField.setAccessible(true);
            colorHexField.set(categoria, entity.getColorHex());

            var iconoField = Categoria.class.getDeclaredField("icono");
            iconoField.setAccessible(true);
            iconoField.set(categoria, entity.getIcon());

            var activaField = Categoria.class.getDeclaredField("activa");
            activaField.setAccessible(true);
            activaField.set(categoria, entity.getIsActive());

            var createdAtField = Categoria.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(categoria, entity.getCreatedAt());

            var updatedAtField = Categoria.class.getDeclaredField("updatedAt");
            updatedAtField.setAccessible(true);
            updatedAtField.set(categoria, entity.getUpdatedAt());
        } catch (Exception e) {
            throw new RuntimeException("Error al mapear CategoriaEntity a Categoria", e);
        }

        return categoria;
    }

    /**
     * Convierte de Domain a Entity.
     */
    public static CategoriaEntity toEntity(Categoria categoria) {
        if (categoria == null) {
            return null;
        }

        CategoriaEntity entity = new CategoriaEntity(
                categoria.getUsuarioId(),
                categoria.getNombre(),
                categoria.getTipoCategoria()
        );

        // Establecer campos adicionales
        if (categoria.getId() != null) {
            entity.setId(categoria.getId());
        }
        entity.setDescription(categoria.getDescripcion());
        entity.setColorHex(categoria.getColorHex());
        entity.setIcon(categoria.getIcono());
        entity.setIsActive(categoria.estaActiva());

        if (categoria.getCreatedAt() != null) {
            entity.setCreatedAt(categoria.getCreatedAt());
        }
        if (categoria.getUpdatedAt() != null) {
            entity.setUpdatedAt(categoria.getUpdatedAt());
        }

        return entity;
    }

    /**
     * Actualiza una Entity existente con datos del Domain.
     */
    public static void updateEntity(Categoria categoria, CategoriaEntity entity) {
        if (categoria == null || entity == null) {
            return;
        }

        entity.setName(categoria.getNombre());
        entity.setDescription(categoria.getDescripcion());
        entity.setColorHex(categoria.getColorHex());
        entity.setIcon(categoria.getIcono());
        entity.setCategoryType(categoria.getTipoCategoria());
        entity.setIsActive(categoria.estaActiva());
    }
}

