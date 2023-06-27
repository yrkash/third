package ru.alishev.springcourse.third.controller;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.alishev.springcourse.third.dto.AuthenticationDTO;
import ru.alishev.springcourse.third.dto.PersonDTO;
import ru.alishev.springcourse.third.model.Person;
import ru.alishev.springcourse.third.security.JWTUtil;
import ru.alishev.springcourse.third.service.RegistrationService;
import ru.alishev.springcourse.third.util.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final PersonValidator personValidator;
    private final RegistrationService registrationService;
    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;

    private static final Logger LOGGER = LoggerFactory.getLogger(SensorController.class);

    @Autowired
    public AuthController(PersonValidator personValidator, RegistrationService registrationService, JWTUtil jwtUtil, ModelMapper modelMapper, AuthenticationManager authenticationManager) {
        this.personValidator = personValidator;
        this.registrationService = registrationService;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
    }

    @RequestMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("person") Person person) {
        return "auth/registration";
    }
    @PostMapping("/registration")
    public ResponseEntity<?>  performRegistration(@RequestBody @Valid PersonDTO personDTO,
                                                   BindingResult bindingResult) {
        LOGGER.info("Registration of new User");

        Person person =convertToPerson(personDTO);
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
//            Throw new CustomException() with message
            ErrorMessage.makeErrorMessage(bindingResult);
        }
            registrationService.register(person);
        String jwtToken = jwtUtil.generateToken(person.getUsername());
        return new ResponseEntity<>(new JWTResponse(jwtToken, System.currentTimeMillis()),HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity<?>  performLogin(@RequestBody AuthenticationDTO authenticationDTO) {
        LOGGER.info("Login...");
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(), authenticationDTO.getPassword());

        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            ErrorResponse errorResponse = new ErrorResponse(
                    "Incorrect login/password", System.currentTimeMillis());
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
        String jwtToken = jwtUtil.generateToken(authenticationDTO.getUsername());
        return new ResponseEntity<>(new JWTResponse(jwtToken, System.currentTimeMillis()),HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?>  performDelete(@RequestBody AuthenticationDTO authenticationDTO) {
        LOGGER.info("Delete user...");
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(), authenticationDTO.getPassword());

        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            ErrorResponse errorResponse = new ErrorResponse(
                    "Incorrect login/password", System.currentTimeMillis());
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
        Person person = convertToPerson(authenticationDTO);
        System.out.println(person.getId());
        registrationService.delete(authenticationDTO.getUsername());
        return new ResponseEntity<>(authenticationDTO.getUsername(),HttpStatus.OK);
    }

    public Person convertToPerson(PersonDTO personDTO) {
        return this.modelMapper.map(personDTO, Person.class);
    }

    public Person convertToPerson(AuthenticationDTO authenticationDTO) {
        return this.modelMapper.map(authenticationDTO, Person.class);
    }
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(CustomException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // BAD_REQUEST - 400 статус
    }

}
