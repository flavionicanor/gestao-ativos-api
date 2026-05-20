package com.flaviorosa.gestao_ativos_api.application.service;

import com.flaviorosa.gestao_ativos_api.domain.exception.RecursoNaoEncontradoException;
import com.flaviorosa.gestao_ativos_api.domain.exception.RegraDeNegocioException;
import com.flaviorosa.gestao_ativos_api.domain.model.Empresa;
import com.flaviorosa.gestao_ativos_api.domain.repository.EmpresaRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class EmpresaService {

    private final EmpresaRepositoryPort empresaRepository;

    public Empresa criar(Empresa empresa){
        if(empresaRepository.existePorCnpj(empresa.getCnpj())){
            throw new RegraDeNegocioException("Já existe uma empresa cadastrada com esse CNPJ: " + empresa.getCnpj());
        }

        empresa.setAtivo(true);
        empresa.setCriadoEm(LocalDateTime.now());
        empresa.setAtualizadoEm(LocalDateTime.now());

        return empresaRepository.salvar(empresa);
    }

    public Empresa buscarPorId(UUID id){
        return empresaRepository.buscarPorId(id).orElseThrow(
                ()-> new RecursoNaoEncontradoException("Empresa", id));
    }

    public List<Empresa> listarAtivos(){
        return empresaRepository.listarAtivos();
    }

    public Empresa atualizar(UUID id, Empresa dadosNovos){
        Empresa empresa = buscarPorId(id);

        empresa.setNome(dadosNovos.getNome());
        empresa.setEmail(dadosNovos.getEmail());
        empresa.setTelefone(dadosNovos.getTelefone());
        empresa.setAtualizadoEm(LocalDateTime.now());

        return empresaRepository.salvar(empresa);
    }

    public void desativar(UUID id){
        Empresa empresa = buscarPorId(id);
        empresa.setAtivo(false);
        empresa.setAtualizadoEm(LocalDateTime.now());

        empresaRepository.salvar(empresa);
    }
}
