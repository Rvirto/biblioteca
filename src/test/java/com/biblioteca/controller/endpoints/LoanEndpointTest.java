package com.biblioteca.controller.endpoints;

import com.biblioteca.ApplicationTests;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static com.biblioteca.controller.endpoints.LoanEndpoint.LOAN_DEVOLUTION_RESOURCE_PATH;
import static com.biblioteca.controller.endpoints.LoanEndpoint.LOAN_RESOURCE_PATH;
import static com.biblioteca.controller.endpoints.LoanEndpoint.LOAN_SELF_PATH;
import static com.biblioteca.model.enumeration.BookStatusEnum.AVAILABLE;
import static com.biblioteca.model.enumeration.BookStatusEnum.UNAVAILABLE;
import static com.biblioteca.model.enumeration.ExceptionMessagesEnum.BOOK_IS_NOT_AVAILABLE;
import static com.biblioteca.model.enumeration.ExceptionMessagesEnum.BOOK_NOT_FOUND;
import static com.biblioteca.model.enumeration.ExceptionMessagesEnum.CLIENT_NOT_FOUND;
import static com.biblioteca.model.enumeration.ExceptionMessagesEnum.LOAN_ALREADY_EXISTS;
import static com.biblioteca.model.enumeration.ExceptionMessagesEnum.LOAN_ALREADY_RETURNED;
import static com.biblioteca.model.enumeration.ExceptionMessagesEnum.LOAN_NOT_FOUND;
import static com.biblioteca.model.enumeration.ExceptionMessagesEnum.REQUIRED_MATRIX_PARAM;
import static com.biblioteca.model.enumeration.LoanStatusEnum.BORROWED;
import static com.biblioteca.model.enumeration.LoanStatusEnum.RETURNED;
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

public class LoanEndpointTest extends ApplicationTests<LoanEndpointTest> {

    @Test
    public void shouldReturnOkWhenGetLoanById() throws Exception {

        final String uri = fromPath(LOAN_SELF_PATH).buildAndExpand("a5993416-4255-11ec-71d3-0242ac130004").toUriString();

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("a5993416-4255-11ec-71d3-0242ac130004"))
                .andExpect(jsonPath("$.status").value(BORROWED.name()))
                .andExpect(jsonPath("$.book.id").value("a5993416-4255-11ec-71d3-0242ac130007"))
                .andExpect(jsonPath("$.book.title").value("Observability Engineering"))
                .andExpect(jsonPath("$.book.author").value("Liz Fong"))
                .andExpect(jsonPath("$.book.yearPublication").value("2022"))
                .andExpect(jsonPath("$.book.status").value(UNAVAILABLE.name()))
                .andExpect(jsonPath("$.client.id").value("a5993416-4255-11ec-71d3-0242ac130004"))
                .andExpect(jsonPath("$.client.name").value("Renato Virto"))
                .andExpect(jsonPath("$.client.email").value("renatovirtomoreira@outlook.com"))
                .andExpect(jsonPath("$.client.documentType").value("CPF"))
                .andExpect(jsonPath("$.client.documentValue").value("12345678998"))
                .andExpect(jsonPath("$.creationDate").value("2024-04-23T23:01:40.619-03:00"))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnNotFoundWhenGetLoanByIdThatDoesntExist() throws Exception {

        final String uri = fromPath(LOAN_SELF_PATH).buildAndExpand("LOAN_ID_NOT_EXISTS").toUriString();

        mockMvc.perform(get(uri)
                        .contentType(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors.messages").exists())
                .andExpect(jsonPath("$.errors.messages[0].code").value(LOAN_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.errors.messages[0].message").value(LOAN_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnCreatedWhenPostLoan() throws Exception {
        final String uri = fromPath(LOAN_RESOURCE_PATH).toUriString();

        String content = super.getScenarioBody("shouldReturnCreatedWhenPostLoan");

        MvcResult result = mockMvc.perform(post(uri)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, containsString(uri)))
                .andReturn();

        String location = result.getResponse().getHeader(LOCATION);
        String loanId = super.getLoanIdOfLocation(location);

        mockMvc.perform(get(location)
                        .contentType(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(loanId))
                .andExpect(jsonPath("$.status").value(BORROWED.name()))
                .andExpect(jsonPath("$.book.id").value("a5993416-4255-11ec-71d3-0242ac130006"))
                .andExpect(jsonPath("$.book.title").value("Google BigQuery"))
                .andExpect(jsonPath("$.book.author").value("Valliappa Lakshmanan e Jordan Tigani"))
                .andExpect(jsonPath("$.book.yearPublication").value("2019"))
                .andExpect(jsonPath("$.book.status").value(UNAVAILABLE.name()))
                .andExpect(jsonPath("$.client.id").value("a5993416-4255-11ec-71d3-0242ac130005"))
                .andExpect(jsonPath("$.client.name").value("Maria Eduarda Gomes"))
                .andExpect(jsonPath("$.client.email").value("mariaeduardagomes@outlook.com"))
                .andExpect(jsonPath("$.client.documentType").value("CPF"))
                .andExpect(jsonPath("$.client.documentValue").value("09876543212"))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnBadRequestWhenPostLoanWithoutClientId() throws Exception {
        final String uri = fromPath(LOAN_RESOURCE_PATH).toUriString();

        String content = super.getScenarioBody("shouldReturnBadRequestWhenPostLoanWithoutClientId");

        mockMvc.perform(post(uri)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.messages").exists())
                .andExpect(jsonPath("$.errors.messages[0].code").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errors.messages[0].message").value("clientId must not be blank"))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnBadRequestWhenPostLoanWithoutBookId() throws Exception {
        final String uri = fromPath(LOAN_RESOURCE_PATH).toUriString();

        String content = super.getScenarioBody("shouldReturnBadRequestWhenPostLoanWithoutBookId");

        mockMvc.perform(post(uri)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.messages").exists())
                .andExpect(jsonPath("$.errors.messages[0].code").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errors.messages[0].message").value("bookId must not be blank"))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnBadRequestWhenPostLoanWithoutDevolutionDate() throws Exception {
        final String uri = fromPath(LOAN_RESOURCE_PATH).toUriString();

        String content = super.getScenarioBody("shouldReturnBadRequestWhenPostLoanWithoutDevolutionDate");

        mockMvc.perform(post(uri)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.messages").exists())
                .andExpect(jsonPath("$.errors.messages[0].code").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errors.messages[0].message").value("devolutionDate must not be null"))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnBadRequestWhenPostLoanButAlreadyExists() throws Exception {
        final String uri = fromPath(LOAN_RESOURCE_PATH).toUriString();

        String content = super.getScenarioBody("shouldReturnBadRequestWhenPostLoanButAlreadyExists");

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
                .andExpect(jsonPath("$.errors.messages[0].code").value(LOAN_ALREADY_EXISTS.getCode()))
                .andExpect(jsonPath("$.errors.messages[0].message").value(LOAN_ALREADY_EXISTS.getMessage()))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnBadRequestWhenPostLoanWhenBookIsUnavailable() throws Exception {
        final String uri = fromPath(LOAN_RESOURCE_PATH).toUriString();

        String content = super.getScenarioBody("shouldReturnBadRequestWhenPostLoanWhenBookIsUnavailable");

        mockMvc.perform(post(uri)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.messages").exists())
                .andExpect(jsonPath("$.errors.messages[0].code").value(BOOK_IS_NOT_AVAILABLE.getCode()))
                .andExpect(jsonPath("$.errors.messages[0].message").value(BOOK_IS_NOT_AVAILABLE.getMessage()))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnBadRequestWhenPostLoanWhenClientIdNotExists() throws Exception {
        final String uri = fromPath(LOAN_RESOURCE_PATH).toUriString();

        String content = super.getScenarioBody("shouldReturnBadRequestWhenPostLoanWhenClientIdNotExists");

        mockMvc.perform(post(uri)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.messages").exists())
                .andExpect(jsonPath("$.errors.messages[0].code").value(CLIENT_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.errors.messages[0].message").value(CLIENT_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnBadRequestWhenPostLoanWhenBookIdNotExists() throws Exception {
        final String uri = fromPath(LOAN_RESOURCE_PATH).toUriString();

        String content = super.getScenarioBody("shouldReturnBadRequestWhenPostLoanWhenBookIdNotExists");

        mockMvc.perform(post(uri)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.messages").exists())
                .andExpect(jsonPath("$.errors.messages[0].code").value(BOOK_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.errors.messages[0].message").value(BOOK_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnAcceptedWhenPostDevolution() throws Exception {
        final String uri = fromPath(LOAN_RESOURCE_PATH).toUriString();

        String content = super.getScenarioBody("shouldReturnAcceptedWhenPostDevolution");

        MvcResult result = mockMvc.perform(post(uri)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, containsString(uri)))
                .andReturn();

        String location = result.getResponse().getHeader(LOCATION);
        String loanId = super.getLoanIdOfLocation(location);

        mockMvc.perform(get(location)
                        .contentType(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(loanId))
                .andExpect(jsonPath("$.status").value(BORROWED.name()))
                .andExpect(jsonPath("$.book.id").value("a5993416-4255-11ec-71d3-0242ac130009"))
                .andExpect(jsonPath("$.book.title").value("Kubernetes"))
                .andExpect(jsonPath("$.book.author").value("Jason William"))
                .andExpect(jsonPath("$.book.yearPublication").value("2019"))
                .andExpect(jsonPath("$.book.status").value(UNAVAILABLE.name()))
                .andExpect(jsonPath("$.client.id").value("a5993416-4255-11ec-71d3-0242ac130006"))
                .andExpect(jsonPath("$.client.name").value("João Viel"))
                .andExpect(jsonPath("$.client.email").value("joaoviel@outlook.com"))
                .andExpect(jsonPath("$.client.documentType").value("CPF"))
                .andExpect(jsonPath("$.client.documentValue").value("74185296345"))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));

        final String uriDevolution = fromPath(LOAN_DEVOLUTION_RESOURCE_PATH).buildAndExpand(loanId).toUriString();

        mockMvc.perform(post(uriDevolution)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isAccepted())
                .andReturn();

        mockMvc.perform(get(location)
                        .contentType(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(loanId))
                .andExpect(jsonPath("$.status").value(RETURNED.name()))
                .andExpect(jsonPath("$.book.id").value("a5993416-4255-11ec-71d3-0242ac130009"))
                .andExpect(jsonPath("$.book.title").value("Kubernetes"))
                .andExpect(jsonPath("$.book.author").value("Jason William"))
                .andExpect(jsonPath("$.book.yearPublication").value("2019"))
                .andExpect(jsonPath("$.book.status").value(AVAILABLE.name()))
                .andExpect(jsonPath("$.client.id").value("a5993416-4255-11ec-71d3-0242ac130006"))
                .andExpect(jsonPath("$.client.name").value("João Viel"))
                .andExpect(jsonPath("$.client.email").value("joaoviel@outlook.com"))
                .andExpect(jsonPath("$.client.documentType").value("CPF"))
                .andExpect(jsonPath("$.client.documentValue").value("74185296345"))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnBadRequestWhenPostDevolutionWhenLoanIdNotExists() throws Exception {
        final String uri = fromPath(LOAN_DEVOLUTION_RESOURCE_PATH).buildAndExpand("INVALID_LOAN_ID").toUriString();

        mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.messages").exists())
                .andExpect(jsonPath("$.errors.messages[0].code").value(LOAN_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.errors.messages[0].message").value(LOAN_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnAcceptedWhenPostDevolutionButLoanAlreadyReturned() throws Exception {
        final String uri = fromPath(LOAN_RESOURCE_PATH).toUriString();

        String content = super.getScenarioBody("shouldReturnAcceptedWhenPostDevolutionButLoanAlreadyReturned");

        MvcResult result = mockMvc.perform(post(uri)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, containsString(uri)))
                .andReturn();

        String location = result.getResponse().getHeader(LOCATION);
        String loanId = super.getLoanIdOfLocation(location);

        mockMvc.perform(get(location)
                        .contentType(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(loanId))
                .andExpect(jsonPath("$.status").value(BORROWED.name()))
                .andExpect(jsonPath("$.book.id").value("a5993416-4255-11ec-71d3-0242ac130010"))
                .andExpect(jsonPath("$.book.title").value("Docker"))
                .andExpect(jsonPath("$.book.author").value("Jason William"))
                .andExpect(jsonPath("$.book.yearPublication").value("2020"))
                .andExpect(jsonPath("$.book.status").value(UNAVAILABLE.name()))
                .andExpect(jsonPath("$.client.id").value("a5993416-4255-11ec-71d3-0242ac130006"))
                .andExpect(jsonPath("$.client.name").value("João Viel"))
                .andExpect(jsonPath("$.client.email").value("joaoviel@outlook.com"))
                .andExpect(jsonPath("$.client.documentType").value("CPF"))
                .andExpect(jsonPath("$.client.documentValue").value("74185296345"))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));

        final String uriDevolution = fromPath(LOAN_DEVOLUTION_RESOURCE_PATH).buildAndExpand(loanId).toUriString();

        mockMvc.perform(post(uriDevolution)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isAccepted())
                .andReturn();

        mockMvc.perform(get(location)
                        .contentType(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(loanId))
                .andExpect(jsonPath("$.status").value(RETURNED.name()))
                .andExpect(jsonPath("$.book.id").value("a5993416-4255-11ec-71d3-0242ac130010"))
                .andExpect(jsonPath("$.book.title").value("Docker"))
                .andExpect(jsonPath("$.book.author").value("Jason William"))
                .andExpect(jsonPath("$.book.yearPublication").value("2020"))
                .andExpect(jsonPath("$.book.status").value(AVAILABLE.name()))
                .andExpect(jsonPath("$.client.id").value("a5993416-4255-11ec-71d3-0242ac130006"))
                .andExpect(jsonPath("$.client.name").value("João Viel"))
                .andExpect(jsonPath("$.client.email").value("joaoviel@outlook.com"))
                .andExpect(jsonPath("$.client.documentType").value("CPF"))
                .andExpect(jsonPath("$.client.documentValue").value("74185296345"))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));

        mockMvc.perform(post(uriDevolution)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.messages").exists())
                .andExpect(jsonPath("$.errors.messages[0].code").value(LOAN_ALREADY_RETURNED.getCode()))
                .andExpect(jsonPath("$.errors.messages[0].message").value(LOAN_ALREADY_RETURNED.getMessage()))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnOkWhenGetLoanByClientId() throws Exception {
        final String uri = fromPath(LOAN_RESOURCE_PATH)
                .toUriString().concat(";clientId=a5993416-4255-11ec-71d3-0242ac130004");

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.loans[0].id").value("a5993416-4255-11ec-71d3-0242ac130004"))
                .andExpect(jsonPath("$._embedded.loans[0].status").value(BORROWED.name()))
                .andExpect(jsonPath("$._embedded.loans[0].book.id").value("a5993416-4255-11ec-71d3-0242ac130007"))
                .andExpect(jsonPath("$._embedded.loans[0].book.title").value("Observability Engineering"))
                .andExpect(jsonPath("$._embedded.loans[0].book.author").value("Liz Fong"))
                .andExpect(jsonPath("$._embedded.loans[0].book.yearPublication").value("2022"))
                .andExpect(jsonPath("$._embedded.loans[0].book.status").value(UNAVAILABLE.name()))
                .andExpect(jsonPath("$._embedded.loans[0].client.id").value("a5993416-4255-11ec-71d3-0242ac130004"))
                .andExpect(jsonPath("$._embedded.loans[0].client.name").value("Renato Virto"))
                .andExpect(jsonPath("$._embedded.loans[0].client.email").value("renatovirtomoreira@outlook.com"))
                .andExpect(jsonPath("$._embedded.loans[0].client.documentType").value("CPF"))
                .andExpect(jsonPath("$._embedded.loans[0].client.documentValue").value("12345678998"))
                .andExpect(jsonPath("$._embedded.loans[0]._links['self'].href").value(containsString(LOAN_RESOURCE_PATH.concat("/a5993416-4255-11ec-71d3-0242ac130004"))))
                .andExpect(jsonPath("$.page.size").value(10))
                .andExpect(jsonPath("$.page.totalElements").value(3))
                .andExpect(jsonPath("$.page.totalPages").value(1))
                .andExpect(jsonPath("$.page.number").value(0));
    }

    @Test
    public void shouldReturnOkWhenGetLoanByBookId() throws Exception {
        final String uri = fromPath(LOAN_RESOURCE_PATH)
                .toUriString().concat(";bookId=a5993416-4255-11ec-71d3-0242ac130007");

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.loans[0].id").value("a5993416-4255-11ec-71d3-0242ac130004"))
                .andExpect(jsonPath("$._embedded.loans[0].status").value(BORROWED.name()))
                .andExpect(jsonPath("$._embedded.loans[0].book.id").value("a5993416-4255-11ec-71d3-0242ac130007"))
                .andExpect(jsonPath("$._embedded.loans[0].book.title").value("Observability Engineering"))
                .andExpect(jsonPath("$._embedded.loans[0].book.author").value("Liz Fong"))
                .andExpect(jsonPath("$._embedded.loans[0].book.yearPublication").value("2022"))
                .andExpect(jsonPath("$._embedded.loans[0].book.status").value(UNAVAILABLE.name()))
                .andExpect(jsonPath("$._embedded.loans[0].client.id").value("a5993416-4255-11ec-71d3-0242ac130004"))
                .andExpect(jsonPath("$._embedded.loans[0].client.name").value("Renato Virto"))
                .andExpect(jsonPath("$._embedded.loans[0].client.email").value("renatovirtomoreira@outlook.com"))
                .andExpect(jsonPath("$._embedded.loans[0].client.documentType").value("CPF"))
                .andExpect(jsonPath("$._embedded.loans[0].client.documentValue").value("12345678998"))
                .andExpect(jsonPath("$._embedded.loans[0]._links['self'].href").value(containsString(LOAN_RESOURCE_PATH.concat("/a5993416-4255-11ec-71d3-0242ac130004"))))
                .andExpect(jsonPath("$.page.size").value(10))
                .andExpect(jsonPath("$.page.totalElements").value(1))
                .andExpect(jsonPath("$.page.totalPages").value(1))
                .andExpect(jsonPath("$.page.number").value(0));
    }

    @Test
    public void shouldReturnOkWhenGetLoanByStatus() throws Exception {
        final String uri = fromPath(LOAN_RESOURCE_PATH)
                .toUriString().concat(";status=RETURNED");

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.loans[0].id").value("a5993416-4255-11ec-71d3-0242ac130006"))
                .andExpect(jsonPath("$._embedded.loans[0].status").value(RETURNED.name()))
                .andExpect(jsonPath("$._embedded.loans[0].book.id").value("a5993416-4255-11ec-71d3-0242ac130004"))
                .andExpect(jsonPath("$._embedded.loans[0].book.title").value("Clean Code"))
                .andExpect(jsonPath("$._embedded.loans[0].book.author").value("Robert Cecil Martin"))
                .andExpect(jsonPath("$._embedded.loans[0].book.yearPublication").value("2008"))
                .andExpect(jsonPath("$._embedded.loans[0].book.status").value(AVAILABLE.name()))
                .andExpect(jsonPath("$._embedded.loans[0].client.id").value("a5993416-4255-11ec-71d3-0242ac130004"))
                .andExpect(jsonPath("$._embedded.loans[0].client.name").value("Renato Virto"))
                .andExpect(jsonPath("$._embedded.loans[0].client.email").value("renatovirtomoreira@outlook.com"))
                .andExpect(jsonPath("$._embedded.loans[0].client.documentType").value("CPF"))
                .andExpect(jsonPath("$._embedded.loans[0].client.documentValue").value("12345678998"))
                .andExpect(jsonPath("$._embedded.loans[0]._links['self'].href").value(containsString(LOAN_RESOURCE_PATH.concat("/a5993416-4255-11ec-71d3-0242ac130006"))))
                .andExpect(jsonPath("$.page.size").value(10))
                .andExpect(jsonPath("$.page.totalElements").value(1))
                .andExpect(jsonPath("$.page.totalPages").value(1))
                .andExpect(jsonPath("$.page.number").value(0));
    }

    @Test
    public void shouldReturnOkWhenGetLoanByClientIdAndBookId() throws Exception {
        final String uri = fromPath(LOAN_RESOURCE_PATH)
                .toUriString().concat(";clientId=a5993416-4255-11ec-71d3-0242ac130004;bookId=a5993416-4255-11ec-71d3-0242ac130007");

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.loans[0].id").value("a5993416-4255-11ec-71d3-0242ac130004"))
                .andExpect(jsonPath("$._embedded.loans[0].status").value(BORROWED.name()))
                .andExpect(jsonPath("$._embedded.loans[0].book.id").value("a5993416-4255-11ec-71d3-0242ac130007"))
                .andExpect(jsonPath("$._embedded.loans[0].book.title").value("Observability Engineering"))
                .andExpect(jsonPath("$._embedded.loans[0].book.author").value("Liz Fong"))
                .andExpect(jsonPath("$._embedded.loans[0].book.yearPublication").value("2022"))
                .andExpect(jsonPath("$._embedded.loans[0].book.status").value(UNAVAILABLE.name()))
                .andExpect(jsonPath("$._embedded.loans[0].client.id").value("a5993416-4255-11ec-71d3-0242ac130004"))
                .andExpect(jsonPath("$._embedded.loans[0].client.name").value("Renato Virto"))
                .andExpect(jsonPath("$._embedded.loans[0].client.email").value("renatovirtomoreira@outlook.com"))
                .andExpect(jsonPath("$._embedded.loans[0].client.documentType").value("CPF"))
                .andExpect(jsonPath("$._embedded.loans[0].client.documentValue").value("12345678998"))
                .andExpect(jsonPath("$._embedded.loans[0]._links['self'].href").value(containsString(LOAN_RESOURCE_PATH.concat("/a5993416-4255-11ec-71d3-0242ac130004"))))
                .andExpect(jsonPath("$.page.size").value(10))
                .andExpect(jsonPath("$.page.totalElements").value(1))
                .andExpect(jsonPath("$.page.totalPages").value(1))
                .andExpect(jsonPath("$.page.number").value(0));
    }

    @Test
    public void shouldReturnOkWhenGetLoanByClientIdAndBookIdIsInvalid() throws Exception {
        final String uri = fromPath(LOAN_RESOURCE_PATH)
                .toUriString().concat(";clientId=a5993416-4255-11ec-71d3-0242ac130004;bookId=INVALID_BOOK_ID");

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.size").value(10))
                .andExpect(jsonPath("$.page.totalElements").value(0))
                .andExpect(jsonPath("$.page.totalPages").value(0))
                .andExpect(jsonPath("$.page.number").value(0));
    }

    @Test
    public void shouldReturnOkWhenGetLoanByClientIsInvalidIdAndBookId() throws Exception {
        final String uri = fromPath(LOAN_RESOURCE_PATH)
                .toUriString().concat(";clientId=INVALID_CLIENT_ID;bookId=a5993416-4255-11ec-71d3-0242ac130007");

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.size").value(10))
                .andExpect(jsonPath("$.page.totalElements").value(0))
                .andExpect(jsonPath("$.page.totalPages").value(0))
                .andExpect(jsonPath("$.page.number").value(0));
    }

    @Test
    public void shouldReturnBadRequestWhenGetLoanByStatusIsInvalidId() throws Exception {
        final String uri = fromPath(LOAN_RESOURCE_PATH)
                .toUriString().concat(";status=INVALID_STATUS");

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.messages").exists())
                .andExpect(jsonPath("$.errors.messages[0].code").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnBadRequestWhenGetLoanByInvalidMatrixVariable() throws Exception {
        final String uri = fromPath(LOAN_RESOURCE_PATH).toUriString().concat(";invalid=invalid");

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.messages").exists())
                .andExpect(jsonPath("$.errors.messages[0].code").value(REQUIRED_MATRIX_PARAM.getCode()))
                .andExpect(jsonPath("$.errors.messages[0].message").value(String.format(REQUIRED_MATRIX_PARAM.getMessage(),
                        "clientId or bookId or status")))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }
}
