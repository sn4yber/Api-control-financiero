package com.controfinanciero.infrastructure.persistence.repository;

import com.controfinanciero.infrastructure.persistence.entity.RachaAhorroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RachaAhorroRepository extends JpaRepository<RachaAhorroEntity, Long> {

    Optional<RachaAhorroEntity> findByUsuarioId(Long usuarioId);
}

