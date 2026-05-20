package com.flaviorosa.gestao_ativos_api.infrastructure.web.controller;

import com.flaviorosa.gestao_ativos_api.domain.model.Usuario;
import com.flaviorosa.gestao_ativos_api.domain.repository.UsuarioRepositoryPort;
import com.flaviorosa.gestao_ativos_api.infrastructure.security.JwtService;
import com.flaviorosa.gestao_ativos_api.infrastructure.web.dto.request.LoginRequest;
import com.flaviorosa.gestao_ativos_api.infrastructure.web.dto.response.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints de autenticação")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UsuarioRepositoryPort usuarioRepository;

    @PostMapping("/login")
    @Operation(summary = "Realiza login e retorna o token JWT")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request){

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        String token = jwtService.gerarToken(userDetails);

        Usuario usuario = usuarioRepository.buscarPorEmail(request.getEmail()).orElseThrow();

        return ResponseEntity.ok(
                new AuthResponse(
                        token,
                        usuario.getEmail(),
                        usuario.getPerfil().name(),
                        usuario.getEmpresa().getId().toString()
                ));
    }

}
