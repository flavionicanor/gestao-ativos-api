package com.flaviorosa.gestao_ativos_api.domain.repository;

import com.flaviorosa.gestao_ativos_api.domain.model.Imagem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ImagemRepositoryPort {

    Imagem salvar(Imagem imagem);
    Optional<Imagem> buscarPorId(UUID id);
    List<Imagem> listarPorAtivo(UUID ativoId);
    void deletar(UUID id);

}
