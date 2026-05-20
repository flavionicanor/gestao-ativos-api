package com.flaviorosa.gestao_ativos_api.infrastructure.persistence.adapter;

import com.flaviorosa.gestao_ativos_api.domain.model.Categoria;
import com.flaviorosa.gestao_ativos_api.domain.repository.CategoriaRepositoryPort;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.entity.CategoriaEntity;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.entity.EmpresaEntity;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.mapper.CategoriaMapper;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.repository.CategoriaJpaRepository;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.repository.EmpresaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Transactional
@RequiredArgsConstructor
public class CategoriaRepositoryAdapter implements CategoriaRepositoryPort {

    private final CategoriaJpaRepository jpaRepository;
    private final EmpresaJpaRepository empresaJpaRepository;

    @Override
    public Categoria salvar(Categoria categoria) {
        CategoriaEntity entity;

        if (categoria.getId() != null) {
            entity = jpaRepository.findById(categoria.getId())
                    .map(existing -> {
                        existing.setNome(categoria.getNome());
                        existing.setDescricao(categoria.getDescricao());
                        existing.setAtivo(categoria.isAtivo());
                        return existing;
                    })
                    .orElse(CategoriaMapper.toEntity(categoria));
        } else {
            // Busca a EmpresaEntity gerenciada antes de salvar
            EmpresaEntity empresa = empresaJpaRepository
                    .findById(categoria.getEmpresa().getId())
                    .orElseThrow(() -> new RuntimeException(
                            "Empresa não encontrada: " + categoria.getEmpresa().getId()
                    ));

            entity = CategoriaMapper.toEntity(categoria);
            entity.setEmpresa(empresa);
        }

        return CategoriaMapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Categoria> buscarPorId(UUID id) {
        return jpaRepository.findById(id)
                .map(CategoriaMapper::toDomain);
    }

    @Override
    public List<Categoria> listarPorEmpresa(UUID empresaId) {
        return jpaRepository.findAllByEmpresaId(empresaId)
                .stream()
                .map(CategoriaMapper::toDomain)
                .toList();
    }

    @Override
    public List<Categoria> listarAtivosPorEmpresa(UUID empresaId) {
        return jpaRepository.findAllByEmpresaIdAndAtivoTrue(empresaId)
                .stream()
                .map(CategoriaMapper::toDomain)
                .toList();
    }

    @Override
    public void deletar(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existePorNomeEEmpresa(String nome, UUID empresaId) {
        return jpaRepository.existsByNomeAndEmpresaId(nome, empresaId);
    }
}
