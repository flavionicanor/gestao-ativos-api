CREATE TABLE imagens (
                         id              UUID         NOT NULL DEFAULT gen_random_uuid(),
                         ativo_id        UUID         NOT NULL,
                         nome_arquivo    VARCHAR(255) NOT NULL,
                         url             TEXT         NOT NULL,
                         tamanho_bytes   BIGINT,
                         criado_em       TIMESTAMP    NOT NULL,
                         atualizado_em   TIMESTAMP    NOT NULL,

                         CONSTRAINT pk_imagens PRIMARY KEY (id),
                         CONSTRAINT fk_imagens_ativo FOREIGN KEY (ativo_id)
                             REFERENCES ativos (id)
);