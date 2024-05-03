package com.biblioteca.api.rest.endpoints;

import com.biblioteca.ApplicationTests;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static com.biblioteca.api.rest.endpoints.ClientEndpoint.CLIENT_RESOURCE_PATH;
import static com.biblioteca.api.rest.endpoints.ClientEndpoint.CLIENT_SELF_PATH;
import static com.biblioteca.domain.enumeration.ExceptionMessagesEnum.CLIENT_ALREADY_EXISTS;
import static com.biblioteca.domain.enumeration.ExceptionMessagesEnum.CLIENT_NOT_FOUND;
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
 * Client Endpoint Test class for validations of all existing endpoints and their returns from the Client endpoint class
 *
 * @author Renato Virto (renatovirtomoreira@outlook.com)
 * @since 1.0.0
 */
public class ClientEndpointTest extends ApplicationTests<ClientEndpointTest> {

    @Test
    public void shouldReturnOkWhenGetClientById() throws Exception {

        final String uri = fromPath(CLIENT_SELF_PATH).buildAndExpand("a5993416-4255-11ec-71d3-0242ac130004").toUriString();

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("a5993416-4255-11ec-71d3-0242ac130004"))
                .andExpect(jsonPath("$.name").value("Renato Virto"))
                .andExpect(jsonPath("$.email").value("renatovirtomoreira@outlook.com"))
                .andExpect(jsonPath("$.documentType").value("CPF"))
                .andExpect(jsonPath("$.documentValue").value("12345678998"))
                .andExpect(jsonPath("$.creationDate").value("2024-04-23T23:01:40.619-03:00"))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnNotFoundWhenGetClientByIdThatDoesntExist() throws Exception {

        final String uri = fromPath(CLIENT_SELF_PATH).buildAndExpand("CLIENT_ID_NOT_EXISTS").toUriString();

        mockMvc.perform(get(uri)
                        .contentType(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors.messages").exists())
                .andExpect(jsonPath("$.errors.messages[0].code").value(CLIENT_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.errors.messages[0].message").value(CLIENT_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnCreatedWhenPostClient() throws Exception {
        final String uri = fromPath(CLIENT_RESOURCE_PATH).toUriString();

        String content = super.getScenarioBody("shouldReturnCreatedWhenPostClient");

        MvcResult result = mockMvc.perform(post(uri)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, containsString(uri)))
                .andReturn();

        String location = result.getResponse().getHeader(LOCATION);
        String clientId = super.getClientIdOfLocation(location);

        mockMvc.perform(get(location)
                        .contentType(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(clientId))
                .andExpect(jsonPath("$.name").value("Ruan Felipe"))
                .andExpect(jsonPath("$.email").value("ruanfelipe@outlook.com"))
                .andExpect(jsonPath("$.documentType").value("CPF"))
                .andExpect(jsonPath("$.documentValue").value("85274196387"))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnBadRequestWhenPostClientWithoutName() throws Exception {
        final String uri = fromPath(CLIENT_RESOURCE_PATH).toUriString();

        String content = super.getScenarioBody("shouldReturnBadRequestWhenPostClientWithoutName");

        mockMvc.perform(post(uri)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.messages").exists())
                .andExpect(jsonPath("$.errors.messages[0].code").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errors.messages[0].message").value("name must not be blank"))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnBadRequestWhenPostClientWithoutEmail() throws Exception {
        final String uri = fromPath(CLIENT_RESOURCE_PATH).toUriString();

        String content = super.getScenarioBody("shouldReturnBadRequestWhenPostClientWithoutEmail");

        mockMvc.perform(post(uri)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.messages").exists())
                .andExpect(jsonPath("$.errors.messages[0].code").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errors.messages[0].message").value("email must not be blank"))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnBadRequestWhenPostClientWithoutDocumentType() throws Exception {
        final String uri = fromPath(CLIENT_RESOURCE_PATH).toUriString();

        String content = super.getScenarioBody("shouldReturnBadRequestWhenPostClientWithoutDocumentType");

        mockMvc.perform(post(uri)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.messages").exists())
                .andExpect(jsonPath("$.errors.messages[0].code").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errors.messages[0].message").value("documentType must not be blank"))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnBadRequestWhenPostClientWithoutDocumentValue() throws Exception {
        final String uri = fromPath(CLIENT_RESOURCE_PATH).toUriString();

        String content = super.getScenarioBody("shouldReturnBadRequestWhenPostClientWithoutDocumentValue");

        mockMvc.perform(post(uri)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.messages").exists())
                .andExpect(jsonPath("$.errors.messages[0].code").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errors.messages[0].message").value("documentValue must not be blank"))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnConflictWhenPostClientAlreadyExists() throws Exception {
        final String uri = fromPath(CLIENT_RESOURCE_PATH).toUriString();

        String content = super.getScenarioBody("shouldReturnConflictWhenPostClientAlreadyExists");

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
                .andExpect(jsonPath("$.errors.messages[0].code").value(CLIENT_ALREADY_EXISTS.getCode()))
                .andExpect(jsonPath("$.errors.messages[0].message").value(CLIENT_ALREADY_EXISTS.getMessage()))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }
}
