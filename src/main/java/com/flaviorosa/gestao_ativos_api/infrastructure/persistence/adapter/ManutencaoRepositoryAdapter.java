package com.flaviorosa.gestao_ativos_api.infrastructure.persistence.adapter;

import com.flaviorosa.gestao_ativos_api.domain.model.Manutencao;
import com.flaviorosa.gestao_ativos_api.domain.repository.ManutencaoRepositoryPort;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.entity.AtivoEntity;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.entity.ManutencaoEntity;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.entity.UsuarioEntity;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.mapper.ManutencaoMapper;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.repository.AtivoJpaRepository;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.repository.ManutencaoJpaRepository;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.repository.UsuarioJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Transactional
@RequiredArgsConstructor
public class ManutencaoRepositoryAdapter implements ManutencaoRepositoryPort {

    private final ManutencaoJpaRepository jpaRepository;
    private final AtivoJpaRepository ativoJpaRepository;
    private final UsuarioJpaRepository usuarioJpaRepository;

    @Override
    public Manutencao salvar(Manutencao manutencao) {
        AtivoEntity ativo = ativoJpaRepository
                .findById(manutencao.getAtivo().getId())
                .orElseThrow(() -> new RuntimeException(
                        "Ativo não encontrado: " + manutencao.getAtivo().getId()
                ));

        UsuarioEntity responsavel = usuarioJpaRepository
                .findById(manutencao.getResponsavel().getId())
                .orElseThrow(() -> new RuntimeException(
                        "Responsável não encontrado: " + manutencao.getResponsavel().getId()
                ));

        ManutencaoEntity entity = ManutencaoMapper.toEntity(manutencao);
        if(manutencao.getId() != null) {

            entity = jpaRepository.findById(manutencao.getId())
                    .map(existing -> {
                        existing.setDescricao(manutencao.getDescricao());
                        existing.setDataFim(manutencao.getDataFim());
                        existing.setCusto(manutencao.getCusto());
                        existing.setConcluida(manutencao.isConcluida());
                        existing.setObservacao(manutencao.getObservacao());
                        return existing;
                    })
                    .orElse(entity);

        }

        entity.setAtivo(ativo);
        entity.setResponsavel(responsavel);

        return ManutencaoMapper.toDomain(jpaRepository.save(entity));

    }

    @Override
    public Optional<Manutencao> buscarPorId(UUID id) {
        return jpaRepository.findById(id)
                .map(entity -> ManutencaoMapper.toDomain(entity));
    }

    @Override
    public List<Manutencao> listarPorAtivo(UUID ativoId) {
        return jpaRepository.findAllByAtivoId(ativoId)
                .stream()
                .map(entity -> ManutencaoMapper.toDomain(entity))
                .toList();
    }

    @Override
    public List<Manutencao> listarPorEmpresa(UUID empresaId) {
        return jpaRepository.findAllByAtivoEmpresaId(empresaId)
                .stream()
                .map(ManutencaoMapper::toDomain)
                .toList();
    }

    @Override
    public List<Manutencao> listarEmAndamentoPorEmpresa(UUID empresaId) {
        return jpaRepository.findAllByAtivoEmpresaIdAndConcluidaFalse(empresaId)
                .stream()
                .map(ManutencaoMapper::toDomain)
                .toList();
    }
}
