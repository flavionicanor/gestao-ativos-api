package com.flaviorosa.gestao_ativos_api.infrastructure.web.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class DevolverAtivoRequest {

    @NotNull(message = "Responsável é obrigatório")
    private UUID responsavelId;

    private String Observacao;
}
