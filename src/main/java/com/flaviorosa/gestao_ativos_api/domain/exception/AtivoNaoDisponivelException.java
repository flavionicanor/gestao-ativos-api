package com.flaviorosa.gestao_ativos_api.domain.exception;

public class AtivoNaoDisponivelException extends RuntimeException {

    public AtivoNaoDisponivelException(String message) {
        super(message);
    }

}
