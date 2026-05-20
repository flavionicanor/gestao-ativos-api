package com.flaviorosa.gestao_ativos_api.application.service;

import com.flaviorosa.gestao_ativos_api.domain.exception.RecursoNaoEncontradoException;
import com.flaviorosa.gestao_ativos_api.domain.exception.RegraDeNegocioException;
import com.flaviorosa.gestao_ativos_api.domain.model.Empresa;
import com.flaviorosa.gestao_ativos_api.domain.model.Usuario;
import com.flaviorosa.gestao_ativos_api.domain.repository.UsuarioRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepositoryPort usuarioRepository;
    private final EmpresaService empresaService;

    // @Lazy quebra a dependência circular com o SecurityConfig
    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    public Usuario criar(Usuario usuario){
        if(usuarioRepository.existePorEmail(usuario.getEmail())){
            throw new RegraDeNegocioException("Já existe um usuário cadastrado com e-mail: " + usuario.getEmail());
        }

        // Valida senha na criação
        if (usuario.getSenha() == null || usuario.getSenha().isBlank()) {
            throw new RegraDeNegocioException("A senha é obrigatória.");
        }

        // Garante que a empresa existe antes de vincular
        Empresa empresa = empresaService.buscarPorId(usuario.getEmpresa().getId());
        usuario.setEmpresa(empresa);

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuario.setAtivo(true);
        usuario.setCriadoEm(LocalDateTime.now());
        usuario.setAtualizadoEm(LocalDateTime.now());

        return usuarioRepository.salvar(usuario);

    }

    public Usuario buscarPorId(UUID id){
        return usuarioRepository.buscarPorId(id)
                .orElseThrow(()-> new RecursoNaoEncontradoException("Usuário", id));
    }

    public Usuario buscarPorEmail(String email){
        return usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Usuário não encontrado com e-mail: " + email
                ));
    }

    public List<Usuario> listarPorEmpresa(UUID empresaId){
        empresaService.buscarPorId(empresaId); // valida que a empresa existe

        return usuarioRepository.listarPorEmpresa(empresaId);
    }

    public Usuario atualizar(UUID id, Usuario dadosNovos){
        Usuario usuario = buscarPorId(id);

        usuario.setNome(dadosNovos.getNome());
        usuario.setCargo(dadosNovos.getCargo());
        usuario.setDepartamento(dadosNovos.getDepartamento());
        usuario.setPerfil(dadosNovos.getPerfil());
        usuario.setAtualizadoEm(LocalDateTime.now());

        return usuarioRepository.salvar(usuario);
    }

    public void alterarSenha(UUID id, String senhaAtual, String senhaNova){
        Usuario usuario = buscarPorId(id);

        if(!passwordEncoder.matches(senhaAtual, usuario.getSenha())){
            throw new RegraDeNegocioException("Senha atual incorreta.");
        }

        usuario.setSenha(passwordEncoder.encode(senhaNova));
        usuario.setAtualizadoEm(LocalDateTime.now());

        usuarioRepository.salvar(usuario);
    }

    public void desativar(UUID id){
        Usuario usuario = buscarPorId(id);

        usuario.setAtivo(false);
        usuario.setAtualizadoEm(LocalDateTime.now());
        usuarioRepository.salvar(usuario);
    }

    public void ativar(UUID id) {
        Usuario usuario = buscarPorId(id);
        usuario.setAtivo(true);
        usuario.setAtualizadoEm(LocalDateTime.now());
        usuarioRepository.salvar(usuario);
    }
}
