package com.controfinanciero.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

/**
 * Configuración de CORS para permitir peticiones desde el frontend React
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // Permitir credenciales (cookies, headers de autenticación)
        config.setAllowCredentials(true);

        // Orígenes permitidos (ajusta según tu entorno)
        config.setAllowedOriginPatterns(Arrays.asList(
                "http://localhost:3000",           // React en desarrollo
                "http://localhost:5173",           // Vite en desarrollo
                "http://localhost:4200",           // Angular en desarrollo
                "https://*.vercel.app",            // Vercel deployment
                "https://*.netlify.app",           // Netlify deployment
                "https://*.onrender.com",          // Render deployment
                "https://api-control-financiero.onrender.com", // Tu API en Render
                "https://api-control-financiero.onrender.com/**" // Incluye todas las rutas
        ));

        // Métodos HTTP permitidos
        config.setAllowedMethods(Arrays.asList(
                "GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS"
        ));

        // Headers permitidos
        config.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "Accept",
                "Origin",
                "X-Requested-With",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers"
        ));

        // Headers expuestos (visibles para el cliente)
        config.setExposedHeaders(Arrays.asList(
                "Authorization",
                "Content-Disposition",
                "X-Total-Count"
        ));

        // Tiempo máximo de cache para preflight requests
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}

