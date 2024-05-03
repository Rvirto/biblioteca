package com.biblioteca.domain.services;

import com.biblioteca.domain.entities.Client;
import com.biblioteca.domain.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Client Service class used to meet any need related to a client
 *
 * @author Renato Virto (renatovirtomoreira@outlook.com)
 * @since 1.0.0
 */
@Service
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }


    public Optional<Client> findById(String bookId) {
        return this.clientRepository.findById(bookId);
    }

    /**
     * Method used to insert new clients into the database
     * @param client
     * @return
     */
    public Client create(Client client) {
        client = this.clientRepository.save(client);
        return client;
    }

    public List<Client> findClient(Client client) {
        return this.clientRepository.findAll(Example.of(client));
    }
}
