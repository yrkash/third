package ru.alishev.springcourse.third.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.alishev.springcourse.third.model.Person;
import ru.alishev.springcourse.third.repository.PersonRepository;
import ru.alishev.springcourse.third.security.PersonDetails;

import java.util.Optional;

@Service
public class PersonDetailsService implements UserDetailsService {


    private final PersonRepository personRepository;

    public PersonDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> optionalPerson = personRepository.findByUsername(username);
        if(optionalPerson.isEmpty())
            throw new UsernameNotFoundException("User not found!");
        return new PersonDetails(optionalPerson.get());
    }
    public Optional<Person> loadOptionalOfUserByUsername(String username) {
        Optional<Person> person = personRepository.findByUsername(username);
        return person;
    }
}
