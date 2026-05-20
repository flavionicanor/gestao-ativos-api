package com.flaviorosa.gestao_ativos_api.infrastructure.web.controller;

import com.flaviorosa.gestao_ativos_api.application.service.AtivoService;
import com.flaviorosa.gestao_ativos_api.domain.enums.StatusAtivo;
import com.flaviorosa.gestao_ativos_api.domain.model.Ativo;
import com.flaviorosa.gestao_ativos_api.domain.model.Categoria;
import com.flaviorosa.gestao_ativos_api.domain.model.Empresa;
import com.flaviorosa.gestao_ativos_api.infrastructure.web.dto.request.AtivoRequest;
import com.flaviorosa.gestao_ativos_api.infrastructure.web.dto.response.AtivoResponse;
import com.flaviorosa.gestao_ativos_api.infrastructure.web.dto.response.ImagemResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ativos")
@RequiredArgsConstructor
@Tag(name = "Ativos", description = "Gerenciamento de ativos de TI")
@SecurityRequirement(name = "bearerAuth")
public class AtivoController {

    private final AtivoService ativoService;

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'GESTOR')")
    @Operation(summary = "Cadastra um novo ativo")
    public ResponseEntity<AtivoResponse> criar(@Valid @RequestBody AtivoRequest request) {
        Ativo criado = ativoService.criar(toModel(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(criado));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'GESTOR', 'TECNICO', 'VISUALIZADOR')")
    @Operation(summary = "Busca ativo por ID")
    public ResponseEntity<AtivoResponse> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(toResponse(ativoService.buscarPorId(id)));
    }

    @GetMapping("/empresa/{empresaId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'GESTOR', 'TECNICO', 'VISUALIZADOR')")
    @Operation(summary = "Lista ativos por empresa")
    public ResponseEntity<List<AtivoResponse>> listarPorEmpresa(@PathVariable UUID empresaId) {
        return ResponseEntity.ok(
                ativoService.listarPorEmpresa(empresaId)
                        .stream().map(this::toResponse).toList()
        );
    }

    @GetMapping("/empresa/{empresaId}/status/{status}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'GESTOR', 'TECNICO', 'VISUALIZADOR')")
    @Operation(summary = "Lista ativos por empresa e status")
    public ResponseEntity<List<AtivoResponse>> listarPorEmpresaEStatus(
            @PathVariable UUID empresaId,
            @PathVariable StatusAtivo status
    ) {
        return ResponseEntity.ok(
                ativoService.listarPorEmpresaEStatus(empresaId, status)
                        .stream().map(this::toResponse).toList()
        );
    }

    @GetMapping("/categoria/{categoriaId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'GESTOR', 'TECNICO', 'VISUALIZADOR')")
    @Operation(summary = "Lista ativos por categoria")
    public ResponseEntity<List<AtivoResponse>> listarPorCategoria(
            @PathVariable UUID categoriaId
    ) {
        return ResponseEntity.ok(
                ativoService.listarPorCategoria(categoriaId)
                        .stream().map(this::toResponse).toList()
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'GESTOR')")
    @Operation(summary = "Atualiza dados de um ativo")
    public ResponseEntity<AtivoResponse> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody AtivoRequest request
    ) {
        Ativo atualizado = ativoService.atualizar(id, toModel(request));
        return ResponseEntity.ok(toResponse(atualizado));
    }

    @PostMapping(value = "/{id}/imagens",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'GESTOR', 'TECNICO')")
    @Operation(summary = "Adiciona imagem ao ativo")
    public ResponseEntity<ImagemResponse> adicionarImagem(
            @PathVariable UUID id,
            @RequestParam("arquivo") MultipartFile arquivo
    ) {
        var imagem = ativoService.adicionarImagem(id, arquivo);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ImagemResponse.builder()
                        .id(imagem.getId())
                        .nomeArquivo(imagem.getNomeArquivo())
                        .url(imagem.getUrl())
                        .tamanhoBytes(imagem.getTamanhoBytes())
                        .criadoEm(imagem.getCriadoEm())
                        .build()
        );
    }

    @DeleteMapping("/{id}/imagens/{imagemId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'GESTOR')")
    @Operation(summary = "Remove imagem do ativo")
    public ResponseEntity<Void> removerImagem(
            @PathVariable UUID id,
            @PathVariable UUID imagemId
    ) {
        ativoService.removerImagem(id, imagemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @Operation(summary = "Remove um ativo")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        ativoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    private Ativo toModel(AtivoRequest request) {
        return Ativo.builder()
                .empresa(Empresa.builder().id(request.getEmpresaId()).build())
                .categoria(Categoria.builder().id(request.getCategoriaId()).build())
                .nome(request.getNome())
                .descricao(request.getDescricao())
                .numeroSerie(request.getNumeroSerie())
                .numeroPatrimonio(request.getNumeroPatrimonio())
                .marca(request.getMarca())
                .modelo(request.getModelo())
                .dataAquisicao(request.getDataAquisicao())
                .valorAquisicao(request.getValorAquisicao())
                .build();
    }

    private AtivoResponse toResponse(Ativo ativo) {
        return AtivoResponse.builder()
                .id(ativo.getId())
                .empresaId(ativo.getEmpresa().getId())
                .nomeEmpresa(ativo.getEmpresa().getNome())
                .categoriaId(ativo.getCategoria().getId())
                .nomeCategoria(ativo.getCategoria().getNome())
                .nome(ativo.getNome())
                .descricao(ativo.getDescricao())
                .numeroSerie(ativo.getNumeroSerie())
                .numeroPatrimonio(ativo.getNumeroPatrimonio())
                .marca(ativo.getMarca())
                .modelo(ativo.getModelo())
                .status(ativo.getStatus())
                .dataAquisicao(ativo.getDataAquisicao())
                .valorAquisicao(ativo.getValorAquisicao())
                .imagens(ativo.getImagens() == null ? List.of() :
                        ativo.getImagens().stream()
                                .map(img -> ImagemResponse.builder()
                                        .id(img.getId())
                                        .nomeArquivo(img.getNomeArquivo())
                                        .url(img.getUrl())
                                        .tamanhoBytes(img.getTamanhoBytes())
                                        .criadoEm(img.getCriadoEm())
                                        .build())
                                .toList())
                .criadoEm(ativo.getCriadoEm())
                .atualizadoEm(ativo.getAtualizadoEm())
                .build();
    }
}
