package com.biblioteca.controller.endpoints;

import com.biblioteca.controller.assemblers.BookAssembler;
import com.biblioteca.controller.models.request.BookRequestModel;
import com.biblioteca.controller.models.response.BookResponseModel;
import com.biblioteca.controller.validators.annotations.BookMatrixParamValidation;
import com.biblioteca.model.builders.BookBuilder;
import com.biblioteca.model.entities.Book;
import com.biblioteca.model.enumeration.BookStatusEnum;
import com.biblioteca.model.exceptions.ConflictException;
import com.biblioteca.model.exceptions.NotFoundException;
import com.biblioteca.model.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.biblioteca.model.enumeration.ExceptionMessagesEnum.BOOK_ALREADY_EXISTS;
import static com.biblioteca.model.enumeration.ExceptionMessagesEnum.BOOK_NOT_FOUND;

@RestController
@Validated
public class BookEndpoint {

    public static final String BOOK_RESOURCE_PATH = "/api/books";
    public static final String BOOK_SELF_PATH = BOOK_RESOURCE_PATH + "/{bookId}";

    private final BookService bookService;
    private final BookAssembler bookAssembler;
    private final PagedResourcesAssembler<Book> pagedResponseAssembler;

    @Autowired
    public BookEndpoint(BookService bookService, BookAssembler bookAssembler, PagedResourcesAssembler<Book> pagedResponseAssembler) {
        this.bookService = bookService;
        this.bookAssembler = bookAssembler;
        this.pagedResponseAssembler = pagedResponseAssembler;
    }

    @GetMapping(BOOK_SELF_PATH)
    public ResponseEntity<BookResponseModel> getById(@PathVariable("bookId") final String bookId) {
        final Book book = bookService.findById(bookId).orElseThrow(() -> new NotFoundException(BOOK_NOT_FOUND));
        return ResponseEntity.ok().body(bookAssembler.toModel(book));
    }

    @GetMapping(value = BOOK_RESOURCE_PATH + "{matrixParam}")
    @BookMatrixParamValidation
    public ResponseEntity<PagedModel<BookResponseModel>> getByValues(Pageable pageable,
                                                                     @MatrixVariable(value = "title", required = false) final String title,
                                                                     @MatrixVariable(value = "author", required = false) final String author,
                                                                     @MatrixVariable(value = "status", required = false) final BookStatusEnum status,
                                                                     @MatrixVariable(value = "yearPublication", required = false) final String yearPublication) {
        Book book = BookBuilder.builder().withTitle(title).withAuthor(author).withYearPublication(yearPublication).build();
        Page<Book> bookPage = this.bookService.getByBook(book, pageable, status);

        return ResponseEntity.ok()
                .body(pagedResponseAssembler.toModel(bookPage, bookAssembler));
    }

    @PostMapping(BOOK_RESOURCE_PATH)
    public ResponseEntity<?> create(@RequestBody @Valid BookRequestModel bookRequestModel) {
        Book book = bookAssembler.toEntity(bookRequestModel);

        bookService.findBook(book).stream().findFirst().ifPresent(searchedBook -> {
            throw new ConflictException(
                    BOOK_ALREADY_EXISTS, bookAssembler
                    .buildBookSelfLink(searchedBook.getId()).toUri());
        });

        book = bookService.create(book, bookRequestModel);
        return ResponseEntity.created(bookAssembler.buildBookSelfLink(book.getId()).toUri()).build();
    }
}
