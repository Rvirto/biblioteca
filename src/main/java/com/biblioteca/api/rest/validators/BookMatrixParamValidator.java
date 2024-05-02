package com.biblioteca.api.rest.validators;

import com.biblioteca.api.rest.validators.annotations.BookMatrixParamValidation;
import com.biblioteca.domain.enumeration.BookStatusEnum;
import com.biblioteca.domain.exceptions.BadRequestException;
import org.hibernate.validator.internal.engine.constraintvalidation.CrossParameterConstraintValidatorContextImpl;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;
import java.util.List;
import java.util.stream.IntStream;

import static com.biblioteca.domain.enumeration.ExceptionMessagesEnum.REQUIRED_MATRIX_PARAM;

@Component
@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class BookMatrixParamValidator
        implements ConstraintValidator<BookMatrixParamValidation, Object[]> {

    @Override
    public void initialize(BookMatrixParamValidation constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object[] values, ConstraintValidatorContext constraintValidatorContext) {
        List<String> parameterNames = ((CrossParameterConstraintValidatorContextImpl) constraintValidatorContext).getMethodParameterNames();

        int titleIndex = this.findParameterNameIndex(parameterNames, "title");
        int authorIndex = this.findParameterNameIndex(parameterNames, "author");
        int statusIndex = this.findParameterNameIndex(parameterNames, "status");
        int yearPublicationIndex = this.findParameterNameIndex(parameterNames, "yearPublication");

        if (this.isValid((String) values[titleIndex], (BookStatusEnum) values[statusIndex],
                (String) values[authorIndex], (String) values[yearPublicationIndex])) {
            return true;
        } else {
            throw new BadRequestException(REQUIRED_MATRIX_PARAM, "title or author or status or yearPublication");
        }
    }

    private boolean isValid(String title, BookStatusEnum status, String author, String yearPublication) {
        if (title != null || status != null || author != null || yearPublication != null) {
            return true;
        }
        return false;
    }

    private int findParameterNameIndex(List<String> parameterNames, String parameterNameToFind) {
        return IntStream.range(0, parameterNames.size())
                .filter(i -> parameterNames.get(i).equalsIgnoreCase(parameterNameToFind)).findFirst().getAsInt();
    }
}
