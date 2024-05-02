package com.biblioteca.api.rest.validators;

import com.biblioteca.api.rest.validators.annotations.LoanMatrixParamValidation;
import com.biblioteca.domain.enumeration.LoanStatusEnum;
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
public class LoanMatrixParamValidator
        implements ConstraintValidator<LoanMatrixParamValidation, Object[]> {

    @Override
    public void initialize(LoanMatrixParamValidation constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object[] values, ConstraintValidatorContext constraintValidatorContext) {
        List<String> parameterNames = ((CrossParameterConstraintValidatorContextImpl) constraintValidatorContext).getMethodParameterNames();

        int clientIdIndex = this.findParameterNameIndex(parameterNames, "clientId");
        int bookIdIndex = this.findParameterNameIndex(parameterNames, "bookId");
        int statusIndex = this.findParameterNameIndex(parameterNames, "status");

        if (this.isValid((String) values[clientIdIndex], (LoanStatusEnum) values[statusIndex],
                (String) values[bookIdIndex])) {
            return true;
        } else {
            throw new BadRequestException(REQUIRED_MATRIX_PARAM, "clientId or bookId or status");
        }
    }

    private boolean isValid(String clientId, LoanStatusEnum status, String bookId) {
        if (clientId != null || status != null || bookId != null) {
            return true;
        }
        return false;
    }

    private int findParameterNameIndex(List<String> parameterNames, String parameterNameToFind) {
        return IntStream.range(0, parameterNames.size())
                .filter(i -> parameterNames.get(i).equalsIgnoreCase(parameterNameToFind)).findFirst().getAsInt();
    }
}
