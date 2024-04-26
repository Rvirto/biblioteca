package com.biblioteca.controller.models.response;

import com.biblioteca.model.entities.Loan;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.ZonedDateTime;

@JsonPropertyOrder({
        "id",
        "book",
        "client",
        "status",
        "creationDate"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(value = "loan", collectionRelation = "loans")
public class LoanResponseModel extends RepresentationModel<LoanResponseModel> {

    @JsonProperty("id")
    private String id;

    @JsonProperty("book")
    private BookResponseModel book;

    @JsonProperty("client")
    private ClientResponseModel client;

    @JsonProperty("status")
    private String status;

    @JsonProperty("creationDate")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime creationDate;

    public LoanResponseModel() {
        super();
    }

    public LoanResponseModel(Loan loan) {
        super();
        this.id = loan.getId();
        this.status = loan.getStatus();
        this.book = new BookResponseModel(loan.getBook());
        this.client = new ClientResponseModel(loan.getClient());
        this.creationDate = loan.getCreationDate();
    }
}