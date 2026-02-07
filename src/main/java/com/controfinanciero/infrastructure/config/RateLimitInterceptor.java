package com.controfinanciero.infrastructure.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 🛡️ Rate Limiting Interceptor
 * Limita el número de peticiones por IP/usuario para prevenir abuso
 */
@Slf4j
@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String key = getClientIdentifier(request);
        Bucket bucket = resolveBucket(key);

        if (bucket.tryConsume(1)) {
            // Agregar headers informativos
            response.addHeader("X-Rate-Limit-Remaining", String.valueOf(bucket.getAvailableTokens()));
            return true;
        }

        // Rate limit excedido
        log.warn("🚨 Rate limit excedido para: {}", key);
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"Too many requests\",\"message\":\"Rate limit exceeded. Please try again later.\"}");
        return false;
    }

    private Bucket resolveBucket(String key) {
        return cache.computeIfAbsent(key, k -> createNewBucket());
    }

    private Bucket createNewBucket() {
        // Límite: 100 peticiones por minuto
        Bandwidth limit = Bandwidth.classic(100, Refill.intervally(100, Duration.ofMinutes(1)));
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    private String getClientIdentifier(HttpServletRequest request) {
        // Primero intentar obtener del header de autenticación
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return "user:" + authHeader.substring(7, Math.min(authHeader.length(), 20));
        }

        // Si no hay token, usar IP
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        return "ip:" + ip;
    }
}

