package com.flaviorosa.gestao_ativos_api.infrastructure.persistence.adapter;


import com.flaviorosa.gestao_ativos_api.domain.model.Empresa;
import com.flaviorosa.gestao_ativos_api.domain.repository.EmpresaRepositoryPort;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.entity.EmpresaEntity;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.mapper.EmpresaMapper;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.repository.EmpresaJpaRepository;
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
public class EmpresaRepositoryAdapter implements EmpresaRepositoryPort {

    private final EmpresaJpaRepository jpaRepository;

    @Override
    public Empresa salvar(Empresa empresa) {
        EmpresaEntity entity = EmpresaMapper.toEntity(empresa);

        if(empresa.getId() != null){
            entity = jpaRepository.findById(empresa.getId())
                    .map(existing -> {
                        existing.setNome(empresa.getNome());
                        existing.setEmail(empresa.getEmail());
                        existing.setTelefone(empresa.getTelefone());
                        existing.setAtivo(empresa.isAtivo());
                        return existing;
                    }).orElse(entity);
        }
        return EmpresaMapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Empresa> buscarPorId(UUID id) {
        return jpaRepository.findById(id)
                .map(EmpresaMapper::toDomain);
    }

    @Override
    public Optional<Empresa> buscarPorCnpj(String cnpj) {
        return jpaRepository.findByCnpj(cnpj)
                .map(EmpresaMapper::toDomain);
    }

    @Override
    public List<Empresa> listarAtivos() {
        return jpaRepository.findAllByAtivoTrue().stream()
                .map(EmpresaMapper::toDomain)
                .toList();
    }

    @Override
    public void deletar(UUID id) {
        jpaRepository.deleteById(id);
    }


    @Override
    public boolean existePorCnpj(String cnpj) {
        return jpaRepository.existsByCnpj(cnpj);
    }
}
