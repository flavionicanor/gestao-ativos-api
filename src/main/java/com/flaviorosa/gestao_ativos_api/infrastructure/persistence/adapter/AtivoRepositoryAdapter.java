package com.flaviorosa.gestao_ativos_api.infrastructure.persistence.adapter;

import com.flaviorosa.gestao_ativos_api.domain.enums.StatusAtivo;
import com.flaviorosa.gestao_ativos_api.domain.model.Ativo;
import com.flaviorosa.gestao_ativos_api.domain.repository.AtivoRepositoryPort;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.entity.AtivoEntity;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.entity.CategoriaEntity;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.entity.EmpresaEntity;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.mapper.AtivoMapper;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.repository.AtivoJpaRepository;
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
public class AtivoRepositoryAdapter implements AtivoRepositoryPort {

    private final AtivoJpaRepository jpaRepository;
    private final EmpresaJpaRepository empresaJpaRepository;
    private final CategoriaJpaRepository categoriaJpaRepository;

    @Override
    public Ativo salvar(Ativo ativo) {
        AtivoEntity entity;

        if (ativo.getId() != null) {
            entity = jpaRepository.findById(ativo.getId())
                    .map(existing -> {
                        existing.setNome(ativo.getNome());
                        existing.setDescricao(ativo.getDescricao());
                        existing.setNumeroSerie(ativo.getNumeroSerie());
                        existing.setNumeroPatrimonio(ativo.getNumeroPatrimonio());
                        existing.setMarca(ativo.getMarca());
                        existing.setModelo(ativo.getModelo());
                        existing.setStatus(ativo.getStatus());
                        existing.setDataAquisicao(ativo.getDataAquisicao());
                        existing.setValorAquisicao(ativo.getValorAquisicao());

                        if (ativo.getCategoria() != null) {
                            categoriaJpaRepository
                                    .findById(ativo.getCategoria().getId())
                                    .ifPresent(existing::setCategoria);
                        }
                        return existing;
                    })
                    .orElse(AtivoMapper.toEntity(ativo));
        } else {
            EmpresaEntity empresa = empresaJpaRepository
                    .findById(ativo.getEmpresa().getId())
                    .orElseThrow(() -> new RuntimeException(
                            "Empresa não encontrada: " + ativo.getEmpresa().getId()
                    ));

            CategoriaEntity categoria = categoriaJpaRepository
                    .findById(ativo.getCategoria().getId())
                    .orElseThrow(() -> new RuntimeException(
                            "Categoria não encontrada: " + ativo.getCategoria().getId()
                    ));

            entity = AtivoMapper.toEntity(ativo);
            entity.setEmpresa(empresa);
            entity.setCategoria(categoria);
        }

        return AtivoMapper.toDomain(jpaRepository.save(entity));
    }


    @Override
    public Optional<Ativo> buscarPorId(UUID id) {
        return jpaRepository.findById(id)
                .map(AtivoMapper::toDomain);
    }

    @Override
    public Optional<Ativo> buscarPorNumeroSerie(String numeroSerie) {
        return jpaRepository.findByNumeroSerie(numeroSerie)
                .map(AtivoMapper::toDomain);
    }

    @Override
    public Optional<Ativo> buscarPorNumeroPatrimonio(String numeroPatrimonio) {
        return jpaRepository.findByNumeroPatrimonio(numeroPatrimonio)
                .map(AtivoMapper::toDomain);
    }

    @Override
    public List<Ativo> listarPorEmpresa(UUID empresaId) {
        return jpaRepository.findAllByEmpresaId(empresaId)
                .stream()
                .map(AtivoMapper::toDomain)
                .toList();
    }

    @Override
    public List<Ativo> listarPorEmpresaEStatus(UUID empresaId, StatusAtivo status) {
        return jpaRepository.findAllByEmpresaIdAndStatus(empresaId, status)
                .stream()
                .map(AtivoMapper::toDomain)
                .toList();
    }

    @Override
    public List<Ativo> listarPorCategoria(UUID categoriaId) {
        return jpaRepository.findAllByCategoriaId(categoriaId)
                .stream()
                .map(AtivoMapper::toDomain)
                .toList();
    }

    @Override
    public void deletar(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existePorNumeroSerie(String numeroSerie) {
        return jpaRepository.existsByNumeroSerie(numeroSerie);
    }

    @Override
    public boolean existePorNumeroPatrimonio(String numeroPatrimonio) {
        return jpaRepository.existsByNumeroPatrimonio(numeroPatrimonio);
    }
}
