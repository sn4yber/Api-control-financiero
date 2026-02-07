package com.controfinanciero.infrastructure.web.controller;

import com.controfinanciero.application.dto.CrearUsuarioCommand;
import com.controfinanciero.application.dto.UsuarioDTO;
import com.controfinanciero.application.usecase.usuario.CrearUsuarioUseCase;
import com.controfinanciero.application.usecase.usuario.ObtenerUsuarioUseCase;
import com.controfinanciero.infrastructure.web.dto.request.CrearUsuarioRequest;
import com.controfinanciero.infrastructure.web.dto.response.UsuarioResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST para operaciones de Usuario.
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final CrearUsuarioUseCase crearUsuarioUseCase;
    private final ObtenerUsuarioUseCase obtenerUsuarioUseCase;

    public UsuarioController(
            CrearUsuarioUseCase crearUsuarioUseCase,
            ObtenerUsuarioUseCase obtenerUsuarioUseCase
    ) {
        this.crearUsuarioUseCase = crearUsuarioUseCase;
        this.obtenerUsuarioUseCase = obtenerUsuarioUseCase;
    }

    /**
     * POST /api/usuarios - Crear un nuevo usuario
     */
    @PostMapping
    public ResponseEntity<UsuarioResponse> crearUsuario(@Valid @RequestBody CrearUsuarioRequest request) {
        // Convertir request a command
        CrearUsuarioCommand command = new CrearUsuarioCommand(
                request.username(),
                request.email(),
                request.password(),
                request.fullName()
        );

        // Ejecutar caso de uso
        UsuarioDTO usuarioDTO = crearUsuarioUseCase.ejecutar(command);

        // Convertir DTO a response
        UsuarioResponse response = new UsuarioResponse(
                usuarioDTO.id(),
                usuarioDTO.username(),
                usuarioDTO.email(),
                usuarioDTO.fullName(),
                usuarioDTO.active(),
                usuarioDTO.createdAt()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/usuarios/{id} - Obtener un usuario por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> obtenerUsuario(@PathVariable Long id) {
        UsuarioDTO usuarioDTO = obtenerUsuarioUseCase.ejecutar(id);

        UsuarioResponse response = new UsuarioResponse(
                usuarioDTO.id(),
                usuarioDTO.username(),
                usuarioDTO.email(),
                usuarioDTO.fullName(),
                usuarioDTO.active(),
                usuarioDTO.createdAt()
        );

        return ResponseEntity.ok(response);
    }
}

