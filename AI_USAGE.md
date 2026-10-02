# Registro de Uso de IA Generativa

## Avaliação A1 - Módulo de Produtos

O uso de IA durante o desenvolvimento da avaliação A1 foi realizado como ferramenta de apoio para etapas específicas:

- Apoio na estrutura inicial dos records DTOs e anotações do Swagger (@Schema).
- Consulta de boas práticas para a validação de regras de negócio (rejeição de estoque negativo retornando HTTP 422 com Problem Details RFC 7807).
- Geração dos testes automatizados: os testes unitários com Mockito (seguindo o padrão AAA) e os testes de integração com Testcontainers/PostgreSQL foram gerados com auxílio de IA.

Todo o código foi revisado, testado e compreendido por mim, dominando a arquitetura em camadas, as convenções de chave primária com prefixo id_, a criação da tabela via Flyway, o CRUD completo e a semântica HTTP.