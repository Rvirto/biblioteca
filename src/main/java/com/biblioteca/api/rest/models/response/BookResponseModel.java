package com.biblioteca.api.rest.models.response;

import com.biblioteca.domain.entities.Book;
import com.biblioteca.domain.enumeration.BookStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.ZonedDateTime;

@JsonPropertyOrder({
        "id",
        "title",
        "author",
        "yearPublication",
        "status",
        "creationDate"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(value = "book", collectionRelation = "books")
public class BookResponseModel extends RepresentationModel<BookResponseModel> {

    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("author")
    private String author;

    @JsonProperty("yearPublication")
    private String yearPublication;

    @JsonProperty("creationDate")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime creationDate;

    @JsonProperty("status")
    private String status;

    public BookResponseModel() {
        super();
    }

    public BookResponseModel(Book book) {
        super();
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.yearPublication = book.getYearPublication();
        this.creationDate = book.getCreationDate();
        this.status = this.getStatus(book);
    }

    private String getStatus(Book book) {
        return BookStatusEnum.getStatus(book.isEnable()).name();
    }
}