package com.biblioteca.domain.exceptions;

import com.biblioteca.domain.enumeration.ExceptionMessagesEnum;
import org.springframework.http.HttpStatus;

/**
 * Bad Request Exception class used to throw custom exceptions
 *
 * @author Renato Virto (renatovirtomoreira@outlook.com)
 * @since 1.0.0
 */
public class BadRequestException extends HttpException {

    private static final long serialVersionUID = 1L;

    public BadRequestException(final String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public BadRequestException(final ExceptionMessagesEnum exceptionMessagesEnum) {
        super(exceptionMessagesEnum.getCode(), exceptionMessagesEnum.getMessage(), HttpStatus.BAD_REQUEST);
    }

    public BadRequestException(final ExceptionMessagesEnum exceptionMessagesEnum, Object... args) {
        super(exceptionMessagesEnum.getCode(), String.format(exceptionMessagesEnum.getMessage(), args),
                HttpStatus.BAD_REQUEST);
    }
}