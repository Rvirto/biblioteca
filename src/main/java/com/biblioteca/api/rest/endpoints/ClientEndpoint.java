package com.biblioteca.api.rest.endpoints;

import com.biblioteca.api.rest.assemblers.ClientAssembler;
import com.biblioteca.api.rest.models.request.ClientRequestModel;
import com.biblioteca.api.rest.models.response.ClientResponseModel;
import com.biblioteca.domain.entities.Client;
import com.biblioteca.domain.enumeration.ExceptionMessagesEnum;
import com.biblioteca.domain.exceptions.ConflictException;
import com.biblioteca.domain.exceptions.NotFoundException;
import com.biblioteca.domain.services.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.biblioteca.domain.enumeration.ExceptionMessagesEnum.CLIENT_ALREADY_EXISTS;

/**
 * Client Endpoint client access class for handling client
 *
 * @author Renato Virto (renatovirtomoreira@outlook.com)
 * @since 1.0.0
 */
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

    /**
     * Endpoint search by client ID
     * @param clientId
     * @return
     */
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Consultation carried out successfully"),
            @ApiResponse(responseCode = "404", description = "Client not found for Id informed"),
            @ApiResponse(responseCode = "500", description = "Internal application error")})
    @Operation(summary = "Client search endpoint by id")
    @GetMapping(CLIENT_SELF_PATH)
    public ResponseEntity<ClientResponseModel> getById(@PathVariable("clientId") final String clientId) {
        final Client client = clientService.findById(clientId).orElseThrow(() -> new NotFoundException(ExceptionMessagesEnum.CLIENT_NOT_FOUND));
        return ResponseEntity.ok().body(clientAssembler.toModel(client));
    }

    /**
     * Client creation endpoint
     * @param clientRequestModel
     * @return
     */
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Successfully created"),
            @ApiResponse(responseCode = "409", description = "Existing registration"),
            @ApiResponse(responseCode = "500", description = "Internal application error")})
    @Operation(summary = "Client creation endpoint")
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
