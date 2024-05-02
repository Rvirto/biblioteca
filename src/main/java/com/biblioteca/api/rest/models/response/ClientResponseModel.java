package com.biblioteca.api.rest.models.response;

import com.biblioteca.domain.entities.Client;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.ZonedDateTime;

@JsonPropertyOrder({
        "id",
        "name",
        "email",
        "documentType",
        "documentValue",
        "creationDate"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(value = "client", collectionRelation = "clients")
public class ClientResponseModel extends RepresentationModel<ClientResponseModel> {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("documentType")
    private String documentType;

    @JsonProperty("documentValue")
    private String documentValue;

    @JsonProperty("creationDate")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime creationDate;

    public ClientResponseModel() {
        super();
    }

    public ClientResponseModel(Client client) {
        super();
        this.id = client.getId();
        this.name = client.getName();
        this.email = client.getEmail();
        this.documentType = client.getDocumentType();
        this.documentValue = client.getDocumentValue();
        this.creationDate = client.getCreationDate();
    }
}