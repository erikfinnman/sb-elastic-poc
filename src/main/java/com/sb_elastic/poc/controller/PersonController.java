package com.sb_elastic.poc.controller;

import com.sb_elastic.poc.model.CreatePerson;
import com.sb_elastic.poc.model.Person;
import com.sb_elastic.poc.storage.entities.PersonDocument;
import com.sb_elastic.poc.storage.repositories.PersonRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonRepository personRepository;
    private final Counter getPersonCounter;
    private final Counter createPersonCounter;

    public PersonController(PersonRepository personRepository, MeterRegistry meterRegistry) {
        this.personRepository = personRepository;
        this.getPersonCounter = meterRegistry.counter("person.get.counter");
        this.createPersonCounter = meterRegistry.counter("person.create.counter");
    }

    @GetMapping("/{personId}")
    public Optional<Person> getPerson(@PathVariable String personId) {
        var person = this.personRepository.findById(personId);
        this.getPersonCounter.increment();
        return person.map(personDocument -> new Person(personDocument.getId(), personDocument.getFirstName(), personDocument.getLastName()));
    }

    @PostMapping("")
    public Person createPerson(@RequestBody CreatePerson person) {
        var personDocument = new PersonDocument();
        personDocument.setFirstName(person.firstName());
        personDocument.setLastName(person.lastName());
        personDocument = this.personRepository.save(personDocument);
        this.createPersonCounter.increment();
        return new Person(personDocument.getId(), personDocument.getFirstName(), personDocument.getLastName());
    }

}
