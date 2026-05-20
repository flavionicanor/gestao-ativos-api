package com.flaviorosa.gestao_ativos_api.infrastructure.web.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class AtribuirAtivoRequest {

    @NotNull(message = "Ativo é obrigatório")
    private UUID ativoId;

    @NotNull(message = "Colaborador é obrigatório")
    private UUID colaboradorId;

    @NotNull(message = "Responsável é obrigatório")
    private UUID responsavelId;

    private LocalDate dataDevolucaoPrevista;
    private String observacao;
}
