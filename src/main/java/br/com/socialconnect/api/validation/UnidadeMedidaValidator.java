package br.com.socialconnect.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class UnidadeMedidaValidator implements ConstraintValidator<UnidadeMedidaValida, String> {

    private static final Set<String> UNIDADES_VALIDAS = Set.of(
            "unidade", "kg", "g", "litro", "l", "ml", "pacote", "caixa", "fardo", "lata", "peça", "duzia"
    );

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true; // Deixa o @NotBlank tratar se for nulo/vazio
        }
        return UNIDADES_VALIDAS.contains(value.trim().toLowerCase());
    }
}
