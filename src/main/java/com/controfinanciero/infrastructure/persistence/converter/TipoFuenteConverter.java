package com.controfinanciero.infrastructure.persistence.converter;

import com.controfinanciero.domain.model.enums.TipoFuente;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Converter para mapear TipoFuente a PostgreSQL ENUM.
 */
@Converter(autoApply = true)
public class TipoFuenteConverter implements AttributeConverter<TipoFuente, String> {

    @Override
    public String convertToDatabaseColumn(TipoFuente attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public TipoFuente convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return TipoFuente.valueOf(dbData);
    }
}

