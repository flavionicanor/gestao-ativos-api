package com.flaviorosa.gestao_ativos_api.infrastructure.web.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class TransferirAtivoRequest {

    @NotNull(message = "Novo colaborador é obrigatório")
    private UUID novoColaboradorId;

    @NotNull(message = "Responsável é obrigatório")
    private UUID responsavelId;

    private String observacao;
}
