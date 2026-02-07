package com.controfinanciero.infrastructure.persistence.repository;

import com.controfinanciero.infrastructure.persistence.entity.AhorroAutomaticoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AhorroAutomaticoRepository extends JpaRepository<AhorroAutomaticoEntity, Long> {

    Optional<AhorroAutomaticoEntity> findByUsuarioId(Long usuarioId);
}

