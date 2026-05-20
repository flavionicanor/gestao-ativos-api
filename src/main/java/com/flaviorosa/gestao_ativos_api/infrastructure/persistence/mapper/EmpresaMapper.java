package com.flaviorosa.gestao_ativos_api.infrastructure.persistence.mapper;

import com.flaviorosa.gestao_ativos_api.domain.model.Empresa;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.entity.EmpresaEntity;

public class EmpresaMapper {

    private EmpresaMapper() {}

    public static Empresa toDomain(EmpresaEntity entity) {
        if(entity == null) return null;

        return Empresa
                .builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .cnpj(entity.getCnpj())
                .email(entity.getEmail())
                .telefone(entity.getTelefone())
                .ativo(entity.isAtivo())
                .criadoEm(entity.getCriadoEm())
                .atualizadoEm(entity.getAtualizadoEm())
                .build();
    }

    public static EmpresaEntity toEntity(Empresa domain) {
        if(domain == null) return null;

        return EmpresaEntity
                .builder()
                .nome(domain.getNome())
                .cnpj(domain.getCnpj())
                .email(domain.getEmail())
                .telefone(domain.getTelefone())
                .ativo(domain.isAtivo())
                .build();
    }
}
