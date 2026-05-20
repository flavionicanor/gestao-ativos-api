package com.flaviorosa.gestao_ativos_api.domain.model;

import com.flaviorosa.gestao_ativos_api.domain.enums.TipoMovimentacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movimentacao {

    private UUID id;
    private Ativo ativo;
    private Usuario colaborador;
    private Usuario responsavel;
    private TipoMovimentacao tipo;
    private LocalDateTime dataMovimentacao;
    private LocalDate dataDevolucaoPrevista;
    private LocalDate dataDevolucaoEfetiva;
    private String observacao;
    private LocalDateTime criadoEm;

}
