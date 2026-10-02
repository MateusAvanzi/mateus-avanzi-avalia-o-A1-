package br.com.socialconnect.api.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public record ProblemDetail(
    @Schema(description = "URI de referência do tipo de erro", example = "https://socialconnect.api/errors/validacao")
    String type,

    @Schema(description = "Resumo legível do erro", example = "Erro de validação")
    String title,

    @Schema(description = "Código de status HTTP", example = "400")
    int status,

    @Schema(description = "Descrição detalhada da ocorrência", example = "Um ou mais campos são inválidos.")
    String detail,

    @Schema(description = "URI da requisição que gerou o erro", example = "/api/v1/produtos")
    String instance,

    @Schema(description = "Data e hora do erro", example = "2026-10-02T19:50:00")
    LocalDateTime timestamp,

    @Schema(description = "Lista de erros específicos por campo")
    List<FieldError> errors
) {
    public record FieldError(
        @Schema(description = "Campo que apresentou o erro", example = "nome")
        String field,

        @Schema(description = "Mensagem descritiva do erro", example = "Nome é obrigatório")
        String message
    ) {}
}
