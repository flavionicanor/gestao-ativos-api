package com.flaviorosa.gestao_ativos_api.infrastructure.web.controller;


import com.flaviorosa.gestao_ativos_api.application.service.UsuarioService;
import com.flaviorosa.gestao_ativos_api.domain.model.Empresa;
import com.flaviorosa.gestao_ativos_api.domain.model.Usuario;
import com.flaviorosa.gestao_ativos_api.infrastructure.web.dto.request.AlterarSenhaRequest;
import com.flaviorosa.gestao_ativos_api.infrastructure.web.dto.request.UsuarioRequest;
import com.flaviorosa.gestao_ativos_api.infrastructure.web.dto.response.UsuarioResponse;
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
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Gerenciamento de usuários")
@SecurityRequirement(name = "bearerAuth")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @Operation(summary = "Cadastra um novo usuário")
    public ResponseEntity<UsuarioResponse> criar(@Valid
                                                 @RequestBody UsuarioRequest request) {
        Usuario criado = usuarioService.criar(toModel(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(criado));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'GESTOR')")
    @Operation(summary = "Busca usuário por ID")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(toResponse(usuarioService.buscarPorId(id)));
    }

    @GetMapping("/empresa/{empresaId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'GESTOR')")
    @Operation(summary = "Lista usuários por empresa")
    public ResponseEntity<List<UsuarioResponse>> listarPorEmpresa(@PathVariable UUID empresaId) {
        List<UsuarioResponse> response = usuarioService.listarPorEmpresa(empresaId)
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @Operation(summary = "Atualiza dados de um usuário")
    public ResponseEntity<UsuarioResponse> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody UsuarioRequest request
    ) {
        Usuario atualizado = usuarioService.atualizar(id, toModel(request));
        return ResponseEntity.ok(toResponse(atualizado));
    }

    @PatchMapping("/{id}/senha")
    @Operation(summary = "Altera senha do usuário")
    public ResponseEntity<Void> alterarSenha(
            @PathVariable UUID id,
            @Valid @RequestBody AlterarSenhaRequest request
    ) {
        usuarioService.alterarSenha(id, request.getSenhaAtual(), request.getSenhaNova());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @Operation(summary = "Desativa um usuário")
    public ResponseEntity<Void> desativar(@PathVariable UUID id) {
        usuarioService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/ativar")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @Operation(summary = "Ativa um usuário")
    public ResponseEntity<Void> ativar(@PathVariable UUID id) {
        usuarioService.ativar(id);
        return ResponseEntity.noContent().build();
    }

    private Usuario toModel(UsuarioRequest request) {
        return Usuario.builder()
                .empresa(Empresa.builder().id(request.getEmpresaId()).build())
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(request.getSenha())
                .perfil(request.getPerfil())
                .cargo(request.getCargo())
                .departamento(request.getDepartamento())
                .build();
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .empresaId(usuario.getEmpresa().getId())
                .nomeEmpresa(usuario.getEmpresa().getNome())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .perfil(usuario.getPerfil())
                .cargo(usuario.getCargo())
                .departamento(usuario.getDepartamento())
                .ativo(usuario.isAtivo())
                .criadoEm(usuario.getCriadoEm())
                .atualizadoEm(usuario.getAtualizadoEm())
                .build();
    }

}
