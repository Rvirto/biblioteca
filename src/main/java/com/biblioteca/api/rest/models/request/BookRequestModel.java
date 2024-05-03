package com.biblioteca.api.rest.models.request;

import com.biblioteca.domain.enumeration.BookStatusEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Book request model data input class in request to create a book
 *
 * @author Renato Virto (renatovirtomoreira@outlook.com)
 * @since 1.0.0
 */
public class BookRequestModel {

    @NotBlank
    @JsonProperty("title")
    private String title;

    @NotBlank
    @JsonProperty("author")
    private String author;

    @NotBlank
    @Size(max = 4, message = "must have a maximum of 4 characters")
    @JsonProperty("yearPublication")
    private String yearPublication;

    @NotNull
    @JsonProperty("status")
    private BookStatusEnum status;

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getYearPublication() {
        return yearPublication;
    }

    public BookStatusEnum getStatus() {
        return status;
    }
}
