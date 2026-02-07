package com.controfinanciero.application.usecase.usuario;

import com.controfinanciero.application.dto.UsuarioDTO;
import com.controfinanciero.domain.exception.UsuarioNoEncontradoException;
import com.controfinanciero.domain.model.Usuario;
import com.controfinanciero.domain.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso: Obtener un usuario por ID.
 */
@Service
@Transactional(readOnly = true)
public class ObtenerUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    public ObtenerUsuarioUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public UsuarioDTO ejecutar(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException(usuarioId));

        return new UsuarioDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getFullName(),
                usuario.estaActivo(),
                usuario.getCreatedAt()
        );
    }
}

