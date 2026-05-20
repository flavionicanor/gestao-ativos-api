package com.flaviorosa.gestao_ativos_api.infrastructure.persistence.adapter;

import com.flaviorosa.gestao_ativos_api.domain.exception.RegraDeNegocioException;
import com.flaviorosa.gestao_ativos_api.domain.model.Imagem;
import com.flaviorosa.gestao_ativos_api.domain.repository.ImagemRepositoryPort;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.entity.AtivoEntity;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.entity.ImagemEntity;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.mapper.ImagemMapper;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.repository.AtivoJpaRepository;
import com.flaviorosa.gestao_ativos_api.infrastructure.persistence.repository.ImagemJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Transactional
@RequiredArgsConstructor
public class ImagemRepositoryAdapter implements ImagemRepositoryPort {

    private final ImagemJpaRepository jpaRepository;
    private final AtivoJpaRepository ativoJpaRepository;

    @Override
    public Imagem salvar(Imagem imagem) {
        AtivoEntity ativoEntity = ativoJpaRepository.findById(imagem.getAtivo().getId())
                .orElseThrow(()-> new RegraDeNegocioException("Ativo não encontrado ao salvar imagem: " + imagem.getAtivo().getId()));

        ImagemEntity imagemEntity = ImagemMapper.toEntity(imagem);
        imagemEntity.setAtivo(ativoEntity);

        return ImagemMapper.toDomain(jpaRepository.save(imagemEntity));
    }

    @Override
    public Optional<Imagem> buscarPorId(UUID id) {
        return jpaRepository.findById(id)
                .map(ImagemMapper::toDomain);
    }

    @Override
    public List<Imagem> listarPorAtivo(UUID ativoId) {
        return jpaRepository.findAllByAtivoId(ativoId)
                .stream()
                .map(ImagemMapper::toDomain)
                .toList();
    }

    @Override
    public void deletar(UUID id) {
        jpaRepository.deletarPorId(id);
    }
}
