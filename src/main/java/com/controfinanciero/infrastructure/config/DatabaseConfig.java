package com.controfinanciero.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * Configuración de base de datos.
 * Incluye un bean de prueba para verificar la conexión a Neon PostgreSQL.
 */
@Slf4j
@Configuration
public class DatabaseConfig {

    /**
     * Bean que se ejecuta al inicio para verificar la conexión a la base de datos.
     * Útil para detectar problemas de conexión inmediatamente.
     */
    @Bean
    public CommandLineRunner testDatabaseConnection(DataSource dataSource) {
        return args -> {
            try (Connection connection = dataSource.getConnection()) {
                log.info("✅ Conexión exitosa a la base de datos Neon PostgreSQL");
                log.info("📊 Database: {}", connection.getCatalog());
                log.info("🔗 URL: {}", connection.getMetaData().getURL());
                log.info("👤 Usuario: {}", connection.getMetaData().getUserName());
            } catch (Exception e) {
                log.error("❌ Error al conectar a la base de datos: {}", e.getMessage(), e);
                throw new RuntimeException("No se pudo establecer conexión con Neon PostgreSQL", e);
            }
        };
    }
}

