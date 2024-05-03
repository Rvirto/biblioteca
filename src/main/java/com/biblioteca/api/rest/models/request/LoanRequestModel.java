package com.biblioteca.api.rest.models.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

/**
 * Loan request model data input class in request to create a loan
 *
 * @author Renato Virto (renatovirtomoreira@outlook.com)
 * @since 1.0.0
 */
public class LoanRequestModel {

    @NotBlank
    @JsonProperty("bookId")
    private String bookId;

    @NotBlank
    @JsonProperty("clientId")
    private String clientId;

    @NotNull
    @JsonProperty("devolutionDate")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime devolutionDate;

    public String getClientId() {
        return clientId;
    }

    public ZonedDateTime getDevolutionDate() {
        return devolutionDate;
    }

    public String getBookId() {
        return bookId;
    }
}
