package com.flaviorosa.gestao_ativos_api.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "imagens")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImagemEntity extends BaseEntity {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ativo_id", nullable = false)
    private AtivoEntity ativo;

    @Column(name = "nome_arquivo", nullable = false)
    private String nomeArquivo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String url;

    @Column(name = "tamanho_bytes")
    private Long tamanhoBytes;

}

