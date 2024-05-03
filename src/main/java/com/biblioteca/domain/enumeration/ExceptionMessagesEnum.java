package com.biblioteca.domain.enumeration;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Exception Message Enum class used to map all exceptions thrown in the project
 *
 * @author Renato Virto (renatovirtomoreira@outlook.com)
 * @since 1.0.0
 */
public enum ExceptionMessagesEnum {

    //400
    BOOK_IS_NOT_AVAILABLE(400001, "Book is not available for loan", HttpStatus.BAD_REQUEST),
    REQUIRED_MATRIX_PARAM(400002, "Required array parameter '%s' for method parameter is missing or entered twice", BAD_REQUEST),
    LOAN_ALREADY_RETURNED(400003, "Loan already returned", BAD_REQUEST),

    //404
    BOOK_NOT_FOUND(404001, "Book not found for Id informed", HttpStatus.NOT_FOUND),
    CLIENT_NOT_FOUND(404002, "Client not found for Id informed", HttpStatus.NOT_FOUND),
    LOAN_NOT_FOUND(404003, "Loan not found for Id informed", HttpStatus.NOT_FOUND),

    //409
    BOOK_ALREADY_EXISTS(409001, "Book with these parameters already exists", HttpStatus.CONFLICT),
    CLIENT_ALREADY_EXISTS(409002, "Client with these parameters already exists", HttpStatus.CONFLICT),
    LOAN_ALREADY_EXISTS(409003, "Loan with these parameters already exists", HttpStatus.CONFLICT);


    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;

    ExceptionMessagesEnum(Integer code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}