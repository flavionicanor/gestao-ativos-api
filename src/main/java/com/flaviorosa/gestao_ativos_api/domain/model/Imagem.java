package com.flaviorosa.gestao_ativos_api.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Imagem {

    private UUID id;
    private Ativo ativo;
    private String nomeArquivo;
    private String url;
    private Long tamanhoBytes;
    private LocalDateTime criadoEm;


}
