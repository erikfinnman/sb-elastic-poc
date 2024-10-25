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
}