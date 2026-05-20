package com.flaviorosa.gestao_ativos_api.infrastructure.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private String email;
    private String perfil;
    private String empresaId;

}
