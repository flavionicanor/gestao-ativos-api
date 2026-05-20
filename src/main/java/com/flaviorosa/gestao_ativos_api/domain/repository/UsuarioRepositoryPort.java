package com.flaviorosa.gestao_ativos_api.domain.repository;

import com.flaviorosa.gestao_ativos_api.domain.model.Usuario;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepositoryPort {

    Usuario salvar(Usuario usuario);
    Optional<Usuario> buscarPorId(UUID id);
    Optional<Usuario> buscarPorEmail(String email);
    List<Usuario> listarPorEmpresa(UUID empresaId);
    void deletar(UUID id);
    boolean existePorEmail(String email);

}
