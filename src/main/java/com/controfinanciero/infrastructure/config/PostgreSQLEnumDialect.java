package com.controfinanciero.infrastructure.config;

import org.hibernate.dialect.PostgreSQLDialect;

/**
 * Dialecto personalizado de PostgreSQL para manejar ENUMs nativos.
 * Extiende PostgreSQLDialect y agrega soporte mejorado para tipos ENUM.
 */
public class PostgreSQLEnumDialect extends PostgreSQLDialect {

    public PostgreSQLEnumDialect() {
        super();
    }
}

