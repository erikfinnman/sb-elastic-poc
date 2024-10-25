package com.sb_elastic.poc.storage.repositories;

import com.sb_elastic.poc.TestcontainersConfiguration;
import com.sb_elastic.poc.storage.entities.PersonDocument;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.elasticsearch.DataElasticsearchTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestcontainersConfiguration.class)
@DataElasticsearchTest
class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Test
    void testStorePerson() {
        var person = new PersonDocument();
        person.setFirstName("Erik");
        person.setLastName("Finnman");

        var savedPerson = personRepository.save(person);

        assertThat(savedPerson.getId()).isNotNull();
        assertThat(savedPerson.getFirstName()).isEqualTo("Erik");
        assertThat(savedPerson.getLastName()).isEqualTo("Finnman");
    }
}