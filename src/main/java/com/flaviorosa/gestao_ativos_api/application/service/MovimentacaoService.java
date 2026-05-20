package com.flaviorosa.gestao_ativos_api.application.service;

import com.flaviorosa.gestao_ativos_api.domain.enums.StatusAtivo;
import com.flaviorosa.gestao_ativos_api.domain.enums.TipoMovimentacao;
import com.flaviorosa.gestao_ativos_api.domain.exception.AtivoNaoDisponivelException;
import com.flaviorosa.gestao_ativos_api.domain.exception.RecursoNaoEncontradoException;
import com.flaviorosa.gestao_ativos_api.domain.exception.RegraDeNegocioException;
import com.flaviorosa.gestao_ativos_api.domain.model.Ativo;
import com.flaviorosa.gestao_ativos_api.domain.model.Movimentacao;
import com.flaviorosa.gestao_ativos_api.domain.model.Usuario;
import com.flaviorosa.gestao_ativos_api.domain.repository.AtivoRepositoryPort;
import com.flaviorosa.gestao_ativos_api.domain.repository.MovimentacaoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class MovimentacaoService {

    private final MovimentacaoRepositoryPort movimentacaoRepository;
    private final AtivoService ativoService;
    private final UsuarioService usuarioService;

    public Movimentacao atribuir(Movimentacao movimentacao) {
        Ativo ativo = ativoService.buscarPorId(movimentacao.getAtivo().getId());
        Usuario colaborador = usuarioService.buscarPorId(movimentacao.getColaborador().getId());
        Usuario responsavel = usuarioService.buscarPorId(movimentacao.getResponsavel().getId());

        if(ativo.getStatus() != StatusAtivo.DISPONIVEL) {
            throw  new AtivoNaoDisponivelException(
                    "O ativo '" + ativo.getNome() + "' não está disponível para atribuição. " +
                    "Status atual: " + ativo.getStatus());
        }

        ativoService.atualizarStatus(ativo.getId(), StatusAtivo.EM_USO);

        movimentacao.setAtivo(ativo);
        movimentacao.setColaborador(colaborador);
        movimentacao.setResponsavel(responsavel);
        movimentacao.setTipo(TipoMovimentacao.ATRIBUICAO);
        movimentacao.setDataMovimentacao(LocalDateTime.now());
        movimentacao.setCriadoEm(LocalDateTime.now());

        return movimentacaoRepository.salvar(movimentacao);
    }

    public Movimentacao devolver(UUID ativoId, UUID responsavelId, String observacao) {
        Ativo ativo = ativoService.buscarPorId(ativoId);
        Usuario responsavel = usuarioService.buscarPorId(responsavelId);

        if(ativo.getStatus() != StatusAtivo.EM_USO) {
            throw new RegraDeNegocioException(
                    "O ativo '" + ativo.getNome() + "' não está em uso. " +
                            "Status atual: " + ativo.getStatus()
            );
        }

        // Fecha a movimentação ativa
        Movimentacao movimentacaoAtiva = movimentacaoRepository
                .buscarMovimentacaoAtiva(ativoId)
                .orElseThrow(()-> new RecursoNaoEncontradoException("Nenhuma movimentação ativa encontrada para o ativo: " + ativoId));

        movimentacaoAtiva.setDataDevolucaoEfetiva(LocalDateTime.now().toLocalDate());
        movimentacaoAtiva.setObservacao(observacao);
        movimentacaoRepository.salvar(movimentacaoAtiva);

        // Atualiza status do ativo
        ativoService.atualizarStatus(ativo.getId(), StatusAtivo.DISPONIVEL);

        // Registra a movimentação de devolução
        Movimentacao devolucao = Movimentacao
                .builder()
                .ativo(ativo)
                .colaborador(movimentacaoAtiva.getColaborador())
                .responsavel(responsavel)
                .tipo(TipoMovimentacao.DEVOLUCAO)
                .dataMovimentacao(LocalDateTime.now())
                .observacao(observacao)
                .criadoEm(LocalDateTime.now())
                .build();

        return movimentacaoRepository.salvar(devolucao);
    }

    public Movimentacao transferir(UUID ativoId, UUID novoColaboradorId,
                                   UUID responsavelId, String observacao) {
        Ativo ativo = ativoService.buscarPorId(ativoId);
        Usuario novoColaborador = usuarioService.buscarPorId(novoColaboradorId);
        Usuario responsavel = usuarioService.buscarPorId(responsavelId);

        if(ativo.getStatus() != StatusAtivo.EM_USO) {
            throw new RegraDeNegocioException(
                    "O ativo '" + ativo.getNome() + "' não está em uso para ser transferido. " +
                            "Status atual: " + ativo.getStatus()
            );
        }

        // Fecha a movimentação ativa
        Movimentacao movimentacaoAtiva = movimentacaoRepository
                .buscarMovimentacaoAtiva(ativoId)
                .orElseThrow(()-> new RecursoNaoEncontradoException("Nenhuma movimentação ativa encontrada para o ativo: " + ativoId));

        movimentacaoAtiva.setDataDevolucaoEfetiva(LocalDateTime.now().toLocalDate());
        movimentacaoAtiva.setObservacao("Transferido para outro colaborador");
        movimentacaoRepository.salvar(movimentacaoAtiva);

        // Abre nova movimentação para o novo colaborador
        Movimentacao transferencia = Movimentacao
                .builder()
                .ativo(ativo)
                .colaborador(novoColaborador)
                .responsavel(responsavel)
                .tipo(TipoMovimentacao.TRANSFERENCIA)
                .dataMovimentacao(LocalDateTime.now())
                .observacao(observacao)
                .criadoEm(LocalDateTime.now())
                .build();

        return movimentacaoRepository.salvar(transferencia);
    }

    public List<Movimentacao> listarPorAtivo(UUID ativoId) {
        ativoService.buscarPorId(ativoId);
        return movimentacaoRepository.listarPorAtivo(ativoId);
    }

    public List<Movimentacao> listarPorColaborador(UUID colaboradorId) {
        usuarioService.buscarPorId(colaboradorId);
        return movimentacaoRepository.listarPorColaborador(colaboradorId);
    }

    public List<Movimentacao> listarPorEmpresa(UUID empresaId) {
        return movimentacaoRepository.listarPorEmpresa(empresaId);
    }


}
