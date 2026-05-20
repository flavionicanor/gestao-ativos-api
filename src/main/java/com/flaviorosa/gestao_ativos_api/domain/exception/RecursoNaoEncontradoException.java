package com.flaviorosa.gestao_ativos_api.domain.exception;

public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String message) {
        super(message);
    }

    public RecursoNaoEncontradoException(String recurso, Object id) {
        super(recurso + " não encontrado com o id: " + id);
    }
}
