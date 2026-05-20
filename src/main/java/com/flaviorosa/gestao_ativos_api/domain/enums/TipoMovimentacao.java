package com.flaviorosa.gestao_ativos_api.domain.enums;

public enum TipoMovimentacao {
    ATRIBUICAO,    // ativo entregue a um colaborador
    DEVOLUCAO,     // ativo devolvido, volta para DISPONIVEL
    TRANSFERENCIA  // troca direta de um colaborador para outro
}
