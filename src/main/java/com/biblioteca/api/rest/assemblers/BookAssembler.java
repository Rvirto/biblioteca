package com.biblioteca.api.rest.assemblers;

import com.biblioteca.api.rest.models.request.BookRequestModel;
import com.biblioteca.api.rest.endpoints.BookEndpoint;
import com.biblioteca.api.rest.models.response.BookResponseModel;
import com.biblioteca.domain.entities.Book;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BookAssembler extends RepresentationModelAssemblerSupport<Book, BookResponseModel> {

    public BookAssembler() {
        super(BookEndpoint.class, BookResponseModel.class);
    }

    public Link buildBookSelfLink(String bookId) {
        return linkTo(methodOn(BookEndpoint.class).getById(bookId)).withSelfRel();
    }

    @Override
    public BookResponseModel toModel(Book book) {
        final BookResponseModel model = new BookResponseModel(book);
        model.add(buildBookSelfLink(book.getId()));
        return model;
    }

    public Book toEntity(BookRequestModel bookRequestModel) {
        Book book = new Book();
        book.setTitle(bookRequestModel.getTitle());
        book.setAuthor(bookRequestModel.getAuthor());
        book.setYearPublication(bookRequestModel.getYearPublication());
        return book;
    }
}