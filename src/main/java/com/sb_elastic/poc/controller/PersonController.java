package com.sb_elastic.poc.controller;

import com.sb_elastic.poc.model.Person;
import com.sb_elastic.poc.storage.repositories.PersonRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonRepository personRepository;
    private final Counter getPersonCounter;

    public PersonController(PersonRepository personRepository, MeterRegistry meterRegistry) {
        this.personRepository = personRepository;
        this.getPersonCounter = meterRegistry.counter("person.get.counter");
    }

    @GetMapping("/{personId}")
    public Optional<Person> getPerson(@PathVariable String personId) {

        var person = this.personRepository.findById(personId);
        this.getPersonCounter.increment();
        return person.map(personDocument -> new Person(personDocument.getId(), personDocument.getFirstName(), personDocument.getLastName()));
    }

}
