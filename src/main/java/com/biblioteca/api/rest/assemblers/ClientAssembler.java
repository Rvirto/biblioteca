package com.biblioteca.api.rest.assemblers;

import com.biblioteca.api.rest.endpoints.ClientEndpoint;
import com.biblioteca.api.rest.models.request.ClientRequestModel;
import com.biblioteca.api.rest.models.response.ClientResponseModel;
import com.biblioteca.domain.entities.Client;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Client Assembler class used to work with input and output of endpoint information
 *
 * @author Renato Virto (renatovirtomoreira@outlook.com)
 * @since 1.0.0
 */
@Component
public class ClientAssembler extends RepresentationModelAssemblerSupport<Client, ClientResponseModel> {

    public ClientAssembler() {
        super(ClientEndpoint.class, ClientResponseModel.class);
    }

    /**
     * Method used for a link with the client ID
     * @param clientId
     * @return
     */
    public Link buildClientSelfLink(String clientId) {
        return linkTo(methodOn(ClientEndpoint.class).getById(clientId)).withSelfRel();
    }

    /**
     * Method used to create a client return
     * @param client
     * @return
     */
    @Override
    public ClientResponseModel toModel(Client client) {
        final ClientResponseModel model = new ClientResponseModel(client);
        model.add(buildClientSelfLink(client.getId()));
        return model;
    }

    /**
     * Method used for a client entity based on a requestModel
     * @param clientRequestModel
     * @return
     */
    public Client toEntity(ClientRequestModel clientRequestModel) {
        Client client = new Client();
        client.setName(clientRequestModel.getName());
        client.setEmail(clientRequestModel.getEmail());
        client.setDocumentType(clientRequestModel.getDocumentType());
        client.setDocumentValue(clientRequestModel.getDocumentValue());
        return client;
    }
}