package com.sb_elastic.poc.storage.repositories;

import com.sb_elastic.poc.storage.entities.PersonDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface PersonRepository extends ListCrudRepository<PersonDocument, String> {
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
