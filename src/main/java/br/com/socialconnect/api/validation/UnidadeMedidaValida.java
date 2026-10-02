package br.com.socialconnect.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UnidadeMedidaValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface UnidadeMedidaValida {
    String message() default "{produto.unidade.medida.invalida}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
