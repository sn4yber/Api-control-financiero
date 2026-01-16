package com.controfinanciero.infrastructure.security.service;

import com.controfinanciero.domain.model.Usuario;
import com.controfinanciero.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * 🔐 Servicio de Autenticación
 * Obtiene información del usuario autenticado actual
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Obtiene el usuario autenticado actual desde el contexto de seguridad
     * @return Usuario autenticado
     * @throws RuntimeException si no hay usuario autenticado
     */
    public Usuario getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No hay usuario autenticado");
        }

        String username = authentication.getName();

        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));
    }

    /**
     * Obtiene el ID del usuario autenticado actual
     */
    public Long getCurrentUserId() {
        return getCurrentUser().getId();
    }

    /**
     * Verifica si el recurso pertenece al usuario autenticado
     */
    public boolean isCurrentUserOwner(Long resourceUserId) {
        return getCurrentUserId().equals(resourceUserId);
    }
}

