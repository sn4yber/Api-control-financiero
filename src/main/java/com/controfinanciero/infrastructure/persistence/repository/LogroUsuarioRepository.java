package com.controfinanciero.infrastructure.persistence.repository;

import com.controfinanciero.domain.model.enums.TipoLogro;
import com.controfinanciero.infrastructure.persistence.entity.LogroUsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LogroUsuarioRepository extends JpaRepository<LogroUsuarioEntity, Long> {

    List<LogroUsuarioEntity> findByUsuarioId(Long usuarioId);

    Optional<LogroUsuarioEntity> findByUsuarioIdAndTipoLogro(Long usuarioId, TipoLogro tipoLogro);

    List<LogroUsuarioEntity> findByUsuarioIdAndReclamadoFalse(Long usuarioId);

    long countByUsuarioId(Long usuarioId);
}

