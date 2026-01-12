package com.controfinanciero.application.usecase.usuario;

import com.controfinanciero.application.dto.CrearUsuarioCommand;
import com.controfinanciero.application.dto.UsuarioDTO;
import com.controfinanciero.domain.exception.DomainException;
import com.controfinanciero.domain.model.Usuario;
import com.controfinanciero.domain.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso: Crear un nuevo usuario.
 */
@Service
public class CrearUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    public CrearUsuarioUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public UsuarioDTO ejecutar(CrearUsuarioCommand command) {
        // Validar que no exista el email
        if (usuarioRepository.existsByEmail(command.email())) {
            throw new DomainException("Ya existe un usuario con ese email");
        }

        // Validar que no exista el username
        if (usuarioRepository.existsByUsername(command.username())) {
            throw new DomainException("Ya existe un usuario con ese username");
        }

        // Crear usuario de dominio (aquí deberías hashear la password en producción)
        Usuario usuario = new Usuario(
                command.username(),
                command.email(),
                command.password(), // En producción: BCrypt.hashpw(command.password(), BCrypt.gensalt())
                command.fullName()
        );

        // Guardar
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        // Retornar DTO
        return new UsuarioDTO(
                usuarioGuardado.getId(),
                usuarioGuardado.getUsername(),
                usuarioGuardado.getEmail(),
                usuarioGuardado.getFullName(),
                usuarioGuardado.estaActivo(),
                usuarioGuardado.getCreatedAt()
        );
    }
}

