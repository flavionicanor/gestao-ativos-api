CREATE TABLE empresas (
                          id               UUID         NOT NULL DEFAULT gen_random_uuid(),
                          nome             VARCHAR(255) NOT NULL,
                          cnpj             VARCHAR(18)  NOT NULL,
                          email            VARCHAR(255) NOT NULL,
                          telefone         VARCHAR(20),
                          ativo            BOOLEAN      NOT NULL DEFAULT TRUE,
                          criado_em        TIMESTAMP    NOT NULL,
                          atualizado_em    TIMESTAMP    NOT NULL,

                          CONSTRAINT pk_empresas PRIMARY KEY (id),
                          CONSTRAINT uq_empresas_cnpj UNIQUE (cnpj)
);