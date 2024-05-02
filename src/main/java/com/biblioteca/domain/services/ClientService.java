package com.biblioteca.domain.services;

import com.biblioteca.domain.entities.Client;
import com.biblioteca.domain.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Client create(Client client) {
        client = this.clientRepository.save(client);
        return client;
    }

    public List<Client> findClient(Client client) {
        return this.clientRepository.findAll(Example.of(client));
    }
}
