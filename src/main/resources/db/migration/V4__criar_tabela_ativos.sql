CREATE TABLE ativos (
                        id                  UUID            NOT NULL DEFAULT gen_random_uuid(),
                        empresa_id          UUID            NOT NULL,
                        categoria_id        UUID            NOT NULL,
                        nome                VARCHAR(255)    NOT NULL,
                        descricao           TEXT,
                        numero_serie        VARCHAR(100),
                        numero_patrimonio   VARCHAR(100),
                        marca               VARCHAR(100),
                        modelo              VARCHAR(100),
                        status              VARCHAR(20)     NOT NULL DEFAULT 'DISPONIVEL',
                        data_aquisicao      DATE,
                        valor_aquisicao     NUMERIC(15, 2),
                        criado_em           TIMESTAMP       NOT NULL,
                        atualizado_em       TIMESTAMP       NOT NULL,

                        CONSTRAINT pk_ativos PRIMARY KEY (id),
                        CONSTRAINT uq_ativos_numero_serie UNIQUE (numero_serie),
                        CONSTRAINT uq_ativos_numero_patrimonio UNIQUE (numero_patrimonio),
                        CONSTRAINT fk_ativos_empresa FOREIGN KEY (empresa_id)
                            REFERENCES empresas (id),
                        CONSTRAINT fk_ativos_categoria FOREIGN KEY (categoria_id)
                            REFERENCES categorias (id)
);