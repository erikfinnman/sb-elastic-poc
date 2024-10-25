package com.sb_elastic.poc.storage.repositories;

import com.sb_elastic.poc.storage.entities.PersonDocument;
import org.springframework.data.repository.ListCrudRepository;

public interface PersonRepository extends ListCrudRepository<PersonDocument, Long> {
}
