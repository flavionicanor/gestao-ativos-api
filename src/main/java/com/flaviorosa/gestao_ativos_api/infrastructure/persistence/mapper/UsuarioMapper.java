package com.flaviorosa.gestao_ativos_api.infrastructure.persistence.mapper;

import com.flaviorosa.gestao_ativos_api.domain.model.Usuario;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.entity.UsuarioEntity;

public class UsuarioMapper {

    private UsuarioMapper(){}

    public static Usuario toDomain(UsuarioEntity entity) {
        if(entity ==null) return null;

        return Usuario
                .builder()
                .id(entity.getId())
                .empresa(EmpresaMapper.toDomain(entity.getEmpresa()))
                .nome(entity.getNome())
                .email(entity.getEmail())
                .senha(entity.getSenha())
                .perfil(entity.getPerfil())
                .cargo(entity.getCargo())
                .departamento(entity.getDepartamento())
                .ativo(entity.isAtivo())
                .criadoEm(entity.getCriadoEm())
                .atualizadoEm(entity.getAtualizadoEm())
                .build();
    }

    public static UsuarioEntity toEntity(Usuario domain) {
        if(domain == null) return null;

        return UsuarioEntity
                .builder()
                .empresa(EmpresaMapper.toEntity(domain.getEmpresa()))
                .nome(domain.getNome())
                .email(domain.getEmail())
                .senha(domain.getSenha())
                .perfil(domain.getPerfil())
                .cargo(domain.getCargo())
                .departamento(domain.getDepartamento())
                .ativo(domain.isAtivo())
                .build();
    }
}
