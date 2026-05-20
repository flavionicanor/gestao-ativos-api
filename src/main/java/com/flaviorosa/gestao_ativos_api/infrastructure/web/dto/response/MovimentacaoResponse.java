package com.flaviorosa.gestao_ativos_api.infrastructure.web.dto.response;

import com.flaviorosa.gestao_ativos_api.domain.enums.TipoMovimentacao;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class MovimentacaoResponse {

    private UUID id;
    private UUID ativoId;
    private String nomeAtivo;
    private UUID colaboradorId;
    private String nomeColaborador;
    private UUID responsavelId;
    private String nomeResponsavel;
    private TipoMovimentacao tipo;
    private LocalDateTime dataMovimentacao;
    private LocalDate dataDevolucaoPrevista;
    private LocalDate dataDevolucaoEfetiva;
    private String observacao;
    private LocalDateTime criadoEm;

}
