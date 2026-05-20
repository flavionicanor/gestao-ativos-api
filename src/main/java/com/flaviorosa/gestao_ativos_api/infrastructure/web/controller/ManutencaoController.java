package com.flaviorosa.gestao_ativos_api.infrastructure.web.controller;

import com.flaviorosa.gestao_ativos_api.application.service.ManutencaoService;
import com.flaviorosa.gestao_ativos_api.domain.model.Ativo;
import com.flaviorosa.gestao_ativos_api.domain.model.Manutencao;
import com.flaviorosa.gestao_ativos_api.domain.model.Usuario;
import com.flaviorosa.gestao_ativos_api.infrastructure.web.dto.request.ConcluirManutencaoRequest;
import com.flaviorosa.gestao_ativos_api.infrastructure.web.dto.request.ManutencaoRequest;
import com.flaviorosa.gestao_ativos_api.infrastructure.web.dto.response.ManutencaoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/manutencoes")
@RequiredArgsConstructor
@Tag(name = "Manutenções", description = "Controle de manutenções de ativos")
@SecurityRequirement(name = "bearerAuth")
public class ManutencaoController {

    private final ManutencaoService manutencaoService;

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'GESTOR', 'TECNICO')")
    @Operation(summary = "Registra uma nova manutenção")
    public ResponseEntity<ManutencaoResponse> registrar(
            @Valid @RequestBody ManutencaoRequest request
    ) {
        Manutencao manutencao = Manutencao.builder()
                .ativo(Ativo.builder().id(request.getAtivoId()).build())
                .responsavel(Usuario.builder().id(request.getResponsavelId()).build())
                .descricao(request.getDescricao())
                .custo(request.getCusto())
                .observacao(request.getObservacao())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toResponse(manutencaoService.registrar(manutencao)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'GESTOR', 'TECNICO', 'VISUALIZADOR')")
    @Operation(summary = "Busca manutenção por ID")
    public ResponseEntity<ManutencaoResponse> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(toResponse(manutencaoService.buscarPorId(id)));
    }

    @PatchMapping("/{id}/concluir")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'GESTOR', 'TECNICO')")
    @Operation(summary = "Conclui uma manutenção")
    public ResponseEntity<ManutencaoResponse> concluir(
            @PathVariable UUID id,
            @RequestBody ConcluirManutencaoRequest request
    ) {
        return ResponseEntity.ok(
                toResponse(manutencaoService.concluir(id, request.getObservacao()))
        );
    }

    @GetMapping("/ativos/{ativoId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'GESTOR', 'TECNICO', 'VISUALIZADOR')")
    @Operation(summary = "Lista manutenções de um ativo")
    public ResponseEntity<List<ManutencaoResponse>> listarPorAtivo(
            @PathVariable UUID ativoId
    ) {
        return ResponseEntity.ok(
                manutencaoService.listarPorAtivo(ativoId)
                        .stream().map(this::toResponse).toList()
        );
    }

    @GetMapping("/empresa/{empresaId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'GESTOR', 'VISUALIZADOR')")
    @Operation(summary = "Lista todas as manutenções da empresa")
    public ResponseEntity<List<ManutencaoResponse>> listarPorEmpresa(
            @PathVariable UUID empresaId
    ) {
        return ResponseEntity.ok(
                manutencaoService.listarPorEmpresa(empresaId)
                        .stream().map(this::toResponse).toList()
        );
    }

    @GetMapping("/empresa/{empresaId}/andamento")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'GESTOR', 'TECNICO', 'VISUALIZADOR')")
    @Operation(summary = "Lista manutenções em andamento da empresa")
    public ResponseEntity<List<ManutencaoResponse>> listarEmAndamento(
            @PathVariable UUID empresaId
    ) {
        return ResponseEntity.ok(
                manutencaoService.listarEmAndamentoPorEmpresa(empresaId)
                        .stream().map(this::toResponse).toList()
        );
    }

    private ManutencaoResponse toResponse(Manutencao m) {
        return ManutencaoResponse.builder()
                .id(m.getId())
                .ativoId(m.getAtivo().getId())
                .nomeAtivo(m.getAtivo().getNome())
                .responsavelId(m.getResponsavel().getId())
                .nomeResponsavel(m.getResponsavel().getNome())
                .descricao(m.getDescricao())
                .dataInicio(m.getDataInicio())
                .dataFim(m.getDataFim())
                .custo(m.getCusto())
                .concluida(m.isConcluida())
                .observacao(m.getObservacao())
                .criadoEm(m.getCriadoEm())
                .atualizadoEm(m.getAtualizadoEm())
                .build();
    }
}
