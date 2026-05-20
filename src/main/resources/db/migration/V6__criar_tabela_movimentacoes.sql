CREATE TABLE movimentacoes (
                               id                      UUID        NOT NULL DEFAULT gen_random_uuid(),
                               ativo_id                UUID        NOT NULL,
                               colaborador_id          UUID        NOT NULL,
                               responsavel_id          UUID        NOT NULL,
                               tipo                    VARCHAR(20) NOT NULL,
                               data_movimentacao       TIMESTAMP   NOT NULL,
                               data_devolucao_prevista DATE,
                               data_devolucao_efetiva  DATE,
                               observacao              TEXT,
                               criado_em               TIMESTAMP   NOT NULL,
                               atualizado_em           TIMESTAMP   NOT NULL,

                               CONSTRAINT pk_movimentacoes PRIMARY KEY (id),
                               CONSTRAINT fk_movimentacoes_ativo FOREIGN KEY (ativo_id)
                                   REFERENCES ativos (id),
                               CONSTRAINT fk_movimentacoes_colaborador FOREIGN KEY (colaborador_id)
                                   REFERENCES usuarios (id),
                               CONSTRAINT fk_movimentacoes_responsavel FOREIGN KEY (responsavel_id)
                                   REFERENCES usuarios (id)
);