package com.sb_elastic.poc.controller;

import com.sb_elastic.poc.TestConfig;
import com.sb_elastic.poc.storage.entities.PersonDocument;
import com.sb_elastic.poc.storage.repositories.PersonRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

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
        Mockito.when(personRepository.findById(id)).thenReturn(expectedPerson);

        client.perform(MockMvcRequestBuilders.get("/person/" + id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("firstName").value(firstName))
                .andExpect(MockMvcResultMatchers.jsonPath("lastName").value(lastName));
    }

    @Test
    void testGetNonExistingPerson() throws Exception {
        var id = "123";
        Mockito.when(personRepository.findById(id)).thenReturn(Optional.empty());

        client.perform(MockMvcRequestBuilders.get("/person/" + id))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
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
        Mockito.when(personRepository.findAll()).thenReturn(List.of(personDocument));

        client.perform(MockMvcRequestBuilders.get("/persons"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value(firstName))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName").value(lastName));
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
        Mockito.when(personRepository.save(Mockito.any())).thenReturn(personDocument);

        var person = """
                {
                "firstName": "Erik",
                "lastName": "Finnman"
                }
                """;

        client.perform(MockMvcRequestBuilders.post("/person")
                        .content(person)
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("firstName").value(firstName))
                .andExpect(MockMvcResultMatchers.jsonPath("lastName").value(lastName));
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
        Mockito.when(personRepository.findById(Mockito.any())).thenReturn(Optional.of(personDocument));
        Mockito.when(personRepository.save(Mockito.any())).thenReturn(personDocument);

        var person = """
                {
                "firstName": "Erik2",
                "lastName": "Finnman2"
                }
                """;

        client.perform(MockMvcRequestBuilders.put("/person/" + id)
                        .content(person)
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("firstName").value("Erik2"))
                .andExpect(MockMvcResultMatchers.jsonPath("lastName").value("Finnman2"));
    }

    @Test
    void testUpdatePerson_NotFound() throws Exception {
        Mockito.when(personRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        var person = """
                {
                "firstName": "Erik",
                "lastName": "Finnman"
                }
                """;

        client.perform(MockMvcRequestBuilders.put("/person/123")
                        .content(person)
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
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
        Mockito.when(personRepository.findById(Mockito.any())).thenReturn(Optional.of(personDocument));

        client.perform(MockMvcRequestBuilders.delete("/person/" + id))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void testDeletePerson_NotFound() throws Exception {
        Mockito.when(personRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        client.perform(MockMvcRequestBuilders.delete("/person/123"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}