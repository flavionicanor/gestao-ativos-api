package com.flaviorosa.gestao_ativos_api.application.service;

import com.flaviorosa.gestao_ativos_api.application.port.StoragePort;
import com.flaviorosa.gestao_ativos_api.domain.enums.StatusAtivo;
import com.flaviorosa.gestao_ativos_api.domain.exception.RecursoNaoEncontradoException;
import com.flaviorosa.gestao_ativos_api.domain.exception.RegraDeNegocioException;
import com.flaviorosa.gestao_ativos_api.domain.model.Ativo;
import com.flaviorosa.gestao_ativos_api.domain.model.Categoria;
import com.flaviorosa.gestao_ativos_api.domain.model.Empresa;
import com.flaviorosa.gestao_ativos_api.domain.model.Imagem;
import com.flaviorosa.gestao_ativos_api.domain.repository.AtivoRepositoryPort;
import com.flaviorosa.gestao_ativos_api.domain.repository.ImagemRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AtivoService {

    private final AtivoRepositoryPort ativoRepository;
    private final ImagemRepositoryPort imagemRepository;
    private final EmpresaService empresaService;
    private final CategoriaService categoriaService;
    private final StoragePort storagePort;

    public Ativo criar(Ativo ativo) {
        Empresa empresa = empresaService.buscarPorId(ativo.getEmpresa().getId());
        Categoria categoria = categoriaService.buscarPorId(ativo.getCategoria().getId());

        if(ativo.getNumeroSerie() != null && ativoRepository.existePorNumeroSerie(ativo.getNumeroSerie())) {
            throw new RegraDeNegocioException("Já existe um ativo com o número de série: " + ativo.getNumeroSerie());
        }

        if(ativo.getNumeroPatrimonio() != null
                && ativoRepository.existePorNumeroPatrimonio(ativo.getNumeroPatrimonio())){
            throw new RegraDeNegocioException("Já existe um ativo com o número do pratrimonio: " + ativo.getNumeroPatrimonio());
        }

        ativo.setEmpresa(empresa);
        ativo.setCategoria(categoria);
        ativo.setStatus(StatusAtivo.DISPONIVEL);
        ativo.setCriadoEm(LocalDateTime.now());
        ativo.setAtualizadoEm(LocalDateTime.now());

        return ativoRepository.salvar(ativo);
    }

    public Ativo buscarPorId(UUID id) {
        return ativoRepository.buscarPorId(id)
                .orElseThrow(()-> new RecursoNaoEncontradoException("Ativo", id));
    }

    public List<Ativo> listarPorEmpresa(UUID empresaId){
        empresaService.buscarPorId(empresaId);
        return ativoRepository.listarPorEmpresa(empresaId);
    }

    public List<Ativo> listarPorEmpresaEStatus(UUID empresaId, StatusAtivo status) {
        empresaService.buscarPorId(empresaId);
        return ativoRepository.listarPorEmpresaEStatus(empresaId, status);
    }

    public List<Ativo> listarPorCategoria(UUID categoriaId){
        categoriaService.buscarPorId(categoriaId);
        return ativoRepository.listarPorCategoria(categoriaId);
    }

    public Ativo atualizar(UUID id, Ativo dadosNovos) {
        Ativo ativo = buscarPorId(id);

        if(dadosNovos.getCategoria() != null) {
            Categoria categoria = categoriaService.buscarPorId(dadosNovos.getCategoria().getId());
            ativo.setCategoria(categoria);
        }

        ativo.setNome(dadosNovos.getNome());
        ativo.setDescricao(dadosNovos.getDescricao());
        ativo.setMarca(dadosNovos.getMarca());
        ativo.setModelo(dadosNovos.getModelo());
        ativo.setDataAquisicao(dadosNovos.getDataAquisicao());
        ativo.setValorAquisicao(dadosNovos.getValorAquisicao());
        ativo.setAtualizadoEm(LocalDateTime.now());

        return ativoRepository.salvar(ativo);
    }

    public Imagem adicionarImagem(UUID ativoId, MultipartFile arquivo){
        Ativo ativo = buscarPorId(ativoId);

        String nomeArquivo = UUID.randomUUID()+"_"+arquivo.getOriginalFilename();

        try{
            String url = storagePort.upload(
              nomeArquivo,
              arquivo.getInputStream(),
              arquivo.getSize(),
              arquivo.getContentType()
            );

            Imagem imagem = Imagem.builder()
                    .ativo(ativo)
                    .nomeArquivo(nomeArquivo)
                    .url(url)
                    .tamanhoBytes(arquivo.getSize())
                    .criadoEm(LocalDateTime.now())
                    .build();

            return imagemRepository.salvar(imagem);

        } catch (IOException e) {
            throw new RegraDeNegocioException("Erro ao processar o arquivo de imagem");
        }


    }

    public void removerImagem(UUID ativoId, UUID imagemId ) {
        buscarPorId(ativoId); // só valida se existe o ativo

        Imagem imagem = imagemRepository.buscarPorId(imagemId)
                .orElseThrow(()-> new RecursoNaoEncontradoException("Imagem", imagemId));

        storagePort.deletar(imagem.getNomeArquivo());
        imagemRepository.deletar(imagemId);
    }

    public void deletar(UUID id){
        Ativo ativo = buscarPorId(id);

        if(ativo.getStatus() != StatusAtivo.DISPONIVEL) {
            throw new RegraDeNegocioException("Apenas ativos com status DISPONIVEL podem ser removidos. " +
                    "Status atual: " + ativo.getStatus());
        }

        ativoRepository.deletar(id);
    }

    public Ativo atualizarStatus(UUID id, StatusAtivo novoStatus) {
        Ativo ativo = buscarPorId(id);
        ativo.setStatus(novoStatus);
        ativo.setAtualizadoEm(LocalDateTime.now());
        return ativoRepository.salvar(ativo);
    }
}
