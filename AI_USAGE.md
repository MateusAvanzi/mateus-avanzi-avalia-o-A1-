# Registro de Uso de IA Generativa

> **Política da disciplina:** O uso de IA generativa é permitido como
> assistente. O discente é **integralmente responsável** por testar, auditar e
> defender todo o código entregue, independentemente de como foi gerado.

## Instruções

Para cada aula ou entrega, registre abaixo:
- **Data**
- **Ferramenta** (ChatGPT, Copilot, Claude, etc.)
- **Prompt(s) utilizado(s)** (resumo ou cópia)
- **O que foi feito com a saída** (copiado integralmente, adaptado, usado como referência, descartado)

---

## Registro

| Data | Aula | Ferramenta | Prompt (resumo) | Uso da saída |
|------|------|------------|-----------------|--------------|
| 02/10/2026 | A1 - Módulo de Produtos | Gemini | "Auxílio na geração dos records DTOs e OpenAPI schemas" | Adaptado e integrado ao projeto com validações |
| 02/10/2026 | A1 - Módulo de Produtos | Gemini | "Revisão dos testes unitários com padrão AAA" | Utilizado como referência e validação |

---

## Declaração de Uso de IA (A1)

### Ferramentas utilizadas:
- [x] ChatGPT / Claude / Gemini
- [ ] Copilot / Codeium
- [ ] Nenhuma

### Como utilizei:
- Usei IA para auxiliar na geração da estrutura inicial dos DTOs e anotações OpenAPI (@Schema).
- Usei IA para suporte na revisão dos testes unitários seguindo o padrão AAA.
- Usei IA para consultar boas práticas de mapeamento com Flyway e Problem Details (RFC 7807).

### O que eu entendo 100%:
- A arquitetura em camadas e injeção de dependência via construtor com campos final.
- A lógica de validação de estoque não negativo (HTTP 422) e cálculo dinâmico de estoque baixo.
- A convenção de chaves primárias id_ no banco de dados e mapeamento JPA.
- A implementação do CRUD completo com semântica HTTP rigorosa e Problem Details.
- O padrão AAA (Arrange, Act, Assert) nos testes unitários e de integração.

### O que precisei estudar mais:
- As particularidades do SpringDoc OpenAPI e testes de integração com Testcontainers.

---

_Declaração: Ao submeter este repositório, confirmo que todo o código foi
revisado, testado e compreendido por mim._