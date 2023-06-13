package ru.alishev.springcourse.third.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.alishev.springcourse.third.model.Person;
import ru.alishev.springcourse.third.service.PersonDetailsService;

import java.util.Optional;

@Component
public class PersonValidator implements Validator {

    private final PersonDetailsService personDetailsService;
    @Autowired
    public PersonValidator(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        int year = person.getYearOfBirth();
        if (year > 2023)
            errors.rejectValue("yearOfBirth", "","Год должен быть меньше 2023");
        Optional<Person> optionalPerson = personDetailsService.loadOptionalOfUserByUsername(person.getUsername());
        /*try {
            personDetailsService.loadUserByUsername(person.getUsername());
        } catch (UsernameNotFoundException ignored) {
            return;
        }*/
        if (optionalPerson.isPresent()) {
            errors.rejectValue("username","","Человек с таким именем уже существует");
        }
    }
}

