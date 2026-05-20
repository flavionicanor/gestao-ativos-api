CREATE TABLE manutencoes (
                             id              UUID         NOT NULL DEFAULT gen_random_uuid(),
                             ativo_id        UUID         NOT NULL,
                             responsavel_id  UUID         NOT NULL,
                             descricao       TEXT         NOT NULL,
                             data_inicio     DATE         NOT NULL,
                             data_fim        DATE,
                             custo           NUMERIC(15, 2),
                             concluida       BOOLEAN      NOT NULL DEFAULT FALSE,
                             observacao      TEXT,
                             criado_em       TIMESTAMP    NOT NULL,
                             atualizado_em   TIMESTAMP    NOT NULL,

                             CONSTRAINT pk_manutencoes PRIMARY KEY (id),
                             CONSTRAINT fk_manutencoes_ativo FOREIGN KEY (ativo_id)
                                 REFERENCES ativos (id),
                             CONSTRAINT fk_manutencoes_responsavel FOREIGN KEY (responsavel_id)
                                 REFERENCES usuarios (id)
);