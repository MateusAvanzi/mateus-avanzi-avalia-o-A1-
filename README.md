# SocialConnect API

> API RESTful de gestão para instituições sociais (ONGs, bancos de alimentos,
> CRAS, abrigos). Conecta doadores, voluntários e beneficiários.

**Disciplina:** Tópicos Especiais em Sistemas para Internet III
**Stack:** Java 21 · Spring Boot 4.1.1 · JPA · H2 (dev) · PostgreSQL (prod)

---

## Como Rodar

### Pré-requisitos

- JDK 21 LTS ([Adoptium](https://adoptium.net/))
- Maven 3.9+ (ou use o wrapper: `./mvnw`)
- IDE: IntelliJ IDEA (recomendado) ou VS Code

### Passos

```bash
# 1. Clone o repositório
git clone <url-do-repo>
cd socialconnect-api

# 2. Compile o projeto
mvn clean compile

# 3. Execute os testes automatizados
mvn test

# 4. Rode a aplicação
mvn spring-boot:run
```

---

## Documentação Interativa (Swagger UI)

- **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON:** [http://localhost:8080/api-docs](http://localhost:8080/api-docs)

---

## Módulo de Produtos (Avaliação A1)

O módulo de Produtos gerencia o estoque de doações físicas da SocialConnect.

### Regras de Negócio Implementadas

1. **Padrão `id_` para Chaves Primárias:** Coluna `id_produto` no banco de dados com auto-incremento.
2. **Estoque Não Negativo:** Qualquer operação que resulte em `estoqueAtual < 0` ou `estoqueMinimo < 0` é rejeitada com **422 Unprocessable Entity** (RFC 7807 Problem Details).
3. **Alerta de Estoque Baixo:** O DTO de resposta inclui o booleano `estoqueBaixo` calculado dinamicamente (`estoqueAtual < estoqueMinimo`).
4. **Nome Único:** Não é permitido cadastrar produtos com nomes duplicados (**409 Conflict**).
5. **DTOs e Validações:** Java 21 `record` com Bean Validation, validação customizada `@UnidadeMedidaValida` e i18n em português.

### Endpoints Principais

- `GET /api/v1/produtos`: Lista paginada com filtros opcionais por `nome` e `categoria`.
- `GET /api/v1/produtos/{id_produto}`: Busca produto por ID (200 OK ou 404 Not Found).
- `POST /api/v1/produtos`: Cadastra produto (201 Created com Location, 400, 409, 422).
- `PUT /api/v1/produtos/{id_produto}`: Atualização total (200 OK, 400, 404, 409, 422).
- `DELETE /api/v1/produtos/{id_produto}`: Remove produto (204 No Content ou 404 Not Found).