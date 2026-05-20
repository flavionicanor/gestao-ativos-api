package com.flaviorosa.gestao_ativos_api.domain.model;

import com.flaviorosa.gestao_ativos_api.domain.enums.StatusAtivo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ativo {

    private UUID id;
    private Empresa empresa;
    private Categoria categoria;
    private String nome;
    private String descricao;
    private String numeroSerie;
    private String numeroPatrimonio;
    private String marca;
    private String modelo;
    private StatusAtivo status;
    private LocalDate dataAquisicao;
    private BigDecimal valorAquisicao;
    private List<Imagem> imagens;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

}
