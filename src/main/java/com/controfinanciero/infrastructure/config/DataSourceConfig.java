package com.controfinanciero.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.net.URI;

/**
 * Configuración personalizada del DataSource para limpiar parámetros incompatibles
 * de la URL de conexión y parsear correctamente URLs de Neon/Heroku style
 */
@Configuration
public class DataSourceConfig {

    @Bean
    @Primary
    public DataSource dataSource(@Value("${DATABASE_URL:${spring.datasource.url}}") String databaseUrl) {
        String url = databaseUrl;
        String username = null;
        String password = null;

        try {
            // Agregar prefijo jdbc: si no está presente
            if (url != null && !url.startsWith("jdbc:") && url.startsWith("postgresql://")) {
                url = "jdbc:" + url;
                System.out.println("🔧 Agregado prefijo 'jdbc:' a la URL de base de datos");
            }

            // Parsear la URL para extraer usuario y password (formato Neon/Heroku)
            if (url != null && url.startsWith("jdbc:postgresql://")) {
                URI uri = new URI(url.substring(5)); // Remove "jdbc:" prefix for parsing

                String userInfo = uri.getUserInfo();
                if (userInfo != null && userInfo.contains(":")) {
                    String[] credentials = userInfo.split(":", 2);
                    username = credentials[0];
                    password = credentials[1];

                    // Reconstruir URL sin usuario/password (HikariCP los necesita separados)
                    url = "jdbc:postgresql://" + uri.getHost() +
                          (uri.getPort() > 0 ? ":" + uri.getPort() : "") +
                          uri.getPath() +
                          (uri.getQuery() != null ? "?" + uri.getQuery() : "");

                    System.out.println("🔧 Usuario y password extraídos de la URL");
                }
            }

            // Limpiar parámetros incompatibles que Neon puede agregar
            if (url != null && url.contains("channel_binding")) {
                url = url.replaceAll("[&?]channel_binding=[^&]*", "");
                url = url.replaceAll("\\?&", "?");
                url = url.replaceAll("&&", "&");
                System.out.println("🔧 Parámetro channel_binding eliminado");
            }

            System.out.println("📍 URL final: " + (url != null ? url.replaceAll(":[^:@]+@", ":***@") : "null"));
            System.out.println("👤 Usuario: " + (username != null ? username : "no extraído"));

        } catch (Exception e) {
            System.err.println("❌ Error parseando DATABASE_URL: " + e.getMessage());
            throw new RuntimeException("No se pudo parsear DATABASE_URL", e);
        }

        // Configurar HikariCP directamente
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName("org.postgresql.Driver");

        // Pool settings optimizados para Render Free tier
        config.setMaximumPoolSize(5);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);

        return new HikariDataSource(config);
    }
}

