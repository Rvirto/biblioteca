package com.biblioteca.domain.repositories;

import com.biblioteca.domain.entities.BookVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookVersionRepository extends JpaRepository<BookVersion, String> {
}
