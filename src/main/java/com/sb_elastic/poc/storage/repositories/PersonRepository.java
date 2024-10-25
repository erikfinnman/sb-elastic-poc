package com.sb_elastic.poc.storage.repositories;

import com.sb_elastic.poc.storage.entities.PersonDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.lang.NonNull;

import java.util.List;

public interface PersonRepository extends ListCrudRepository<PersonDocument, String> {

    // This override is apparently need if the method should return a "List"
    @NonNull
    List<PersonDocument> findAll();

    List<PersonDocument> findPersonDocumentByFirstName(String firstName);

    @Query("""
            {
              "wildcard": {
                "lastName": {
                  "value": "?0",
                  "boost": 1.0,
                  "rewrite": "constant_score"
                }
              }
            }
            """)
    List<PersonDocument> findPersonDocumentByLastNameWildcard(String lastName);
}
