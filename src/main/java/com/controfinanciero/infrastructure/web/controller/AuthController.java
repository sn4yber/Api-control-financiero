package com.controfinanciero.infrastructure.web.controller;

import com.controfinanciero.application.usecase.usuario.CrearUsuarioUseCase;
import com.controfinanciero.domain.repository.UsuarioRepository;
import com.controfinanciero.infrastructure.security.JwtUtil;
import com.controfinanciero.infrastructure.web.dto.request.LoginRequest;
import com.controfinanciero.infrastructure.web.dto.request.RegisterRequest;
import com.controfinanciero.infrastructure.web.dto.response.AuthResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador de autenticación (Login/Register)
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;
    private final CrearUsuarioUseCase crearUsuarioUseCase;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );

            var usuario = usuarioRepository.findByUsername(request.username())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            String token = jwtUtil.generateToken(usuario.getId(), usuario.getUsername(), usuario.getEmail());

            return ResponseEntity.ok(AuthResponse.of(
                    token,
                    usuario.getId(),
                    usuario.getUsername(),
                    usuario.getEmail(),
                    usuario.getFullName()
            ));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Credenciales inválidas"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        // Validar si ya existe
        if (usuarioRepository.findByUsername(request.username()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("El username ya está en uso"));
        }
        if (usuarioRepository.findByEmail(request.email()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("El email ya está en uso"));
        }

        // Crear usuario con password encriptado
        var comando = new com.controfinanciero.application.dto.CrearUsuarioCommand(
                request.username(),
                request.email(),
                passwordEncoder.encode(request.password()),
                request.fullName()
        );

        var usuarioDto = crearUsuarioUseCase.ejecutar(comando);

        String token = jwtUtil.generateToken(usuarioDto.id(), usuarioDto.username(), usuarioDto.email());

        return ResponseEntity.status(HttpStatus.CREATED).body(AuthResponse.of(
                token,
                usuarioDto.id(),
                usuarioDto.username(),
                usuarioDto.email(),
                usuarioDto.fullName()
        ));
    }

    record ErrorResponse(String message) {}
}

