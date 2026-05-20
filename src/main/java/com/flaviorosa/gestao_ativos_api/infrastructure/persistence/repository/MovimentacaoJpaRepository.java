package com.flaviorosa.gestao_ativos_api.infrastructure.persistence.repository;

import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.entity.MovimentacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MovimentacaoJpaRepository extends JpaRepository<MovimentacaoEntity, UUID> {

    List<MovimentacaoEntity> findAllByAtivoId(UUID ativoId);
    List<MovimentacaoEntity> findAllByColaboradorId(UUID colaboradorId);
    List<MovimentacaoEntity> findAllByAtivoEmpresaId(UUID empresaId);

    @Query("""
        SELECT m FROM MovimentacaoEntity m
        WHERE m.ativo.id = :ativoId
        AND m.dataDevolucaoEfetiva IS NULL
        AND m.tipo IN (
            com.flaviorosa.gestao_ativos_api.domain.enums.TipoMovimentacao.ATRIBUICAO,
            com.flaviorosa.gestao_ativos_api.domain.enums.TipoMovimentacao.TRANSFERENCIA
        )
        ORDER BY m.dataMovimentacao DESC
        LIMIT 1
    """)
    Optional<MovimentacaoEntity> findMovimentacaoAtiva(@Param("ativoId") UUID ativoId);
}
