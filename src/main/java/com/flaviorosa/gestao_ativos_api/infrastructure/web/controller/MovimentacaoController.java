package com.flaviorosa.gestao_ativos_api.infrastructure.web.controller;

import com.flaviorosa.gestao_ativos_api.application.service.MovimentacaoService;
import com.flaviorosa.gestao_ativos_api.domain.model.Ativo;
import com.flaviorosa.gestao_ativos_api.domain.model.Movimentacao;
import com.flaviorosa.gestao_ativos_api.domain.model.Usuario;
import com.flaviorosa.gestao_ativos_api.infrastructure.web.dto.request.AtribuirAtivoRequest;
import com.flaviorosa.gestao_ativos_api.infrastructure.web.dto.request.DevolverAtivoRequest;
import com.flaviorosa.gestao_ativos_api.infrastructure.web.dto.request.TransferirAtivoRequest;
import com.flaviorosa.gestao_ativos_api.infrastructure.web.dto.response.MovimentacaoResponse;
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
@RequestMapping("/api/movimentacoes")
@RequiredArgsConstructor
@Tag(name = "Movimentações", description = "Controle de atribuição e devolução de ativos")
@SecurityRequirement(name = "bearerAuth")
public class MovimentacaoController {

    private final MovimentacaoService movimentacaoService;

    @PostMapping("/atribuir")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'GESTOR')")
    @Operation(summary = "Atribui um ativo a um colaborador")
    public ResponseEntity<MovimentacaoResponse> atribuir(
            @Valid @RequestBody AtribuirAtivoRequest request
    ) {
        Movimentacao movimentacao = Movimentacao.builder()
                .ativo(Ativo.builder().id(request.getAtivoId()).build())
                .colaborador(Usuario.builder().id(request.getColaboradorId()).build())
                .responsavel(Usuario.builder().id(request.getResponsavelId()).build())
                .dataDevolucaoPrevista(request.getDataDevolucaoPrevista())
                .observacao(request.getObservacao())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toResponse(movimentacaoService.atribuir(movimentacao)));
    }

    @PostMapping("/ativos/{ativoId}/devolver")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'GESTOR')")
    @Operation(summary = "Registra a devolução de um ativo")
    public ResponseEntity<MovimentacaoResponse> devolver(
            @PathVariable UUID ativoId,
            @Valid @RequestBody DevolverAtivoRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toResponse(movimentacaoService.devolver(
                        ativoId,
                        request.getResponsavelId(),
                        request.getObservacao()
                )));
    }

    @PostMapping("/ativos/{ativoId}/transferir")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'GESTOR')")
    @Operation(summary = "Transfere um ativo para outro colaborador")
    public ResponseEntity<MovimentacaoResponse> transferir(
            @PathVariable UUID ativoId,
            @Valid @RequestBody TransferirAtivoRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toResponse(movimentacaoService.transferir(
                        ativoId,
                        request.getNovoColaboradorId(),
                        request.getResponsavelId(),
                        request.getObservacao()
                )));
    }

    @GetMapping("/ativos/{ativoId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'GESTOR', 'TECNICO', 'VISUALIZADOR')")
    @Operation(summary = "Lista histórico de movimentações de um ativo")
    public ResponseEntity<List<MovimentacaoResponse>> listarPorAtivo(
            @PathVariable UUID ativoId
    ) {
        return ResponseEntity.ok(
                movimentacaoService.listarPorAtivo(ativoId)
                        .stream().map(this::toResponse).toList()
        );
    }

    @GetMapping("/colaboradores/{colaboradorId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'GESTOR', 'VISUALIZADOR')")
    @Operation(summary = "Lista movimentações de um colaborador")
    public ResponseEntity<List<MovimentacaoResponse>> listarPorColaborador(
            @PathVariable UUID colaboradorId
    ) {
        return ResponseEntity.ok(
                movimentacaoService.listarPorColaborador(colaboradorId)
                        .stream().map(this::toResponse).toList()
        );
    }

    @GetMapping("/empresa/{empresaId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'GESTOR', 'VISUALIZADOR')")
    @Operation(summary = "Lista todas as movimentações da empresa")
    public ResponseEntity<List<MovimentacaoResponse>> listarPorEmpresa(
            @PathVariable UUID empresaId
    ) {
        return ResponseEntity.ok(
                movimentacaoService.listarPorEmpresa(empresaId)
                        .stream().map(this::toResponse).toList()
        );
    }

    private MovimentacaoResponse toResponse(Movimentacao m) {
        return MovimentacaoResponse.builder()
                .id(m.getId())
                .ativoId(m.getAtivo().getId())
                .nomeAtivo(m.getAtivo().getNome())
                .colaboradorId(m.getColaborador().getId())
                .nomeColaborador(m.getColaborador().getNome())
                .responsavelId(m.getResponsavel().getId())
                .nomeResponsavel(m.getResponsavel().getNome())
                .tipo(m.getTipo())
                .dataMovimentacao(m.getDataMovimentacao())
                .dataDevolucaoPrevista(m.getDataDevolucaoPrevista())
                .dataDevolucaoEfetiva(m.getDataDevolucaoEfetiva())
                .observacao(m.getObservacao())
                .criadoEm(m.getCriadoEm())
                .build();
    }
}
