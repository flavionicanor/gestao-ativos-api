package com.flaviorosa.gestao_ativos_api.domain.model;

import com.flaviorosa.gestao_ativos_api.domain.enums.PerfilUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    private UUID id;
    private Empresa empresa;
    private String nome;
    private String email;
    private String senha;
    private PerfilUsuario perfil;
    private String cargo;
    private String departamento;
    private boolean ativo;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;


}
