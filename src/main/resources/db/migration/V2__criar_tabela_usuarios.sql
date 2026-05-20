CREATE TABLE usuarios (
                          id              UUID         NOT NULL DEFAULT gen_random_uuid(),
                          empresa_id      UUID         NOT NULL,
                          nome            VARCHAR(255) NOT NULL,
                          email           VARCHAR(255) NOT NULL,
                          senha           VARCHAR(255) NOT NULL,
                          perfil          VARCHAR(20)  NOT NULL,
                          cargo           VARCHAR(100),
                          departamento    VARCHAR(100),
                          ativo           BOOLEAN      NOT NULL DEFAULT TRUE,
                          criado_em       TIMESTAMP    NOT NULL,
                          atualizado_em   TIMESTAMP    NOT NULL,

                          CONSTRAINT pk_usuarios PRIMARY KEY (id),
                          CONSTRAINT uq_usuarios_email UNIQUE (email),
                          CONSTRAINT fk_usuarios_empresa FOREIGN KEY (empresa_id)
                              REFERENCES empresas (id)
);