package com.flaviorosa.gestao_ativos_api.infrastructure.web.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CategoriaRequest {

    @NotNull(message = "Empresa é obrigatória")
    private UUID empresaId;

    @NotNull(message = "Nome é obrigatório")
    private String nome;

    private String descricao;
}
