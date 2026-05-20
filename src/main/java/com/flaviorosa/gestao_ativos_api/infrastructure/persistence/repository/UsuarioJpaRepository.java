package com.flaviorosa.gestao_ativos_api.infrastructure.persistence.repository;

import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity, UUID> {

    Optional<UsuarioEntity> findByEmail(String email);
    List<UsuarioEntity> findAllByEmpresaId(UUID empresaId);
    boolean existsByEmail(String email);

}
