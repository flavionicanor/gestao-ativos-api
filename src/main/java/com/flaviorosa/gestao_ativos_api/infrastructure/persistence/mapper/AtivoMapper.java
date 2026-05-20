package com.flaviorosa.gestao_ativos_api.infrastructure.persistence.mapper;

import com.flaviorosa.gestao_ativos_api.domain.model.Ativo;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.entity.AtivoEntity;

import java.util.Collections;
import java.util.stream.Collectors;

public class AtivoMapper {

    public static Ativo toDomain(AtivoEntity entity) {
        if (entity == null) return null;

        return Ativo.builder()
                .id(entity.getId())
                .empresa(EmpresaMapper.toDomain(entity.getEmpresa()))
                .categoria(CategoriaMapper.toDomain(entity.getCategoria()))
                .nome(entity.getNome())
                .descricao(entity.getDescricao())
                .numeroSerie(entity.getNumeroSerie())
                .numeroPatrimonio(entity.getNumeroPatrimonio())
                .marca(entity.getMarca())
                .modelo(entity.getModelo())
                .status(entity.getStatus())
                .dataAquisicao(entity.getDataAquisicao())
                .valorAquisicao(entity.getValorAquisicao())
                .imagens(entity.getImagens() == null
                        ? Collections.emptyList()
                        : entity.getImagens().stream()
                        .map(ImagemMapper::toDomain)
                        .collect(Collectors.toList()))
                .criadoEm(entity.getCriadoEm())
                .atualizadoEm(entity.getAtualizadoEm())
                .build();
    }

    public static AtivoEntity toEntity(Ativo domain) {
        if (domain == null) return null;

        return AtivoEntity.builder()
                .empresa(EmpresaMapper.toEntity(domain.getEmpresa()))
                .categoria(CategoriaMapper.toEntity(domain.getCategoria()))
                .nome(domain.getNome())
                .descricao(domain.getDescricao())
                .numeroSerie(domain.getNumeroSerie())
                .numeroPatrimonio(domain.getNumeroPatrimonio())
                .marca(domain.getMarca())
                .modelo(domain.getModelo())
                .status(domain.getStatus())
                .dataAquisicao(domain.getDataAquisicao())
                .valorAquisicao(domain.getValorAquisicao())
                .build();
    }

    private AtivoMapper() {}
}
