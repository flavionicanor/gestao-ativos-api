package com.flaviorosa.gestao_ativos_api.infrastructure.web.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ImagemResponse {

    private UUID id;
    private String nomeArquivo;
    private String url;
    private Long tamanhoBytes;
    private LocalDateTime criadoEm;
}
