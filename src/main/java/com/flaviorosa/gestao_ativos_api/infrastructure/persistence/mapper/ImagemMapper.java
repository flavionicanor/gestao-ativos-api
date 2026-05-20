package com.flaviorosa.gestao_ativos_api.infrastructure.persistence.mapper;

import com.flaviorosa.gestao_ativos_api.domain.model.Imagem;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.entity.ImagemEntity;

public class ImagemMapper {

    private ImagemMapper(){}

    public static Imagem toDomain(ImagemEntity entity){
        if(entity == null) return null;

        return Imagem
                .builder()
                .id(entity.getId())
                .ativo(null) // evita referência circular — o Ativo já conhece suas Imagens
                .nomeArquivo(entity.getNomeArquivo())
                .url(entity.getUrl())
                .tamanhoBytes(entity.getTamanhoBytes())
                .criadoEm(entity.getCriadoEm())
                .build();
    }

    public static ImagemEntity toEntity(Imagem domain){
        if(domain == null) return null;

        return ImagemEntity
                .builder()
                .nomeArquivo(domain.getNomeArquivo())
                .url(domain.getUrl())
                .tamanhoBytes(domain.getTamanhoBytes())
                .build();
    }
}
