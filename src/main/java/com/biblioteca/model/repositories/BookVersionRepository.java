package com.biblioteca.model.repositories;

import com.biblioteca.model.entities.BookVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookVersionRepository extends JpaRepository<BookVersion, String> {
}
