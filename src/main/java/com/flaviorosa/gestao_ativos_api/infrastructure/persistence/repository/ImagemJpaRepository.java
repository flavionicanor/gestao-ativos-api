package com.flaviorosa.gestao_ativos_api.infrastructure.persistence.repository;

import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.entity.ImagemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ImagemJpaRepository extends JpaRepository<ImagemEntity, UUID> {

    List<ImagemEntity> findAllByAtivoId(UUID ativoId);

    @Modifying
    @Query("DELETE FROM ImagemEntity i WHERE i.id = :id")
    void deletarPorId(@Param("id") UUID id);
}
