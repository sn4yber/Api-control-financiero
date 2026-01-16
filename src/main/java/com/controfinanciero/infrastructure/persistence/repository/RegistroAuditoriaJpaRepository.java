package com.controfinanciero.infrastructure.persistence.repository;

import com.controfinanciero.infrastructure.persistence.entity.RegistroAuditoriaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RegistroAuditoriaJpaRepository extends JpaRepository<RegistroAuditoriaEntity, Long> {

    Page<RegistroAuditoriaEntity> findByUsuarioIdOrderByCreatedAtDesc(Long usuarioId, Pageable pageable);

    List<RegistroAuditoriaEntity> findByTipoEntidadAndEntidadIdOrderByCreatedAtDesc(String tipoEntidad, Long entidadId);

    List<RegistroAuditoriaEntity> findByUsuarioIdAndCreatedAtBetween(Long usuarioId, LocalDateTime inicio, LocalDateTime fin);

    long countByUsuarioId(Long usuarioId);
}

