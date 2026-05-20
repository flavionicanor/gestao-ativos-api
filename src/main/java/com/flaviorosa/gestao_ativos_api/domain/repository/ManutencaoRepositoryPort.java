package com.flaviorosa.gestao_ativos_api.domain.repository;

import com.flaviorosa.gestao_ativos_api.domain.model.Manutencao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ManutencaoRepositoryPort {

    Manutencao salvar(Manutencao manutencao);
    Optional<Manutencao> buscarPorId(UUID id);
    List<Manutencao> listarPorAtivo(UUID ativoId);
    List<Manutencao> listarPorEmpresa(UUID empresaId);
    List<Manutencao> listarEmAndamentoPorEmpresa(UUID empresaId);

}
