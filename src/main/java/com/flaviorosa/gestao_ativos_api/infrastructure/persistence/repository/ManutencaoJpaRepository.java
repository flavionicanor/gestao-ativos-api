package com.flaviorosa.gestao_ativos_api.infrastructure.persistence.repository;

import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.entity.ManutencaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ManutencaoJpaRepository extends JpaRepository<ManutencaoEntity, UUID> {

    List<ManutencaoEntity> findAllByAtivoId(UUID ativoId);
    List<ManutencaoEntity> findAllByAtivoEmpresaId(UUID empresaId);
    List<ManutencaoEntity> findAllByAtivoEmpresaIdAndConcluidaFalse(UUID empresaId);
}
