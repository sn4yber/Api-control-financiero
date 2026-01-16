package com.controfinanciero.domain.repository;

import com.controfinanciero.infrastructure.persistence.entity.NotificacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<NotificacionEntity, Long> {

    List<NotificacionEntity> findByUsuarioIdOrderByCreatedAtDesc(Long usuarioId);

    List<NotificacionEntity> findByUsuarioIdAndLeidaFalseOrderByCreatedAtDesc(Long usuarioId);

    @Query("SELECT COUNT(n) FROM NotificacionEntity n WHERE n.usuarioId = :usuarioId AND n.leida = false")
    long countNoLeidasByUsuarioId(Long usuarioId);

    void deleteByUsuarioIdAndLeidaTrue(Long usuarioId);
}

