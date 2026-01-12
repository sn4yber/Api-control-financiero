package com.controfinanciero.application.dto;

import com.controfinanciero.domain.model.enums.TipoFuente;

/**
 * Comando para crear una nueva fuente de ingreso
 */
public record CrearFuenteIngresoCommand(
        Long usuarioId,
        String nombre,
        String descripcion,
        TipoFuente tipoFuente,
        Boolean esIngresoReal,
        Boolean activa
) {
}

