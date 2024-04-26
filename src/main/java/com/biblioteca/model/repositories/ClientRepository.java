package com.biblioteca.model.repositories;

import com.biblioteca.model.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, String> {
}
