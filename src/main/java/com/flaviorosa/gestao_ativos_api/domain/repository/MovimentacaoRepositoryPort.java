package com.flaviorosa.gestao_ativos_api.domain.repository;

import com.flaviorosa.gestao_ativos_api.domain.model.Movimentacao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MovimentacaoRepositoryPort {

    Movimentacao salvar(Movimentacao movimentacao);
    Optional<Movimentacao> buscarPorId(UUID id);
    Optional<Movimentacao> buscarMovimentacaoAtiva(UUID ativoId);
    List<Movimentacao> listarPorAtivo(UUID ativoId);
    List<Movimentacao> listarPorColaborador(UUID colaboradorId);
    List<Movimentacao> listarPorEmpresa(UUID empresaId);
}
