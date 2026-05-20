package com.flaviorosa.gestao_ativos_api.infrastructure.web.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ManutencaoResponse {

    private UUID id;
    private UUID ativoId;
    private String nomeAtivo;
    private UUID responsavelId;
    private String nomeResponsavel;
    private String descricao;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private BigDecimal custo;
    private boolean concluida;
    private String observacao;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
