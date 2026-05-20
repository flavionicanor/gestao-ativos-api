package com.flaviorosa.gestao_ativos_api.infrastructure.persistence.mapper;

import com.flaviorosa.gestao_ativos_api.domain.model.Categoria;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.entity.CategoriaEntity;

public class CategoriaMapper {


    private CategoriaMapper(){}

    public static Categoria toDomain(CategoriaEntity entity) {
        if(entity == null) return null;

        return Categoria
                .builder()
                .id(entity.getId())
                .empresa(EmpresaMapper.toDomain(entity.getEmpresa()))
                .nome(entity.getNome())
                .descricao(entity.getDescricao())
                .ativo(entity.isAtivo())
                .criadoEm(entity.getCriadoEm())
                .atualizadoEm(entity.getAtualizadoEm())
                .build();
    }

    public static CategoriaEntity toEntity(Categoria domain){
        if(domain == null) return null;

        return CategoriaEntity
                .builder()
                .empresa(EmpresaMapper.toEntity(domain.getEmpresa()))
                .nome(domain.getNome())
                .descricao(domain.getDescricao())
                .ativo(domain.isAtivo())
                .build();
    }
}
