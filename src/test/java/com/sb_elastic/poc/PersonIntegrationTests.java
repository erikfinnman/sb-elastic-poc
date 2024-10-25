package com.sb_elastic.poc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb_elastic.poc.storage.entities.PersonDocument;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PersonIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testPerson() throws JsonProcessingException {
        var body = """
                {
                "firstName": "Erik",
                "lastName": "Finnman"
                }
                """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> httpEntity = new HttpEntity<>(body, headers);
        var response = this.restTemplate.postForObject(
                "http://localhost:" + port + "/person",
                httpEntity,
                String.class);

        var objectmapper = new ObjectMapper();
        var person = objectmapper.readValue(response, PersonDocument.class);

        assertThat(person.getFirstName()).isEqualTo("Erik");
        assertThat(person.getLastName()).isEqualTo("Finnman");

        var putBody = """
                {
                "firstName": "Erik2",
                "lastName": "Finnman2"
                }
                """;
        httpEntity = new HttpEntity<>(putBody, headers);
        this.restTemplate.put("http://localhost:" + port + "/person/" + person.getId(),
                httpEntity);
        response = this.restTemplate.getForObject(
                "http://localhost:" + port + "/person/" + person.getId(),
                String.class);
        var updatedPerson = objectmapper.readValue(response, PersonDocument.class);

        assertThat(updatedPerson.getFirstName()).isEqualTo("Erik2");
        assertThat(updatedPerson.getLastName()).isEqualTo("Finnman2");
    }
}
