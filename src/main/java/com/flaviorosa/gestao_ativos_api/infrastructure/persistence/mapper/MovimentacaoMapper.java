package com.flaviorosa.gestao_ativos_api.infrastructure.persistence.mapper;

import com.flaviorosa.gestao_ativos_api.domain.model.Movimentacao;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.entity.MovimentacaoEntity;

public class MovimentacaoMapper {

    public static Movimentacao toDomain(MovimentacaoEntity entity) {
        if (entity == null) return null;

        return Movimentacao.builder()
                .id(entity.getId())
                .ativo(AtivoMapper.toDomain(entity.getAtivo()))
                .colaborador(UsuarioMapper.toDomain(entity.getColaborador()))
                .responsavel(UsuarioMapper.toDomain(entity.getResponsavel()))
                .tipo(entity.getTipo())
                .dataMovimentacao(entity.getDataMovimentacao())
                .dataDevolucaoPrevista(entity.getDataDevolucaoPrevista())
                .dataDevolucaoEfetiva(entity.getDataDevolucaoEfetiva())
                .observacao(entity.getObservacao())
                .criadoEm(entity.getCriadoEm())
                .build();
    }

    public static MovimentacaoEntity toEntity(Movimentacao domain) {
        if (domain == null) return null;

        return MovimentacaoEntity.builder()
                .ativo(AtivoMapper.toEntity(domain.getAtivo()))
                .colaborador(UsuarioMapper.toEntity(domain.getColaborador()))
                .responsavel(UsuarioMapper.toEntity(domain.getResponsavel()))
                .tipo(domain.getTipo())
                .dataMovimentacao(domain.getDataMovimentacao())
                .dataDevolucaoPrevista(domain.getDataDevolucaoPrevista())
                .dataDevolucaoEfetiva(domain.getDataDevolucaoEfetiva())
                .observacao(domain.getObservacao())
                .build();
    }

    private MovimentacaoMapper() {}
}
