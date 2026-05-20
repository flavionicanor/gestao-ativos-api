package com.flaviorosa.gestao_ativos_api.infrastructure.web.controller;

import com.flaviorosa.gestao_ativos_api.application.service.EmpresaService;
import com.flaviorosa.gestao_ativos_api.domain.model.Empresa;
import com.flaviorosa.gestao_ativos_api.infrastructure.web.dto.request.EmpresaRequest;
import com.flaviorosa.gestao_ativos_api.infrastructure.web.dto.response.EmpresaResponse;
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
@RequestMapping("/api/empresas")
@RequiredArgsConstructor
@Tag(name = "Empresas", description = "Gerenciamento de Empresas")
@SecurityRequirement(name = "bearerAuth")
public class EmpresaController {

    private final EmpresaService empresaService;

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Cadastra uma nova empresa")
    public ResponseEntity<EmpresaResponse> criar(@Valid @RequestBody EmpresaRequest request) {
        Empresa empresa = toModel(request);
        Empresa criada = empresaService.criar(empresa);

        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(criada));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Busca empresa por ID")
    public ResponseEntity<EmpresaResponse> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(toResponse(empresaService.buscarPorId(id)));
    }

    @GetMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Lista todas as empresas ativas")
    public ResponseEntity<List<EmpresaResponse>> listarAtivos() {
        List<EmpresaResponse> response = empresaService.listarAtivos()
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Atualiza dados de uma empresa")
    public ResponseEntity<EmpresaResponse> atualizar(@PathVariable UUID id, @Valid @RequestBody EmpresaRequest request) {

        Empresa atualizada = empresaService.atualizar(id, toModel(request));
        return ResponseEntity.ok(toResponse(atualizada));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Desativa uma empresa")
    public ResponseEntity<Void> desativar(@PathVariable UUID id) {
        empresaService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    private Empresa toModel(EmpresaRequest request) {
        return Empresa
                .builder()
                .nome(request.getNome())
                .cnpj(request.getCnpj())
                .email(request.getEmail())
                .telefone(request.getTelefone())
                .build();
    }

    private EmpresaResponse toResponse(Empresa empresa) {
        return EmpresaResponse.builder()
                .id(empresa.getId())
                .nome(empresa.getNome())
                .cnpj(empresa.getCnpj())
                .email(empresa.getEmail())
                .telefone(empresa.getTelefone())
                .ativo(empresa.isAtivo())
                .criadoEm(empresa.getCriadoEm())
                .atualizadoEm(empresa.getAtualizadoEm())
                .build();
    }


}
