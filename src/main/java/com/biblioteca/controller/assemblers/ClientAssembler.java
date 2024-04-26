package com.biblioteca.controller.assemblers;

import com.biblioteca.controller.endpoints.ClientEndpoint;
import com.biblioteca.controller.models.request.ClientRequestModel;
import com.biblioteca.controller.models.response.ClientResponseModel;
import com.biblioteca.model.entities.Client;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ClientAssembler extends RepresentationModelAssemblerSupport<Client, ClientResponseModel> {

    public ClientAssembler() {
        super(ClientEndpoint.class, ClientResponseModel.class);
    }

    public Link buildClientSelfLink(String clientId) {
        return linkTo(methodOn(ClientEndpoint.class).getById(clientId)).withSelfRel();
    }

    @Override
    public ClientResponseModel toModel(Client client) {
        final ClientResponseModel model = new ClientResponseModel(client);
        model.add(buildClientSelfLink(client.getId()));
        return model;
    }

    public Client toEntity(ClientRequestModel clientRequestModel) {
        Client client = new Client();
        client.setName(clientRequestModel.getName());
        client.setEmail(clientRequestModel.getEmail());
        client.setDocumentType(clientRequestModel.getDocumentType());
        client.setDocumentValue(clientRequestModel.getDocumentValue());
        return client;
    }
}