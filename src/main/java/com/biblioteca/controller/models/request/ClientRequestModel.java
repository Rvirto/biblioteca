package com.biblioteca.controller.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class ClientRequestModel {

    @NotBlank
    @JsonProperty("name")
    private String name;

    @NotBlank
    @JsonProperty("email")
    private String email;

    @NotBlank
    @JsonProperty("documentType")
    private String documentType;

    @NotBlank
    @JsonProperty("documentValue")
    private String documentValue;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDocumentType() {
        return documentType;
    }

    public String getDocumentValue() {
        return documentValue;
    }
}
