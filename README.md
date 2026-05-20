# 🖥️ Gestão de Ativos de TI — Backend

API REST para gerenciamento de ativos de TI com controle por empresa, autenticação JWT e armazenamento de imagens via MinIO.

---

## 🏗️ Arquitetura

O projeto segue os princípios da **Clean Architecture**, organizado em camadas:

domain/          → modelos, regras de negócio, ports (interfaces)
application/     → services, casos de uso
infrastructure/  → JPA, adapters, segurança, MinIO, controllers

---

## 🚀 Tecnologias

| Tecnologia       | Versão  | Uso                        |
|------------------|---------|----------------------------|
| Java             | 21      | Linguagem                  |
| Spring Boot      | 3.4.5   | Framework principal        |
| PostgreSQL       | 16      | Banco de dados             |
| Flyway           | —       | Migrations do banco        |
| Spring Security  | —       | Autenticação e autorização |
| JWT (jjwt)       | 0.12.3  | Tokens de acesso           |
| MinIO            | —       | Armazenamento de imagens   |
| Swagger/OpenAPI  | 2.8.8   | Documentação da API        |
| Docker           | —       | Containerização            |

---

## 📋 Pré-requisitos

- [Docker](https://www.docker.com/) e Docker Compose instalados
- Ou: Java 21 + Maven para rodar localmente sem Docker

---

## ▶️ Rodando com Docker (recomendado)

```bash
# Clone o repositório
git clone https://github.com/flavionicanor/gestao-ativos-api.git
cd gestao-ativos-api

# Sobe todos os serviços (PostgreSQL + MinIO + API)
docker-compose up --build
```

Aguarde até ver no log:
Started GestaoAtivosApiApplication in X seconds

| Serviço        | URL                          |
|----------------|------------------------------|
| API REST       | http://localhost:8080        |
| Swagger UI     | http://localhost:8080/swagger-ui.html |
| MinIO Console  | http://localhost:9001        |

---

## 💻 Rodando localmente (sem Docker na API)

Requisito: PostgreSQL e MinIO rodando (via Docker Compose parcial).

```bash
# Sobe apenas os serviços de infraestrutura
docker-compose up postgres minio -d

# Roda a aplicação pelo Maven
./mvnw spring-boot:run
```

---

## 🔐 Acesso inicial

Após subir a aplicação, um usuário **SUPER_ADMIN** é necessário para o primeiro acesso.

Execute no banco de dados:

```sql
-- Substitua o hash pela senha desejada (BCrypt)
INSERT INTO empresas (id, nome, cnpj, email, ativo, criado_em, atualizado_em)
VALUES (gen_random_uuid(), 'Empresa Padrão', '00.000.000/0001-00',
        'contato@empresa.com', true, NOW(), NOW());

INSERT INTO usuarios (id, empresa_id, nome, email, senha, perfil, ativo, criado_em, atualizado_em)
VALUES (
  gen_random_uuid(),
  (SELECT id FROM empresas WHERE cnpj = '00.000.000/0001-00'),
  'Administrador',
  'admin@admin.com',
  '$2a$12$R6v4HNaCRsd6HSaXqav1EO.dDUM2sF4JWxe6FpcAR8ZWD7SeQIyDO',
  'SUPER_ADMIN', true, NOW(), NOW()
);
-- Senha padrão: admin123
```

---

## 🌍 Variáveis de ambiente

| Variável          | Padrão                  | Descrição                  |
|-------------------|-------------------------|----------------------------|
| `DB_USERNAME`     | `postgres`              | Usuário do banco           |
| `DB_PASSWORD`     | `postgres`              | Senha do banco             |
| `MINIO_ACCESS_KEY`| `minioadmin`            | Usuário do MinIO           |
| `MINIO_SECRET_KEY`| `minioadmin`            | Senha do MinIO             |
| `MINIO_BUCKET`    | `ativos`                | Nome do bucket             |
| `JWT_SECRET`      | valor padrão (base64)   | Chave secreta do JWT       |
| `JWT_EXPIRATION`  | `86400000`              | Expiração do token (ms)    |

---

## 📁 Perfis de execução

| Perfil    | Arquivo                    | Uso                        |
|-----------|----------------------------|----------------------------|
| padrão    | `application.yml`          | Desenvolvimento local      |
| `docker`  | `application-docker.yml`   | Execução em container      |

---

## 👥 Perfis de usuário

| Perfil        | Permissões                                      |
|---------------|-------------------------------------------------|
| SUPER_ADMIN   | Acesso total + gerencia todas as empresas       |
| ADMIN         | Gerencia usuários, ativos, movimentações        |
| GESTOR        | Atribui/devolve ativos, registra manutenções    |
| TECNICO       | Registra e conclui manutenções                  |
| VISUALIZADOR  | Somente leitura                                 |

---

## 📖 Documentação da API

Com a aplicação rodando, acesse:

http://localhost:8080/swagger-ui.html

---

## 🗄️ Estrutura do banco

O schema é gerenciado pelo **Flyway**. As migrations estão em:

src/main/resources/db/migration/
V1__criar_tabela_empresas.sql
V2__criar_tabela_usuarios.sql
V3__criar_tabela_categorias.sql
V4__criar_tabela_ativos.sql
V5__criar_tabela_imagens.sql
V6__criar_tabela_movimentacoes.sql
V7__criar_tabela_manutencoes.sql
