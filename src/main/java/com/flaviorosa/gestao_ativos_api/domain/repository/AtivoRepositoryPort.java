package com.flaviorosa.gestao_ativos_api.domain.repository;

import com.flaviorosa.gestao_ativos_api.domain.enums.StatusAtivo;
import com.flaviorosa.gestao_ativos_api.domain.model.Ativo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AtivoRepositoryPort {

    Ativo salvar(Ativo ativo);
    Optional<Ativo> buscarPorId(UUID id);
    Optional<Ativo> buscarPorNumeroSerie(String numeroSerie);
    Optional<Ativo> buscarPorNumeroPatrimonio(String numeroPatrimonio);
    List<Ativo> listarPorEmpresa(UUID empresaId);
    List<Ativo> listarPorEmpresaEStatus(UUID empresaId, StatusAtivo status);
    List<Ativo> listarPorCategoria(UUID categoriaId);
    void deletar(UUID id);
    boolean existePorNumeroSerie(String numeroSerie);
    boolean existePorNumeroPatrimonio(String numeroPatrimonio);

}
