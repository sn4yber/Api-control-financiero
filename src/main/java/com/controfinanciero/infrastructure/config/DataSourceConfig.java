package com.controfinanciero.infrastructure.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Configuración personalizada del DataSource para limpiar parámetros incompatibles
 * de la URL de conexión (como channel_binding que Neon agrega pero Java PostgreSQL driver no soporta)
 */
@Configuration
public class DataSourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public DataSource dataSource(DataSourceProperties properties) {
        String url = properties.getUrl();

        // Limpiar parámetros incompatibles que Neon puede agregar
        if (url != null && url.contains("channel_binding")) {
            // Remover &channel_binding=require o ?channel_binding=require
            url = url.replaceAll("[&?]channel_binding=[^&]*", "");
            // Limpiar & duplicados que pueden quedar
            url = url.replaceAll("\\?&", "?");
            url = url.replaceAll("&&", "&");

            System.out.println("🔧 URL de base de datos sanitizada (channel_binding removido)");
            System.out.println("📍 URL limpia: " + url.replaceAll(":[^:@]+@", ":***@")); // Ocultar password en logs
        }

        HikariDataSource dataSource = properties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();

        dataSource.setJdbcUrl(url);

        return dataSource;
    }
}

