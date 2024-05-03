package com.biblioteca.api.rest.endpoints;


import com.biblioteca.ApplicationTests;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static com.biblioteca.api.rest.endpoints.BookEndpoint.BOOK_RESOURCE_PATH;
import static com.biblioteca.api.rest.endpoints.BookEndpoint.BOOK_SELF_PATH;
import static com.biblioteca.domain.enumeration.BookStatusEnum.AVAILABLE;
import static com.biblioteca.domain.enumeration.BookStatusEnum.UNAVAILABLE;
import static com.biblioteca.domain.enumeration.ExceptionMessagesEnum.BOOK_ALREADY_EXISTS;
import static com.biblioteca.domain.enumeration.ExceptionMessagesEnum.BOOK_NOT_FOUND;
import static com.biblioteca.domain.enumeration.ExceptionMessagesEnum.REQUIRED_MATRIX_PARAM;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

/**
 * Book Endpoint Test class for validations of all existing endpoints and their returns from the Book endpoint class
 *
 * @author Renato Virto (renatovirtomoreira@outlook.com)
 * @since 1.0.0
 */
public class BookEndpointTest extends ApplicationTests<BookEndpointTest> {

    @Test
    public void shouldReturnOkWhenGetBookById() throws Exception {

        final String uri = fromPath(BOOK_SELF_PATH).buildAndExpand("a5993416-4255-11ec-71d3-0242ac130004").toUriString();

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("a5993416-4255-11ec-71d3-0242ac130004"))
                .andExpect(jsonPath("$.title").value("Clean Code"))
                .andExpect(jsonPath("$.author").value("Robert Cecil Martin"))
                .andExpect(jsonPath("$.yearPublication").value("2008"))
                .andExpect(jsonPath("$.creationDate").value("2024-04-23T23:01:40.619-03:00"))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnNotFoundWhenGetBookByIdThatDoesntExist() throws Exception {

        final String uri = fromPath(BOOK_SELF_PATH).buildAndExpand("BOOK_ID_NOT_EXISTS").toUriString();

        mockMvc.perform(get(uri)
                        .contentType(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors.messages").exists())
                .andExpect(jsonPath("$.errors.messages[0].code").value(BOOK_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.errors.messages[0].message").value(BOOK_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnOkWhenGetBookByTitle() throws Exception {

        final String uri = fromPath(BOOK_RESOURCE_PATH)
                .toUriString().concat(";title=Clean Code");

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.books[0].id").value("a5993416-4255-11ec-71d3-0242ac130004"))
                .andExpect(jsonPath("$._embedded.books[0].title").value("Clean Code"))
                .andExpect(jsonPath("$._embedded.books[0].author").value("Robert Cecil Martin"))
                .andExpect(jsonPath("$._embedded.books[0].yearPublication").value("2008"))
                .andExpect(jsonPath("$._embedded.books[0].status").value(AVAILABLE.name()))
                .andExpect(jsonPath("$._embedded.books[0]._links['self'].href").value(containsString(BOOK_RESOURCE_PATH.concat("/a5993416-4255-11ec-71d3-0242ac130004"))))
                .andExpect(jsonPath("$.page.size").value(10))
                .andExpect(jsonPath("$.page.totalElements").value(1))
                .andExpect(jsonPath("$.page.totalPages").value(1))
                .andExpect(jsonPath("$.page.number").value(0));
    }

    @Test
    public void shouldReturnOkWhenGetBookByAuthor() throws Exception {

        final String uri = fromPath(BOOK_RESOURCE_PATH)
                .toUriString().concat(";author=Robert Cecil Martin");

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.books[0].id").value("a5993416-4255-11ec-71d3-0242ac130004"))
                .andExpect(jsonPath("$._embedded.books[0].title").value("Clean Code"))
                .andExpect(jsonPath("$._embedded.books[0].author").value("Robert Cecil Martin"))
                .andExpect(jsonPath("$._embedded.books[0].yearPublication").value("2008"))
                .andExpect(jsonPath("$._embedded.books[0].status").value(AVAILABLE.name()))
                .andExpect(jsonPath("$._embedded.books[0]._links['self'].href").value(containsString(BOOK_RESOURCE_PATH.concat("/a5993416-4255-11ec-71d3-0242ac130004"))))
                .andExpect(jsonPath("$.page.size").value(10))
                .andExpect(jsonPath("$.page.totalElements").value(1))
                .andExpect(jsonPath("$.page.totalPages").value(1))
                .andExpect(jsonPath("$.page.number").value(0));
    }

    @Test
    public void shouldReturnOkWhenGetBookByTitleAndAuthor() throws Exception {

        final String uri = fromPath(BOOK_RESOURCE_PATH)
                .toUriString().concat(";title=Clean Code;author=Robert Cecil Martin");

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.books[0].id").value("a5993416-4255-11ec-71d3-0242ac130004"))
                .andExpect(jsonPath("$._embedded.books[0].title").value("Clean Code"))
                .andExpect(jsonPath("$._embedded.books[0].author").value("Robert Cecil Martin"))
                .andExpect(jsonPath("$._embedded.books[0].yearPublication").value("2008"))
                .andExpect(jsonPath("$._embedded.books[0].status").value(AVAILABLE.name()))
                .andExpect(jsonPath("$._embedded.books[0]._links['self'].href").value(containsString(BOOK_RESOURCE_PATH.concat("/a5993416-4255-11ec-71d3-0242ac130004"))))
                .andExpect(jsonPath("$.page.size").value(10))
                .andExpect(jsonPath("$.page.totalElements").value(1))
                .andExpect(jsonPath("$.page.totalPages").value(1))
                .andExpect(jsonPath("$.page.number").value(0));
    }

    @Test
    public void shouldReturnOkWhenGetBookByYearPublication() throws Exception {

        final String uri = fromPath(BOOK_RESOURCE_PATH)
                .toUriString().concat(";yearPublication=2008");

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.books[0].id").value("a5993416-4255-11ec-71d3-0242ac130004"))
                .andExpect(jsonPath("$._embedded.books[0].title").value("Clean Code"))
                .andExpect(jsonPath("$._embedded.books[0].author").value("Robert Cecil Martin"))
                .andExpect(jsonPath("$._embedded.books[0].yearPublication").value("2008"))
                .andExpect(jsonPath("$._embedded.books[0].status").value(AVAILABLE.name()))
                .andExpect(jsonPath("$._embedded.books[0]._links['self'].href").value(containsString(BOOK_RESOURCE_PATH.concat("/a5993416-4255-11ec-71d3-0242ac130004"))))
                .andExpect(jsonPath("$.page.size").value(10))
                .andExpect(jsonPath("$.page.totalElements").value(1))
                .andExpect(jsonPath("$.page.totalPages").value(1))
                .andExpect(jsonPath("$.page.number").value(0));
    }

    @Test
    public void shouldReturnOkWhenGetBookByTitleAndAuthorAndYearPublication() throws Exception {

        final String uri = fromPath(BOOK_RESOURCE_PATH)
                .toUriString().concat(";title=Clean Code;author=Robert Cecil Martin;yearPublication=2008");

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.books[0].id").value("a5993416-4255-11ec-71d3-0242ac130004"))
                .andExpect(jsonPath("$._embedded.books[0].title").value("Clean Code"))
                .andExpect(jsonPath("$._embedded.books[0].author").value("Robert Cecil Martin"))
                .andExpect(jsonPath("$._embedded.books[0].yearPublication").value("2008"))
                .andExpect(jsonPath("$._embedded.books[0].status").value(AVAILABLE.name()))
                .andExpect(jsonPath("$._embedded.books[0]._links['self'].href").value(containsString(BOOK_RESOURCE_PATH.concat("/a5993416-4255-11ec-71d3-0242ac130004"))))
                .andExpect(jsonPath("$.page.size").value(10))
                .andExpect(jsonPath("$.page.totalElements").value(1))
                .andExpect(jsonPath("$.page.totalPages").value(1))
                .andExpect(jsonPath("$.page.number").value(0));
    }

    @Test
    public void shouldReturnOkWhenGetBookByAuthorThatDoesNotExist() throws Exception {
        final String uri = fromPath(BOOK_RESOURCE_PATH).toUriString().concat(";author=invalidAuthor");

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)))
                .andExpect(jsonPath("$.page.size").value(10))
                .andExpect(jsonPath("$.page.totalElements").value(0))
                .andExpect(jsonPath("$.page.totalPages").value(0))
                .andExpect(jsonPath("$.page.number").value(0));
    }

    @Test
    public void shouldReturnOkWhenGetBookByTitleButAuthorThatDoesNotExist() throws Exception {
        final String uri = fromPath(BOOK_RESOURCE_PATH).toUriString().concat("title=Clean Code;author=invalidAuthor");

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.size").value(10))
                .andExpect(jsonPath("$.page.totalElements").value(0))
                .andExpect(jsonPath("$.page.totalPages").value(0))
                .andExpect(jsonPath("$.page.number").value(0));
    }

    @Test
    public void shouldReturnBadRequestWhenGetBookByInvalidMatrixVariable() throws Exception {
        final String uri = fromPath(BOOK_RESOURCE_PATH).toUriString().concat(";invalid=invalid");

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.messages").exists())
                .andExpect(jsonPath("$.errors.messages[0].code").value(REQUIRED_MATRIX_PARAM.getCode()))
                .andExpect(jsonPath("$.errors.messages[0].message").value(String.format(REQUIRED_MATRIX_PARAM.getMessage(),
                        "title or author or status or yearPublication")))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnOkWhenGetBookByStatus() throws Exception {

        final String uri = fromPath(BOOK_RESOURCE_PATH)
                .toUriString().concat(";status=AVAILABLE");

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.books[0].id").value("a5993416-4255-11ec-71d3-0242ac130004"))
                .andExpect(jsonPath("$._embedded.books[0].title").value("Clean Code"))
                .andExpect(jsonPath("$._embedded.books[0].author").value("Robert Cecil Martin"))
                .andExpect(jsonPath("$._embedded.books[0].yearPublication").value("2008"))
                .andExpect(jsonPath("$._embedded.books[0].status").value(AVAILABLE.name()))
                .andExpect(jsonPath("$._embedded.books[0]._links['self'].href").value(containsString(BOOK_RESOURCE_PATH.concat("/a5993416-4255-11ec-71d3-0242ac130004"))))
                .andExpect(jsonPath("$._embedded.books[1].id").value("a5993416-4255-11ec-71d3-0242ac130005"))
                .andExpect(jsonPath("$._embedded.books[1].title").value("Design Patterns com Java"))
                .andExpect(jsonPath("$._embedded.books[1].author").value("Eduardo Guerra"))
                .andExpect(jsonPath("$._embedded.books[1].yearPublication").value("2014"))
                .andExpect(jsonPath("$._embedded.books[1].status").value(AVAILABLE.name()))
                .andExpect(jsonPath("$._embedded.books[1]._links['self'].href").value(containsString(BOOK_RESOURCE_PATH.concat("/a5993416-4255-11ec-71d3-0242ac130005"))))
                .andExpect(jsonPath("$._embedded.books[2].id").value("a5993416-4255-11ec-71d3-0242ac130006"))
                .andExpect(jsonPath("$._embedded.books[2].title").value("Google BigQuery"))
                .andExpect(jsonPath("$._embedded.books[2].author").value("Valliappa Lakshmanan e Jordan Tigani"))
                .andExpect(jsonPath("$._embedded.books[2].yearPublication").value("2019"))
                .andExpect(jsonPath("$._embedded.books[2].status").value(AVAILABLE.name()))
                .andExpect(jsonPath("$._embedded.books[2]._links['self'].href").value(containsString(BOOK_RESOURCE_PATH.concat("/a5993416-4255-11ec-71d3-0242ac130006"))))
                .andExpect(jsonPath("$.page.size").value(10))
                .andExpect(jsonPath("$.page.totalPages").value(1))
                .andExpect(jsonPath("$.page.number").value(0));
    }

    @Test
    public void shouldReturnCreatedWhenPostBook() throws Exception {
        final String uri = fromPath(BOOK_RESOURCE_PATH).toUriString();

        String content = super.getScenarioBody("shouldReturnCreatedWhenPostBook");

        MvcResult result = mockMvc.perform(post(uri)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, containsString(uri)))
                .andReturn();

        String location = result.getResponse().getHeader(LOCATION);
        String bookId = super.getBookIdOfLocation(location);

        mockMvc.perform(get(location)
                        .contentType(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookId))
                .andExpect(jsonPath("$.title").value("Effective Java"))
                .andExpect(jsonPath("$.status").value(UNAVAILABLE.name()))
                .andExpect(jsonPath("$.author").value("Joshua Bloch"))
                .andExpect(jsonPath("$.yearPublication").value("2001"))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnBadRequestWhenPostBookWithoutTitle() throws Exception {
        final String uri = fromPath(BOOK_RESOURCE_PATH).toUriString();

        String content = super.getScenarioBody("shouldReturnBadRequestWhenPostBookWithoutTitle");

        mockMvc.perform(post(uri)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.messages").exists())
                .andExpect(jsonPath("$.errors.messages[0].code").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errors.messages[0].message").value("title must not be blank"))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnBadRequestWhenPostBookWithoutAuthor() throws Exception {
        final String uri = fromPath(BOOK_RESOURCE_PATH).toUriString();

        String content = super.getScenarioBody("shouldReturnBadRequestWhenPostBookWithoutAuthor");

        mockMvc.perform(post(uri)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.messages").exists())
                .andExpect(jsonPath("$.errors.messages[0].code").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errors.messages[0].message").value("author must not be blank"))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnBadRequestWhenPostBookWithoutYearPublication() throws Exception {
        final String uri = fromPath(BOOK_RESOURCE_PATH).toUriString();

        String content = super.getScenarioBody("shouldReturnBadRequestWhenPostBookWithoutYearPublication");

        mockMvc.perform(post(uri)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.messages").exists())
                .andExpect(jsonPath("$.errors.messages[0].code").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errors.messages[0].message").value("yearPublication must not be blank"))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnBadRequestWhenPostBookWithoutStatus() throws Exception {
        final String uri = fromPath(BOOK_RESOURCE_PATH).toUriString();

        String content = super.getScenarioBody("shouldReturnBadRequestWhenPostBookWithoutStatus");

        mockMvc.perform(post(uri)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.messages").exists())
                .andExpect(jsonPath("$.errors.messages[0].code").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errors.messages[0].message").value("status must not be null"))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnBadRequestWhenPostBookWithWrongStatus() throws Exception {
        final String uri = fromPath(BOOK_RESOURCE_PATH).toUriString();

        String content = super.getScenarioBody("shouldReturnBadRequestWhenPostBookWithWrongStatus");

        mockMvc.perform(post(uri)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.messages").exists())
                .andExpect(jsonPath("$.errors.messages[0].code").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnConflictWhenPostBookAlreadyExists() throws Exception {
        final String uri = fromPath(BOOK_RESOURCE_PATH).toUriString();

        String content = super.getScenarioBody("shouldReturnConflictWhenPostBookAlreadyExists");

        mockMvc.perform(post(uri)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, containsString(uri)));

        mockMvc.perform(post(uri)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errors.messages").exists())
                .andExpect(jsonPath("$.errors.messages[0].code").value(BOOK_ALREADY_EXISTS.getCode()))
                .andExpect(jsonPath("$.errors.messages[0].message").value(BOOK_ALREADY_EXISTS.getMessage()))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }
}