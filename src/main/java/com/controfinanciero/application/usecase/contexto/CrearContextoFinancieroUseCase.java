package com.controfinanciero.application.usecase.contexto;

import com.controfinanciero.application.dto.ContextoFinancieroDTO;
import com.controfinanciero.application.dto.CrearContextoFinancieroCommand;
import com.controfinanciero.domain.exception.UsuarioNoEncontradoException;
import com.controfinanciero.domain.model.ContextoFinanciero;
import com.controfinanciero.domain.repository.ContextoFinancieroRepository;
import com.controfinanciero.domain.repository.UsuarioRepository;

/**
 * Caso de uso: Crear Contexto Financiero.
 * Crea el contexto financiero de un usuario.
 */
public class CrearContextoFinancieroUseCase {

    private final ContextoFinancieroRepository contextoRepository;
    private final UsuarioRepository usuarioRepository;

    public CrearContextoFinancieroUseCase(
            ContextoFinancieroRepository contextoRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.contextoRepository = contextoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public ContextoFinancieroDTO ejecutar(CrearContextoFinancieroCommand command) {
        // Validar que el usuario existe
        usuarioRepository.findById(command.usuarioId())
                .orElseThrow(() -> new UsuarioNoEncontradoException(command.usuarioId()));

        // Validar que no exista ya un contexto para este usuario
        if (contextoRepository.existsByUsuarioId(command.usuarioId())) {
            throw new IllegalStateException("El usuario ya tiene un contexto financiero configurado");
        }

        // Crear el contexto
        ContextoFinanciero contexto = new ContextoFinanciero(
                command.usuarioId(),
                command.tipoIngreso()
        );

        // Configurar ahorro si viene
        if (command.porcentajeAhorroDeseado() != null) {
            contexto.configurarAhorro(command.porcentajeAhorroDeseado());
        }

        // Configurar periodo de análisis
        if (command.periodoAnalisis() != null) {
            contexto.configurarPeriodoAnalisis(
                    command.periodoAnalisis(),
                    command.diasPeriodoPersonalizado()
            );
        }

        // Configurar ingreso variable
        if (command.tieneIngresoVariable() != null) {
            contexto.cambiarTipoIngreso(
                    command.tipoIngreso(),
                    command.tieneIngresoVariable()
            );
        }

        // Guardar
        ContextoFinanciero contextoGuardado = contextoRepository.save(contexto);

        // Convertir a DTO
        return mapearADTO(contextoGuardado);
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

