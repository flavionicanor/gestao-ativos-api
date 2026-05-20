package com.flaviorosa.gestao_ativos_api.infrastructure.persistence.adapter;

import com.flaviorosa.gestao_ativos_api.domain.model.Movimentacao;
import com.flaviorosa.gestao_ativos_api.domain.repository.MovimentacaoRepositoryPort;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.entity.AtivoEntity;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.entity.MovimentacaoEntity;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.entity.UsuarioEntity;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.mapper.MovimentacaoMapper;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.repository.AtivoJpaRepository;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.repository.MovimentacaoJpaRepository;
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
public class MovimentacaoRepositoryAdapter implements MovimentacaoRepositoryPort {

    private final MovimentacaoJpaRepository jpaRepository;
    private final AtivoJpaRepository ativoJpaRepository;
    private final UsuarioJpaRepository usuarioJpaRepository;

    @Override
    public Movimentacao salvar(Movimentacao movimentacao) {
        AtivoEntity ativo = ativoJpaRepository.findById(movimentacao.getAtivo().getId())
                .orElseThrow(() -> new RuntimeException(
                        "Ativo não encontrado: " + movimentacao.getAtivo().getId()
                ));

        UsuarioEntity colaborador = usuarioJpaRepository.findById(movimentacao.getColaborador().getId())
                .orElseThrow(() -> new RuntimeException(
                        "Colaborador não encontrado: " + movimentacao.getColaborador().getId()
                ));

        UsuarioEntity responsavel = usuarioJpaRepository.findById(movimentacao.getResponsavel().getId())
                .orElseThrow(() -> new RuntimeException(
                        "Responsável não encontrado: " + movimentacao.getResponsavel().getId()
                ));

        MovimentacaoEntity entity = MovimentacaoMapper.toEntity(movimentacao);

        if(movimentacao.getId() != null){
            entity = jpaRepository.findById(movimentacao.getId())
                    .map(existing ->{
                        existing.setDataDevolucaoEfetiva(movimentacao.getDataDevolucaoEfetiva());
                        existing.setObservacao(movimentacao.getObservacao());
                        return existing;
                    })
                    .orElse(entity);
        }

        entity.setAtivo(ativo);
        entity.setColaborador(colaborador);
        entity.setResponsavel(responsavel);

        return MovimentacaoMapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Movimentacao> buscarPorId(UUID id) {
        return jpaRepository.findById(id)
                .map(MovimentacaoMapper::toDomain);
    }

    @Override
    public Optional<Movimentacao> buscarMovimentacaoAtiva(UUID ativoId) {
        return jpaRepository.findMovimentacaoAtiva(ativoId)
                .map(MovimentacaoMapper::toDomain);
    }

    @Override
    public List<Movimentacao> listarPorAtivo(UUID ativoId) {
        return jpaRepository.findAllByAtivoId(ativoId)
                .stream()
                .map(MovimentacaoMapper::toDomain)
                .toList();
    }

    @Override
    public List<Movimentacao> listarPorColaborador(UUID colaboradorId) {
        return jpaRepository.findAllByColaboradorId(colaboradorId)
                .stream()
                .map(MovimentacaoMapper::toDomain)
                .toList();
    }

    @Override
    public List<Movimentacao> listarPorEmpresa(UUID empresaId) {
        return jpaRepository.findAllByAtivoEmpresaId(empresaId)
                .stream()
                .map(MovimentacaoMapper::toDomain)
                .toList();
    }
}
