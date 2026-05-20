CREATE TABLE categorias (
                            id              UUID         NOT NULL DEFAULT gen_random_uuid(),
                            empresa_id      UUID         NOT NULL,
                            nome            VARCHAR(255) NOT NULL,
                            descricao       TEXT,
                            ativo           BOOLEAN      NOT NULL DEFAULT TRUE,
                            criado_em       TIMESTAMP    NOT NULL,
                            atualizado_em   TIMESTAMP    NOT NULL,

                            CONSTRAINT pk_categorias PRIMARY KEY (id),
                            CONSTRAINT fk_categorias_empresa FOREIGN KEY (empresa_id)
                                REFERENCES empresas (id)
);