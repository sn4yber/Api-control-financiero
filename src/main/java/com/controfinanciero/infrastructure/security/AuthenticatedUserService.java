package com.controfinanciero.infrastructure.security;

import com.controfinanciero.domain.model.Usuario;
import com.controfinanciero.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Service to get the current authenticated user from JWT token
 */
@Service
@RequiredArgsConstructor
public class AuthenticatedUserService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Gets the current authenticated user
     * @return Authenticated user
     * @throws RuntimeException if no user is authenticated
     */
    public Usuario getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return usuarioRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Authenticated user not found in database"));
        }

        throw new RuntimeException("No authenticated user");
    }

    /**
     * Gets the ID of the current authenticated user
     * @return User ID
     */
    public Long getCurrentUserId() {
        return getCurrentUser().getId();
    }

    /**
     * Verifies if the current user owns the resource
     * @param resourceUserId ID of the resource owner
     * @return true if current user is the owner
     */
    public boolean isCurrentUserOwner(Long resourceUserId) {
        return getCurrentUserId().equals(resourceUserId);
    }
}

