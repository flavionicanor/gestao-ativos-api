package com.flaviorosa.gestao_ativos_api.infrastructure.persistence.repository;

import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.entity.EmpresaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmpresaJpaRepository extends JpaRepository<EmpresaEntity, UUID> {

    Optional<EmpresaEntity> findByCnpj(String cnpj);
    List<EmpresaEntity> findAllByAtivoTrue();
    boolean existsByCnpj(String cnpj);

}
