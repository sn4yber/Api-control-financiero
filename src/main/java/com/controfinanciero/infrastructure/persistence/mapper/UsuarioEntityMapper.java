
package com.controfinanciero.infrastructure.persistence.mapper;

import com.controfinanciero.domain.model.Usuario;
import com.controfinanciero.infrastructure.persistence.entity.UsuarioEntity;

/**
 * Mapper para convertir entre Usuario (Domain) y UsuarioEntity (Persistence).
 */
public class UsuarioEntityMapper {

    /**
     * Convierte de Entity a Domain.
     */
    public static Usuario toDomain(UsuarioEntity entity) {
        if (entity == null) {
            return null;
        }

        Usuario usuario = new Usuario(
                entity.getUsername(),
                entity.getEmail(),
                entity.getPasswordHash(),
                entity.getFullName()
        );

        // Usar reflexión para establecer el ID (porque no hay setter público)
        try {
            var idField = Usuario.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(usuario, entity.getId());

            var createdAtField = Usuario.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(usuario, entity.getCreatedAt());

            var updatedAtField = Usuario.class.getDeclaredField("updatedAt");
            updatedAtField.setAccessible(true);
            updatedAtField.set(usuario, entity.getUpdatedAt());

            var activeField = Usuario.class.getDeclaredField("active");
            activeField.setAccessible(true);
            activeField.set(usuario, entity.getActive());
        } catch (Exception e) {
            throw new RuntimeException("Error al mapear UsuarioEntity a Usuario", e);
        }

        return usuario;
    }

    /**
     * Convierte de Domain a Entity.
     */
    public static UsuarioEntity toEntity(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        UsuarioEntity entity = new UsuarioEntity(
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getPasswordHash(),
                usuario.getFullName()
        );

        // Establecer campos adicionales si existen
        if (usuario.getId() != null) {
            entity.setId(usuario.getId());
        }
        if (usuario.getCreatedAt() != null) {
            entity.setCreatedAt(usuario.getCreatedAt());
        }
        if (usuario.getUpdatedAt() != null) {
            entity.setUpdatedAt(usuario.getUpdatedAt());
        }
        entity.setActive(usuario.estaActivo());

        return entity;
    }

    /**
     * Actualiza una entidad existente con datos del dominio.
     */
    public static void updateEntity(UsuarioEntity entity, Usuario usuario) {
        if (entity == null || usuario == null) {
            return;
        }

        entity.setUsername(usuario.getUsername());
        entity.setEmail(usuario.getEmail());
        entity.setPasswordHash(usuario.getPasswordHash());
        entity.setFullName(usuario.getFullName());
        entity.setActive(usuario.estaActivo());
    }
}
