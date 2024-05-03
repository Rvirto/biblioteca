package com.biblioteca.api.rest.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

/**
 * Client request model data input class in request to create a client
 *
 * @author Renato Virto (renatovirtomoreira@outlook.com)
 * @since 1.0.0
 */
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
