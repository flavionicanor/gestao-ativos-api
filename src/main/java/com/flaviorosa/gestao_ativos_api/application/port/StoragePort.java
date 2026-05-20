package com.flaviorosa.gestao_ativos_api.application.port;

import java.io.InputStream;

public interface StoragePort {

    /**
     * Faz upload de um arquivo e retorna a URL de acesso permanente.
     */
    String upload(String nomeArquivo, InputStream inputStream, long tamanhoBytes, String contentTypes);

    /**
     * Remove um arquivo do storage pelo nome.
     */
    void deletar(String nomeArquivo);

    /**
     * Gera uma URL temporária de acesso (útil para downloads seguros).
     */
    String geraUrlTemporaria(String nomeArquivo, int expiracaoMinutos);

    /**
     * Verifica se o bucket padrão existe e o cria se necessário.
     * Chamado na inicialização da aplicação.
     */
    void garantirBucketExiste();
}
