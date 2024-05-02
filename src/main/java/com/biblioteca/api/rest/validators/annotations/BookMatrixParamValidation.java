package com.biblioteca.api.rest.validators.annotations;

import com.biblioteca.api.rest.validators.BookMatrixParamValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.METHOD)
@Retention(RUNTIME)
@Constraint(validatedBy = BookMatrixParamValidator.class)
@Documented
public @interface BookMatrixParamValidation {
    String message() default "Invalid matrix parameters";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
