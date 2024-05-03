package com.biblioteca.api.rest.models.errors;

import java.util.LinkedList;
import java.util.List;

/**
 * Error Message Wrapper Model class to wrap a list of error messages
 *
 * @author Renato Virto (renatovirtomoreira@outlook.com)
 * @since 1.0.0
 */
public class ErrorMessageWrapperModel {

    private List<ErrorMessageModel> messages;

    public ErrorMessageWrapperModel() {
        messages = new LinkedList<ErrorMessageModel>();
    }

    public List<ErrorMessageModel> getMessages() {
        return messages;
    }

    public void setMessages(final List<ErrorMessageModel> messages) {
        this.messages = messages;
    }

    public void addErrorMessage(final Integer errorCode, final String errorMessage) {
        messages.add(new ErrorMessageModel(errorCode, errorMessage));
    }
}