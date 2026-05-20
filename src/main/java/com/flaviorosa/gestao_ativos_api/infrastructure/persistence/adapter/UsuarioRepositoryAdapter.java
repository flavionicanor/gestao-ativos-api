package com.flaviorosa.gestao_ativos_api.infrastructure.persistence.adapter;

import com.flaviorosa.gestao_ativos_api.domain.model.Usuario;
import com.flaviorosa.gestao_ativos_api.domain.repository.UsuarioRepositoryPort;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.entity.EmpresaEntity;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.entity.UsuarioEntity;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.mapper.UsuarioMapper;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.repository.EmpresaJpaRepository;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.repository.UsuarioJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Transactional
@RequiredArgsConstructor
public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {

    private final UsuarioJpaRepository jpaRepository;
    private final EmpresaJpaRepository empresaJpaRepository;

    @Override
    public Usuario salvar(Usuario usuario) {
        UsuarioEntity entity;

        if (usuario.getId() != null) {
            entity = jpaRepository.findById(usuario.getId())
                    .map(existing -> {
                        existing.setNome(usuario.getNome());
                        existing.setEmail(usuario.getEmail());
                        existing.setSenha(usuario.getSenha());
                        existing.setPerfil(usuario.getPerfil());
                        existing.setCargo(usuario.getCargo());
                        existing.setDepartamento(usuario.getDepartamento());
                        existing.setAtivo(usuario.isAtivo());
                        return existing;
                    })
                    .orElse(UsuarioMapper.toEntity(usuario));
        } else {
            // Busca a EmpresaEntity gerenciada pelo JPA antes de salvar
            EmpresaEntity empresa = empresaJpaRepository
                    .findById(usuario.getEmpresa().getId())
                    .orElseThrow(() -> new RuntimeException(
                            "Empresa não encontrada: " + usuario.getEmpresa().getId()
                    ));

            entity = UsuarioMapper.toEntity(usuario);
            entity.setEmpresa(empresa);
        }

        return UsuarioMapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Usuario> buscarPorId(UUID id) {
        return jpaRepository.findById(id)
                .map(UsuarioMapper::toDomain);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(UsuarioMapper::toDomain);
    }

    @Override
    public List<Usuario> listarPorEmpresa(UUID empresaId) {
        return jpaRepository.findAllByEmpresaId(empresaId)
                .stream()
                .map(UsuarioMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deletar(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existePorEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }
}