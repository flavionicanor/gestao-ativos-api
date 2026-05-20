package com.flaviorosa.gestao_ativos_api.infrastructure.web.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class CategoriaResponse {

    private UUID id;
    private UUID empresaId;
    private String nomeEmpresa;
    private String nome;
    private String descricao;
    private boolean ativo;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

}
