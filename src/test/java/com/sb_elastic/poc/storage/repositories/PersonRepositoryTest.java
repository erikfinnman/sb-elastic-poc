package com.sb_elastic.poc.storage.repositories;

import com.sb_elastic.poc.TestcontainersConfiguration;
import com.sb_elastic.poc.storage.entities.PersonDocument;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
        person.setFirstName("Some");
        person.setLastName("Person");

        var savedPerson = personRepository.save(person);

        assertThat(savedPerson.getId()).isNotNull();
        assertThat(savedPerson.getFirstName()).isEqualTo("Some");
        assertThat(savedPerson.getLastName()).isEqualTo("Person");
    }

    @Test
    void testQueryFirstName() {
        var person = new PersonDocument();
        person.setFirstName("Some");
        person.setLastName("Person");
        personRepository.save(person);

        var persons = personRepository.findPersonDocumentByFirstName("Some");

        assertThat(persons.getFirst().getFirstName()).isEqualTo("Some");
        assertThat(persons.getFirst().getLastName()).isEqualTo("Person");
    }

    @ParameterizedTest
    @ValueSource(strings = {"*innman", "*nnman", "*man"})
    void testQueryLastNameContaining(String lastName) {
        var person = new PersonDocument();
        person.setFirstName("Erik");
        person.setLastName("Finnman");
        personRepository.save(person);

        var persons = personRepository.findPersonDocumentByLastNameWildcard(lastName);

        assertThat(persons.getFirst().getLastName()).isEqualTo("Finnman");
    }
    
    
}