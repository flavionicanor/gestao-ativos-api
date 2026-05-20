package com.flaviorosa.gestao_ativos_api.domain.enums;

public enum StatusAtivo {
    DISPONIVEL,      // pronto para ser atribuído
    EM_USO,          // com algum colaborador
    EM_MANUTENCAO,   // em reparo ou revisão
    DESCARTADO,      // baixado definitivamente
    EXTRAVIADO       // não localizado
}
