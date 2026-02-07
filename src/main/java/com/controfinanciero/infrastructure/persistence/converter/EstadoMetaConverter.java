package com.controfinanciero.infrastructure.persistence.converter;

import com.controfinanciero.domain.model.enums.EstadoMeta;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Converter para mapear EstadoMeta a PostgreSQL ENUM.
 */
@Converter(autoApply = true)
public class EstadoMetaConverter implements AttributeConverter<EstadoMeta, String> {

    @Override
    public String convertToDatabaseColumn(EstadoMeta attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public EstadoMeta convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return EstadoMeta.valueOf(dbData);
    }
}

