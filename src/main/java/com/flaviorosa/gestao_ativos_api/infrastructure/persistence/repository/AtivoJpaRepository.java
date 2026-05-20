package com.flaviorosa.gestao_ativos_api.infrastructure.persistence.repository;

import com.flaviorosa.gestao_ativos_api.domain.enums.StatusAtivo;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.entity.AtivoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AtivoJpaRepository extends JpaRepository<AtivoEntity, UUID> {

    Optional<AtivoEntity> findByNumeroSerie(String numeroSerie);
    Optional<AtivoEntity> findByNumeroPatrimonio(String numeroPatrimonio);
    List<AtivoEntity> findAllByEmpresaId(UUID empresaId);
    List<AtivoEntity> findAllByEmpresaIdAndStatus(UUID empresaId, StatusAtivo status);
    List<AtivoEntity> findAllByCategoriaId(UUID categoriaId);
    boolean existsByNumeroSerie(String numeroSerie);
    boolean existsByNumeroPatrimonio(String numeroPatrimonio);
}