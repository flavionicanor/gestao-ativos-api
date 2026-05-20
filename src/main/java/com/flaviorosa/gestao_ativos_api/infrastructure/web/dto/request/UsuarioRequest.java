package com.flaviorosa.gestao_ativos_api.infrastructure.web.dto.request;


import com.flaviorosa.gestao_ativos_api.domain.enums.PerfilUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class UsuarioRequest {

    @NotNull(message = "Empresa é obrigatória")
    private UUID empresaId;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail inválido")
    private String email;

    private String senha;

    @NotNull(message = "Perfil é obrigatório")
    private PerfilUsuario perfil;

    private String cargo;

    private String departamento;
}
