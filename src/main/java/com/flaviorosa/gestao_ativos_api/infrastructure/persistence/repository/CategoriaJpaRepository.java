package com.flaviorosa.gestao_ativos_api.infrastructure.persistence.repository;

import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.entity.CategoriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoriaJpaRepository extends JpaRepository<CategoriaEntity, UUID> {

    List<CategoriaEntity> findAllByEmpresaId(UUID empresaId);
    List<CategoriaEntity> findAllByEmpresaIdAndAtivoTrue(UUID empresaId);
    boolean existsByNomeAndEmpresaId(String nome, UUID empresaId);


}
