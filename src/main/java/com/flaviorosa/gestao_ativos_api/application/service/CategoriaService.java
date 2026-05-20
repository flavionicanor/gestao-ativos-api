package com.flaviorosa.gestao_ativos_api.application.service;

import com.flaviorosa.gestao_ativos_api.domain.exception.RecursoNaoEncontradoException;
import com.flaviorosa.gestao_ativos_api.domain.exception.RegraDeNegocioException;
import com.flaviorosa.gestao_ativos_api.domain.model.Categoria;
import com.flaviorosa.gestao_ativos_api.domain.model.Empresa;
import com.flaviorosa.gestao_ativos_api.domain.repository.CategoriaRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepositoryPort categoriaRepository;
    private final EmpresaService empresaService;

    public Categoria criar(Categoria categoria){
        Empresa empresa = empresaService.buscarPorId(categoria.getEmpresa().getId());

        if (categoriaRepository.existePorNomeEEmpresa(categoria.getNome(), empresa.getId())){
            throw new RegraDeNegocioException("Já existe uma categoria com o nome '"+ categoria.getNome()+"' nesta empresa");
        }

        categoria.setEmpresa(empresa);
        categoria.setAtivo(true);
        categoria.setCriadoEm(LocalDateTime.now());
        categoria.setAtualizadoEm(LocalDateTime.now());

        return categoriaRepository.salvar(categoria);
    }

    public Categoria buscarPorId(UUID id) {
        return categoriaRepository.buscarPorId(id)
                .orElseThrow(()-> new RecursoNaoEncontradoException("Categoria", id));
    }

    public List<Categoria> listarPorEmpresa(UUID empresaId) {
        empresaService.buscarPorId(empresaId);
        return categoriaRepository.listarPorEmpresa(empresaId);
    }

    public List<Categoria> listarAtivoPorEmpresa(UUID empresaId) {
        empresaService.buscarPorId(empresaId);
        return categoriaRepository.listarAtivosPorEmpresa(empresaId);
    }

    public Categoria atualizar(UUID id, Categoria dadosNovos) {
        Categoria categoria = buscarPorId(id);

        categoria.setNome(dadosNovos.getNome());
        categoria.setDescricao(dadosNovos.getDescricao());
        categoria.setAtualizadoEm(LocalDateTime.now());

        return categoriaRepository.salvar(categoria);
    }

    public void desativar(UUID id) {
        Categoria categoria = buscarPorId(id);
        categoria.setAtivo(false);
        categoria.setAtualizadoEm(LocalDateTime.now());
        categoriaRepository.salvar(categoria);
    }

    public void ativar(UUID id) {
        Categoria categoria = buscarPorId(id);
        categoria.setAtivo(true);
        categoria.setAtualizadoEm(LocalDateTime.now());
        categoriaRepository.salvar(categoria);
    }
}
