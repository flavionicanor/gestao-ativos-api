package com.flaviorosa.gestao_ativos_api.infrastructure.web.dto.response;

import com.flaviorosa.gestao_ativos_api.domain.enums.StatusAtivo;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class AtivoResponse {

    private UUID id;
    private UUID empresaId;
    private String nomeEmpresa;
    private UUID categoriaId;
    private String nomeCategoria;
    private String nome;
    private String descricao;
    private String numeroSerie;
    private String numeroPatrimonio;
    private String marca;
    private String modelo;
    private StatusAtivo status;
    private LocalDate dataAquisicao;
    private BigDecimal valorAquisicao;
    private List<ImagemResponse> imagens;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

}
