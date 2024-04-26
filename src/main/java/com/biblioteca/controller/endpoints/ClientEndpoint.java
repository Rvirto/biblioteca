package com.biblioteca.controller.endpoints;

import com.biblioteca.controller.assemblers.ClientAssembler;
import com.biblioteca.controller.models.request.ClientRequestModel;
import com.biblioteca.controller.models.response.ClientResponseModel;
import com.biblioteca.model.entities.Client;
import com.biblioteca.model.enumeration.ExceptionMessagesEnum;
import com.biblioteca.model.exceptions.ConflictException;
import com.biblioteca.model.exceptions.NotFoundException;
import com.biblioteca.model.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.biblioteca.model.enumeration.ExceptionMessagesEnum.CLIENT_ALREADY_EXISTS;

@RestController
public class ClientEndpoint {

    public static final String CLIENT_RESOURCE_PATH = "/api/clients";
    public static final String CLIENT_SELF_PATH = CLIENT_RESOURCE_PATH + "/{clientId}";

    private final ClientService clientService;
    private final ClientAssembler clientAssembler;

    @Autowired
    public ClientEndpoint(ClientService clientService, ClientAssembler clientAssembler) {
        this.clientService = clientService;
        this.clientAssembler = clientAssembler;
    }

    @GetMapping(CLIENT_SELF_PATH)
    public ResponseEntity<ClientResponseModel> getById(@PathVariable("clientId") final String clientId) {
        final Client client = clientService.findById(clientId).orElseThrow(() -> new NotFoundException(ExceptionMessagesEnum.CLIENT_NOT_FOUND));
        return ResponseEntity.ok().body(clientAssembler.toModel(client));
    }

    @PostMapping(CLIENT_RESOURCE_PATH)
    public ResponseEntity<?> create(@RequestBody @Valid ClientRequestModel clientRequestModel) {
        Client client = clientAssembler.toEntity(clientRequestModel);

        clientService.findClient(client).stream().findFirst().ifPresent(searchedClient -> {
            throw new ConflictException(
                    CLIENT_ALREADY_EXISTS, clientAssembler
                    .buildClientSelfLink(searchedClient.getId()).toUri());
        });

        client = clientService.create(client);
        return ResponseEntity.created(clientAssembler.buildClientSelfLink(client.getId()).toUri()).build();
    }
}
