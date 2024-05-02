package com.biblioteca.api.rest.endpoints;

import com.biblioteca.api.rest.models.request.ClientRequestModel;
import com.biblioteca.api.rest.models.response.ClientResponseModel;
import com.biblioteca.api.rest.assemblers.ClientAssembler;
import com.biblioteca.domain.entities.Client;
import com.biblioteca.domain.enumeration.ExceptionMessagesEnum;
import com.biblioteca.domain.exceptions.ConflictException;
import com.biblioteca.domain.exceptions.NotFoundException;
import com.biblioteca.domain.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.biblioteca.domain.enumeration.ExceptionMessagesEnum.CLIENT_ALREADY_EXISTS;

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
