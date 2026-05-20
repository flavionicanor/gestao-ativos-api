package com.flaviorosa.gestao_ativos_api.infrastructure.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ManutencaoRequest {

    @NotNull(message = "Ativo é obrigatório")
    private UUID ativoId;

    @NotNull(message = "Responsável é obrigatório")
    private UUID responsavelId;

    @NotBlank(message = "Descricao é obrigatória")
    private String descricao;

    private BigDecimal custo;
    private String observacao;
}
