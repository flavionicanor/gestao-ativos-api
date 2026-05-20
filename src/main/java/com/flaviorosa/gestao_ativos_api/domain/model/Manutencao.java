package com.flaviorosa.gestao_ativos_api.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Manutencao {

    private UUID id;
    private Ativo ativo;
    private Usuario responsavel;
    private String descricao;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private BigDecimal custo;
    private boolean concluida;
    private String observacao;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

}
