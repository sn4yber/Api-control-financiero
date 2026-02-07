package com.controfinanciero.infrastructure.persistence.repository;

import com.controfinanciero.infrastructure.persistence.entity.MetaColaboradorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MetaColaboradorRepository extends JpaRepository<MetaColaboradorEntity, Long> {

    List<MetaColaboradorEntity> findByMetaId(Long metaId);

    List<MetaColaboradorEntity> findByUsuarioId(Long usuarioId);

    Optional<MetaColaboradorEntity> findByMetaIdAndUsuarioId(Long metaId, Long usuarioId);

    List<MetaColaboradorEntity> findByMetaIdAndUsuarioIdNot(Long metaId, Long usuarioId);
}

