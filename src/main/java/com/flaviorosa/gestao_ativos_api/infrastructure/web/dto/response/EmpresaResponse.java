package com.flaviorosa.gestao_ativos_api.infrastructure.web.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class EmpresaResponse {

    private UUID id;
    private String nome;
    private String cnpj;
    private String email;
    private String telefone;
    private boolean ativo;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

}
