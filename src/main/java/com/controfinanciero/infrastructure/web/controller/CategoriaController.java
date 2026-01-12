package com.controfinanciero.infrastructure.web.controller;

import com.controfinanciero.application.dto.CategoriaDTO;
import com.controfinanciero.application.dto.CrearCategoriaCommand;
import com.controfinanciero.application.usecase.categoria.CrearCategoriaUseCase;
import com.controfinanciero.application.usecase.categoria.ObtenerCategoriasUseCase;
import com.controfinanciero.infrastructure.web.dto.request.CrearCategoriaRequest;
import com.controfinanciero.infrastructure.web.dto.response.CategoriaResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller REST para operaciones de Categoría.
 */
@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CrearCategoriaUseCase crearCategoriaUseCase;
    private final ObtenerCategoriasUseCase obtenerCategoriasUseCase;

    public CategoriaController(
            CrearCategoriaUseCase crearCategoriaUseCase,
            ObtenerCategoriasUseCase obtenerCategoriasUseCase
    ) {
        this.crearCategoriaUseCase = crearCategoriaUseCase;
        this.obtenerCategoriasUseCase = obtenerCategoriasUseCase;
    }

    /**
     * POST /api/categorias - Crear una categoría
     */
    @PostMapping
    public ResponseEntity<CategoriaResponse> crearCategoria(
            @Valid @RequestBody CrearCategoriaRequest request
    ) {
        CrearCategoriaCommand command = new CrearCategoriaCommand(
                request.usuarioId(),
                request.nombre(),
                request.descripcion(),
                request.tipoCategoria(),
                request.colorHex(),
                request.icono()
        );

        CategoriaDTO dto = crearCategoriaUseCase.ejecutar(command);
        CategoriaResponse response = mapearAResponse(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/categorias/{id} - Obtener categoría por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> obtenerPorId(@PathVariable Long id) {
        CategoriaDTO dto = obtenerCategoriasUseCase.ejecutarPorId(id);
        CategoriaResponse response = mapearAResponse(dto);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/categorias/usuario/{usuarioId} - Obtener todas las categorías de un usuario
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<CategoriaResponse>> obtenerPorUsuario(@PathVariable Long usuarioId) {
        List<CategoriaDTO> dtos = obtenerCategoriasUseCase.ejecutarPorUsuario(usuarioId);
        List<CategoriaResponse> responses = dtos.stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    /**
     * GET /api/categorias/usuario/{usuarioId}/activas - Obtener categorías activas de un usuario
     */
    @GetMapping("/usuario/{usuarioId}/activas")
    public ResponseEntity<List<CategoriaResponse>> obtenerActivasPorUsuario(@PathVariable Long usuarioId) {
        List<CategoriaDTO> dtos = obtenerCategoriasUseCase.ejecutarActivasPorUsuario(usuarioId);
        List<CategoriaResponse> responses = dtos.stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    private CategoriaResponse mapearAResponse(CategoriaDTO dto) {
        return new CategoriaResponse(
                dto.id(),
                dto.usuarioId(),
                dto.nombre(),
                dto.descripcion(),
                dto.colorHex(),
                dto.icono(),
                dto.tipoCategoria(),
                dto.activa(),
                dto.createdAt(),
                dto.updatedAt()
        );
    }
}









