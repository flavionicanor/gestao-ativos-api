package com.flaviorosa.gestao_ativos_api.infrastructure.persistence.mapper;

import com.flaviorosa.gestao_ativos_api.domain.model.Manutencao;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.entity.ManutencaoEntity;

public class ManutencaoMapper {

    public static Manutencao toDomain(ManutencaoEntity entity) {
        if (entity == null) return null;

        return Manutencao.builder()
                .id(entity.getId())
                .ativo(AtivoMapper.toDomain(entity.getAtivo()))
                .responsavel(UsuarioMapper.toDomain(entity.getResponsavel()))
                .descricao(entity.getDescricao())
                .dataInicio(entity.getDataInicio())
                .dataFim(entity.getDataFim())
                .custo(entity.getCusto())
                .concluida(entity.isConcluida())
                .observacao(entity.getObservacao())
                .criadoEm(entity.getCriadoEm())
                .atualizadoEm(entity.getAtualizadoEm())
                .build();
    }

    public static ManutencaoEntity toEntity(Manutencao domain) {
        if (domain == null) return null;

        return ManutencaoEntity.builder()
                .ativo(AtivoMapper.toEntity(domain.getAtivo()))
                .responsavel(UsuarioMapper.toEntity(domain.getResponsavel()))
                .descricao(domain.getDescricao())
                .dataInicio(domain.getDataInicio())
                .dataFim(domain.getDataFim())
                .custo(domain.getCusto())
                .concluida(domain.isConcluida())
                .observacao(domain.getObservacao())
                .build();
    }

    private ManutencaoMapper() {}
}
