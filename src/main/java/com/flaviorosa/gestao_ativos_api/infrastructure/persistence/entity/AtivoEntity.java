package com.flaviorosa.gestao_ativos_api.infrastructure.persistence.entity;

import com.flaviorosa.gestao_ativos_api.domain.enums.StatusAtivo;
import jakarta.persistence.*;
import lombok.*;
import org.checkerframework.checker.units.qual.C;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ativos")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AtivoEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private EmpresaEntity empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private CategoriaEntity categoria;

    @Column(nullable = false)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "numero_serie", unique = true)
    private String numeroSerie;

    @Column(name = "numero_patrimonio")
    private String numeroPatrimonio;

    private String marca;

    private String modelo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusAtivo status;

    @Column(name = "data_aquisicao")
    private LocalDate dataAquisicao;

    @Column(name = "valor_aquisicao", precision = 15, scale = 2)
    private BigDecimal valorAquisicao;

    @OneToMany(mappedBy = "ativo", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ImagemEntity> imagens = new ArrayList<>();

}

