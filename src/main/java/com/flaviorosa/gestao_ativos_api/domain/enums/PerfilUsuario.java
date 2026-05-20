package com.flaviorosa.gestao_ativos_api.domain.enums;

public enum PerfilUsuario {
    SUPER_ADMIN,   // gerencia todas as empresas do sistema
    ADMIN,         // gerencia tudo dentro da sua empresa
    GESTOR,        // atribui e devolve ativos, gera relatórios
    TECNICO,       // registra manutenções
    VISUALIZADOR   // somente leitura
}
