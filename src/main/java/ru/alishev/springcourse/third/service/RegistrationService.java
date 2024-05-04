package ru.alishev.springcourse.third.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alishev.springcourse.third.model.Person;
import ru.alishev.springcourse.third.model.Role;
import ru.alishev.springcourse.third.repository.PersonRepository;

import java.util.Optional;
import java.util.Set;

@Service
public class RegistrationService {
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        Set<Role> roles = Set.of(Role.ROLE_USER);
        person.setRoles(roles);
//        person.setRole("ROLE_USER");
        personRepository.save(person);
    }

    @Transactional
    public void delete(String username) {
        Optional<Person> optionalPerson = personRepository.findByUsername(username);
        if (optionalPerson.isPresent()) {
            personRepository.delete(optionalPerson.get());
        }
    }

    public void performAddAdminRole(String username) {
        Optional<Person> optionalPerson = personRepository.findByUsername(username);
        if (optionalPerson.isPresent()) {
            Set<Role> currentRoles = optionalPerson.get().getRoles();
            currentRoles.add(Role.ROLE_ADMIN);
            optionalPerson.get().setRoles(currentRoles);
            personRepository.save(optionalPerson.get());
        }
    }
}
