package com.flaviorosa.gestao_ativos_api.infrastructure.web.controller;

import com.flaviorosa.gestao_ativos_api.application.service.CategoriaService;
import com.flaviorosa.gestao_ativos_api.domain.model.Categoria;
import com.flaviorosa.gestao_ativos_api.domain.model.Empresa;
import com.flaviorosa.gestao_ativos_api.infrastructure.web.dto.request.CategoriaRequest;
import com.flaviorosa.gestao_ativos_api.infrastructure.web.dto.response.CategoriaResponse;
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
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
@Tag(name = "Categorias", description = "Gerenciamento de categorias de ativos")
@SecurityRequirement(name = "bearerAuth")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @Operation(summary = "Cadastra uma nova categoria")
    public ResponseEntity<CategoriaResponse> criar(@Valid @RequestBody CategoriaRequest request) {
        Categoria criada = categoriaService.criar(toModel(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(criada));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'GESTOR', 'TECNICO')")
    @Operation(summary = "Busca categoria por ID")
    public ResponseEntity<CategoriaResponse> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(toResponse(categoriaService.buscarPorId(id)));
    }

    @GetMapping("/empresa/{empresaId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'GESTOR', 'TECNICO', 'VISUALIZADOR')")
    @Operation(summary = "Lista todas as categorias da empresa")
    public ResponseEntity<List<CategoriaResponse>> listarPorEmpresa(
            @PathVariable UUID empresaId
    ) {
        List<CategoriaResponse> response = categoriaService.listarPorEmpresa(empresaId)
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/empresa/{empresaId}/ativas")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'GESTOR', 'TECNICO', 'VISUALIZADOR')")
    @Operation(summary = "Lista categorias ativas da empresa")
    public ResponseEntity<List<CategoriaResponse>> listarAtivasPorEmpresa(
            @PathVariable UUID empresaId
    ) {
        List<CategoriaResponse> response = categoriaService.listarAtivoPorEmpresa(empresaId)
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @Operation(summary = "Atualiza uma categoria")
    public ResponseEntity<CategoriaResponse> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody CategoriaRequest request
    ) {
        Categoria atualizada = categoriaService.atualizar(id, toModel(request));
        return ResponseEntity.ok(toResponse(atualizada));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @Operation(summary = "Desativa uma categoria")
    public ResponseEntity<Void> desativar(@PathVariable UUID id) {
        categoriaService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/ativar")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @Operation(summary = "Ativa uma categoria")
    public ResponseEntity<Void> ativar(@PathVariable UUID id) {
        categoriaService.ativar(id);
        return ResponseEntity.noContent().build();
    }

    private Categoria toModel(CategoriaRequest request) {
        return Categoria.builder()
                .empresa(Empresa.builder().id(request.getEmpresaId()).build())
                .nome(request.getNome())
                .descricao(request.getDescricao())
                .build();
    }

    private CategoriaResponse toResponse(Categoria categoria) {
        return CategoriaResponse.builder()
                .id(categoria.getId())
                .empresaId(categoria.getEmpresa().getId())
                .nomeEmpresa(categoria.getEmpresa().getNome())
                .nome(categoria.getNome())
                .descricao(categoria.getDescricao())
                .ativo(categoria.isAtivo())
                .criadoEm(categoria.getCriadoEm())
                .atualizadoEm(categoria.getAtualizadoEm())
                .build();
    }
}
