package com.flaviorosa.gestao_ativos_api.infrastructure.web.dto.response;

import com.flaviorosa.gestao_ativos_api.domain.enums.PerfilUsuario;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class UsuarioResponse {

    private UUID id;
    private UUID empresaId;
    private String nomeEmpresa;
    private String nome;
    private String email;
    private PerfilUsuario perfil;
    private String cargo;
    private String departamento;
    private boolean ativo;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

}
