package com.biblioteca.domain.exceptions;

import com.biblioteca.domain.enumeration.ExceptionMessagesEnum;
import org.springframework.http.HttpStatus;

import java.net.URI;

public class ConflictException extends HttpException {

    private static final long serialVersionUID = 1L;
    private URI location;

    public ConflictException(final String message) {
        super(message, HttpStatus.CONFLICT);
    }

    public ConflictException(final ExceptionMessagesEnum exceptionMessagesEnum, final URI locationURI) {
        super(exceptionMessagesEnum.getCode(), exceptionMessagesEnum.getMessage(), HttpStatus.CONFLICT);
        this.location = locationURI;
    }

    public URI getLocation() {
        return location;
    }
}