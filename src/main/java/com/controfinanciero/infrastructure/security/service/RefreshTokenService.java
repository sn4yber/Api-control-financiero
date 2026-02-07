package com.controfinanciero.infrastructure.security.service;

import com.controfinanciero.infrastructure.security.model.RefreshToken;
import com.controfinanciero.infrastructure.security.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * 🔐 Servicio de gestión de Refresh Tokens
 * Maneja la creación, validación y revocación de tokens de refresco
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    // Refresh tokens duran 30 días
    private static final int REFRESH_TOKEN_VALIDITY_DAYS = 30;

    /**
     * Crea un nuevo refresh token para un usuario
     */
    @Transactional
    public RefreshToken createRefreshToken(Long userId) {
        // Revocar tokens anteriores del usuario
        revokeAllUserTokens(userId);

        // Crear nuevo token
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUserId(userId);
        refreshToken.setExpiresAt(LocalDateTime.now().plusDays(REFRESH_TOKEN_VALIDITY_DAYS));
        refreshToken.setRevoked(false);

        RefreshToken saved = refreshTokenRepository.save(refreshToken);
        log.info("🔑 Refresh token creado para usuario: {}", userId);

        return saved;
    }

    /**
     * Valida un refresh token
     */
    public Optional<RefreshToken> validateRefreshToken(String token) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);

        if (refreshToken.isEmpty()) {
            log.warn("⚠️ Refresh token no encontrado");
            return Optional.empty();
        }

        RefreshToken rt = refreshToken.get();

        if (!rt.isValid()) {
            log.warn("⚠️ Refresh token inválido o expirado para usuario: {}", rt.getUserId());
            return Optional.empty();
        }

        return refreshToken;
    }

    /**
     * Revoca un refresh token específico
     */
    @Transactional
    public void revokeToken(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(rt -> {
            rt.setRevoked(true);
            refreshTokenRepository.save(rt);
            log.info("🔒 Refresh token revocado");
        });
    }

    /**
     * Revoca todos los tokens de un usuario
     */
    @Transactional
    public void revokeAllUserTokens(Long userId) {
        var tokens = refreshTokenRepository.findByUserIdAndRevokedFalse(userId);
        tokens.forEach(token -> {
            token.setRevoked(true);
            refreshTokenRepository.save(token);
        });
        log.info("🔒 {} tokens revocados para usuario: {}", tokens.size(), userId);
    }

    /**
     * Elimina tokens expirados (limpieza)
     */
    @Transactional
    public void deleteExpiredTokens() {
        var allTokens = refreshTokenRepository.findAll();
        var expired = allTokens.stream()
                .filter(RefreshToken::isExpired)
                .toList();

        refreshTokenRepository.deleteAll(expired);
        log.info("🧹 {} tokens expirados eliminados", expired.size());
    }
}

