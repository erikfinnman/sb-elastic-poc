package com.sb_elastic.poc.controller;

import com.sb_elastic.poc.TestConfig;
import com.sb_elastic.poc.storage.entities.PersonDocument;
import com.sb_elastic.poc.storage.repositories.PersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)
@Import(TestConfig.class)
class PersonControllerTest {

    @MockBean
    private PersonRepository personRepository;

    @Autowired
    private MockMvc client;

    @Test
    void testGetPerson() throws Exception {
        var id = "123";
        var firstName = "Erik";
        var lastName = "Finnman";

        var personDocument = new PersonDocument();
        personDocument.setId(id);
        personDocument.setFirstName(firstName);
        personDocument.setLastName(lastName);
        var expectedPerson = Optional.of(personDocument);
        when(personRepository.findById(id)).thenReturn(expectedPerson);

        client.perform(get("/person/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("firstName").value(firstName))
                .andExpect(jsonPath("lastName").value(lastName));
    }

    @Test
    void testGetNonExistingPerson() throws Exception {
        var id = "123";
        when(personRepository.findById(id)).thenReturn(Optional.empty());

        client.perform(get("/person/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetPersons() throws Exception {
        var id = "123";
        var firstName = "Erik";
        var lastName = "Finnman";

        var personDocument = new PersonDocument();
        personDocument.setId(id);
        personDocument.setFirstName(firstName);
        personDocument.setLastName(lastName);
        when(personRepository.findAll()).thenReturn(List.of(personDocument));

        client.perform(get("/persons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id))
                .andExpect(jsonPath("$[0].firstName").value(firstName))
                .andExpect(jsonPath("$[0].lastName").value(lastName));
    }

    @Test
    void testCreatePerson() throws Exception {
        var id = "123";
        var firstName = "Erik";
        var lastName = "Finnman";

        var personDocument = new PersonDocument();
        personDocument.setId(id);
        personDocument.setFirstName(firstName);
        personDocument.setLastName(lastName);
        when(personRepository.save(any())).thenReturn(personDocument);

        var person = """
                {
                "firstName": "Erik",
                "lastName": "Finnman"
                }
                """;

        client.perform(post("/person")
                        .content(person)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("firstName").value(firstName))
                .andExpect(jsonPath("lastName").value(lastName));
    }

    @Test
    void testUpdatePerson() throws Exception {
        var id = "123";
        var firstName = "Erik";
        var lastName = "Finnman";

        var personDocument = new PersonDocument();
        personDocument.setId(id);
        personDocument.setFirstName(firstName);
        personDocument.setLastName(lastName);
        when(personRepository.findById(any())).thenReturn(Optional.of(personDocument));
        when(personRepository.save(any())).thenReturn(personDocument);

        var person = """
                {
                "firstName": "Erik2",
                "lastName": "Finnman2"
                }
                """;

        client.perform(put("/person/" + id)
                        .content(person)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("firstName").value("Erik2"))
                .andExpect(jsonPath("lastName").value("Finnman2"));
    }

    @Test
    void testUpdatePerson_NotFound() throws Exception {
        when(personRepository.findById(any())).thenReturn(Optional.empty());

        var person = """
                {
                "firstName": "Erik",
                "lastName": "Finnman"
                }
                """;

        client.perform(put("/person/123")
                        .content(person)
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeletePerson() throws Exception {
        var id = "123";
        var firstName = "Erik";
        var lastName = "Finnman";

        var personDocument = new PersonDocument();
        personDocument.setId(id);
        personDocument.setFirstName(firstName);
        personDocument.setLastName(lastName);
        when(personRepository.findById(any())).thenReturn(Optional.of(personDocument));

        client.perform(MockMvcRequestBuilders.delete("/person/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeletePerson_NotFound() throws Exception {
        when(personRepository.findById(any())).thenReturn(Optional.empty());

        client.perform(MockMvcRequestBuilders.delete("/person/123"))
                .andExpect(status().isNotFound());
    }
}