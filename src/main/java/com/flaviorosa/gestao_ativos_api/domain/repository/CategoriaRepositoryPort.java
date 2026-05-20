package com.flaviorosa.gestao_ativos_api.domain.repository;

import com.flaviorosa.gestao_ativos_api.domain.model.Categoria;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoriaRepositoryPort {

    Categoria salvar(Categoria categoria);
    Optional<Categoria> buscarPorId(UUID id);
    List<Categoria> listarPorEmpresa(UUID empresaId);
    List<Categoria> listarAtivosPorEmpresa(UUID empresaId);
    void deletar(UUID id);
    boolean existePorNomeEEmpresa(String nome, UUID empresaId);
}
