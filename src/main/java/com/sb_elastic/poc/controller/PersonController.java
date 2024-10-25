package com.sb_elastic.poc.controller;

import com.sb_elastic.poc.model.Person;
import com.sb_elastic.poc.model.PostPerson;
import com.sb_elastic.poc.storage.entities.PersonDocument;
import com.sb_elastic.poc.storage.repositories.PersonRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/")
public class PersonController {

    private final PersonRepository personRepository;
    private final Counter getPersonCounter;
    private final Counter createPersonCounter;
    private final Counter updatePersonCounter;

    public PersonController(PersonRepository personRepository, MeterRegistry meterRegistry) {
        this.personRepository = personRepository;
        this.getPersonCounter = meterRegistry.counter("person.get.counter");
        this.createPersonCounter = meterRegistry.counter("person.create.counter");
        this.updatePersonCounter = meterRegistry.counter("person.update.counter");
    }

    // This should really use pagination, but it returns a List for this simple example
    @GetMapping("/persons")
    public List<Person> listPersons() {
        return this.personRepository.findAll()
                .stream()
                .map(personDocument -> new Person(personDocument.getId(), personDocument.getFirstName(), personDocument.getLastName()))
                .collect(java.util.stream.Collectors.toList());
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved person"),
            @ApiResponse(responseCode = "404", description = "Person not found")
    })
    @GetMapping("/person/{personId}")
    public Person getPerson(@PathVariable String personId) {
        var person = this.personRepository.findById(personId);
        this.getPersonCounter.increment();
        return person.map(personDocument -> new Person(personDocument.getId(), personDocument.getFirstName(), personDocument.getLastName()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/person")
    public Person postPerson(@RequestBody PostPerson person) {
        var personDocument = new PersonDocument();
        personDocument.setFirstName(person.firstName());
        personDocument.setLastName(person.lastName());
        personDocument = this.personRepository.save(personDocument);
        this.createPersonCounter.increment();
        return new Person(personDocument.getId(), personDocument.getFirstName(), personDocument.getLastName());
    }

    @PutMapping("/person/{personId}")
    public Person putPerson(@PathVariable String personId, @RequestBody PostPerson person) {
        return this.personRepository.findById(personId).map(personDocument -> {
            personDocument.setFirstName(person.firstName());
            personDocument.setLastName(person.lastName());
            personDocument = this.personRepository.save(personDocument);
            this.updatePersonCounter.increment();
            return new Person(personDocument.getId(), personDocument.getFirstName(), personDocument.getLastName());
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted person"),
            @ApiResponse(responseCode = "404", description = "Person not found")
    })
    @DeleteMapping("/person/{personId}")
    public ResponseEntity<Object> deletePerson(@PathVariable String personId) {
        return this.personRepository.findById(personId).map(personDocument -> {
            this.personRepository.deleteById(personDocument.getId());
            return ResponseEntity.noContent().build();
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
