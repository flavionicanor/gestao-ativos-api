package com.flaviorosa.gestao_ativos_api.infrastructure.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class AtivoRequest {

    @NotNull(message = "Empresa é obrigatório")
    private UUID empresaId;

    @NotNull(message = "Categoria é obrigatório")
    private UUID categoriaId;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    private String descricao;
    private String numeroSerie;
    private String numeroPatrimonio;
    private String marca;
    private String modelo;
    private LocalDate dataAquisicao;
    private BigDecimal valorAquisicao;
}
