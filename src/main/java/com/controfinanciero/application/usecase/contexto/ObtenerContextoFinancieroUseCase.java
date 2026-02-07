package com.controfinanciero.application.usecase.contexto;

import com.controfinanciero.application.dto.ContextoFinancieroDTO;
import com.controfinanciero.domain.model.ContextoFinanciero;
import com.controfinanciero.domain.repository.ContextoFinancieroRepository;

/**
 * Caso de uso: Obtener Contexto Financiero.
 * Obtiene el contexto financiero de un usuario.
 */
public class ObtenerContextoFinancieroUseCase {

    private final ContextoFinancieroRepository contextoRepository;

    public ObtenerContextoFinancieroUseCase(ContextoFinancieroRepository contextoRepository) {
        this.contextoRepository = contextoRepository;
    }

    public ContextoFinancieroDTO ejecutarPorUsuarioId(Long usuarioId) {
        ContextoFinanciero contexto = contextoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("No existe contexto financiero para el usuario " + usuarioId));

        return mapearADTO(contexto);
    }

    public ContextoFinancieroDTO ejecutarPorId(Long id) {
        ContextoFinanciero contexto = contextoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe contexto financiero con ID " + id));

        return mapearADTO(contexto);
    }

    private ContextoFinancieroDTO mapearADTO(ContextoFinanciero contexto) {
        return new ContextoFinancieroDTO(
                contexto.getId(),
                contexto.getUsuarioId(),
                contexto.getTipoIngreso(),
                contexto.isTieneIngresoVariable(),
                contexto.getPorcentajeAhorroDeseado(),
                contexto.getPeriodoAnalisis(),
                contexto.getDiasPeriodoPersonalizado(),
                contexto.getCodigoMoneda(),
                contexto.getCreatedAt(),
                contexto.getUpdatedAt()
        );
    }
}

