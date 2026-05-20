package com.flaviorosa.gestao_ativos_api.application.service;

import com.flaviorosa.gestao_ativos_api.domain.enums.StatusAtivo;
import com.flaviorosa.gestao_ativos_api.domain.exception.RecursoNaoEncontradoException;
import com.flaviorosa.gestao_ativos_api.domain.exception.RegraDeNegocioException;
import com.flaviorosa.gestao_ativos_api.domain.model.Ativo;
import com.flaviorosa.gestao_ativos_api.domain.model.Manutencao;
import com.flaviorosa.gestao_ativos_api.domain.model.Usuario;
import com.flaviorosa.gestao_ativos_api.domain.repository.AtivoRepositoryPort;
import com.flaviorosa.gestao_ativos_api.domain.repository.ManutencaoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ManutencaoService {

    private final ManutencaoRepositoryPort manutencaoRepository;
    private final AtivoService ativoService;
    private final UsuarioService usuarioService;

    public Manutencao registrar(Manutencao manutencao) {
        Ativo ativo = ativoService.buscarPorId(manutencao.getAtivo().getId());
        Usuario responsavel = usuarioService.buscarPorId(manutencao.getResponsavel().getId());

        if(ativo.getStatus() == StatusAtivo.DESCARTADO ||
            ativo.getStatus() == StatusAtivo.EXTRAVIADO){
            throw new RegraDeNegocioException(
                    "Não é possível registrar manutenção para um ativo com status: "
                            + ativo.getStatus()
            );
        }

        if(ativo.getStatus() == StatusAtivo.EM_MANUTENCAO) {
            throw new RegraDeNegocioException(
                    "O ativo '" + ativo.getNome() + "' já está em manutenção."
            );
        }

        ativoService.atualizarStatus(ativo.getId(), StatusAtivo.EM_MANUTENCAO);

        manutencao.setAtivo(ativo);
        manutencao.setResponsavel(responsavel);
        manutencao.setDataInicio(LocalDate.now());
        manutencao.setConcluida(false);
        manutencao.setCriadoEm(LocalDateTime.now());
        manutencao.setAtualizadoEm(LocalDateTime.now());

        return manutencaoRepository.salvar(manutencao);
    }

    public Manutencao buscarPorId(UUID id){
        return manutencaoRepository.buscarPorId(id).orElseThrow(()-> new RecursoNaoEncontradoException("Manutenção", id));
    }

    public Manutencao concluir(UUID id, String observacao) {
        Manutencao manutencao = buscarPorId(id);

        if(manutencao.isConcluida()){
            throw new RegraDeNegocioException("Esta manutenção ja foi concluida");
        }

        manutencao.setConcluida(true);
        manutencao.setDataFim(LocalDate.now());
        manutencao.setObservacao(observacao);
        manutencao.setAtualizadoEm(LocalDateTime.now());
        manutencaoRepository.salvar(manutencao);

        ativoService.atualizarStatus(
                manutencao.getAtivo().getId(), StatusAtivo.DISPONIVEL
        );

        return manutencao;
    }

    public List<Manutencao> listarPorAtivo(UUID ativoId) {
        ativoService.buscarPorId(ativoId);
        return manutencaoRepository.listarPorAtivo(ativoId);
    }

    public List<Manutencao> listarPorEmpresa(UUID empresaId){
        return manutencaoRepository.listarPorEmpresa(empresaId);
    }

    public List<Manutencao> listarEmAndamentoPorEmpresa(UUID empresaId) {
        return manutencaoRepository.listarEmAndamentoPorEmpresa(empresaId);
    }

}
