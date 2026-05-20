package com.flaviorosa.gestao_ativos_api.domain.repository;

import com.flaviorosa.gestao_ativos_api.domain.model.Empresa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface EmpresaRepositoryPort {

    Empresa salvar(Empresa empresa);
    Optional<Empresa> buscarPorId(UUID id);
    Optional<Empresa> buscarPorCnpj(String cnpj);
    List<Empresa> listarAtivos();
    void deletar(UUID id);
    boolean existePorCnpj(String cnpj);

}
