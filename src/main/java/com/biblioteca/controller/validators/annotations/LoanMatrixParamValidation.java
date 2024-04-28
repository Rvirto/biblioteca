package com.biblioteca.controller.validators.annotations;

import com.biblioteca.controller.validators.LoanMatrixParamValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.METHOD)
@Retention(RUNTIME)
@Constraint(validatedBy = LoanMatrixParamValidator.class)
@Documented
public @interface LoanMatrixParamValidation {
    String message() default "Invalid matrix parameters";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
