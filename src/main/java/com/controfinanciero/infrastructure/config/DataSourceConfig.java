package com.controfinanciero.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Configuración personalizada del DataSource para limpiar parámetros incompatibles
 * de la URL de conexión (como channel_binding que Neon agrega pero Java PostgreSQL driver no soporta)
 */
@Configuration
public class DataSourceConfig {

    @Bean
    @Primary
    public DataSource dataSource(@Value("${DATABASE_URL:${spring.datasource.url}}") String databaseUrl) {
        String url = databaseUrl;

        // Agregar prefijo jdbc: si no está presente (Neon da postgresql:// pero JDBC necesita jdbc:postgresql://)
        if (url != null && !url.startsWith("jdbc:") && url.startsWith("postgresql://")) {
            url = "jdbc:" + url;
            System.out.println("🔧 Agregado prefijo 'jdbc:' a la URL de base de datos");
        }

        // Limpiar parámetros incompatibles que Neon puede agregar
        if (url != null && url.contains("channel_binding")) {
            // Remover &channel_binding=require o ?channel_binding=require
            url = url.replaceAll("[&?]channel_binding=[^&]*", "");
            // Limpiar & duplicados que pueden quedar
            url = url.replaceAll("\\?&", "?");
            url = url.replaceAll("&&", "&");

            System.out.println("🔧 URL de base de datos sanitizada (channel_binding removido)");
        }

        System.out.println("📍 URL final: " + (url != null ? url.replaceAll(":[^:@]+@", ":***@") : "null")); // Ocultar password en logs

        // Configurar HikariCP directamente
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
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

