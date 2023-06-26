package ru.alishev.springcourse.third.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;
import ru.alishev.springcourse.third.controller.AuthController;
import ru.alishev.springcourse.third.dto.PersonDTO;
import ru.alishev.springcourse.third.model.Person;
import ru.alishev.springcourse.third.security.JWTUtil;
import ru.alishev.springcourse.third.service.RegistrationService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@RunWith(MockitoJUnitRunner.class)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class AuthControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private JWTUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private BindingResult bindingResult;

    private MockMvc mockMvc;

    private PersonDTO personDTO;



    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        personDTO = new PersonDTO();
        personDTO.setUsername("testuser");
        personDTO.setPassword("testpassword");
        personDTO.setYearOfBirth(2000);
    }

    @Test
    public void getUnAuth() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "yourUsername");
        jsonObject.put("password", "yourPassword");
        String json = jsonObject.toString();

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void postAdminIsOk() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "admin");
        jsonObject.put("password", "12345");
        String json = jsonObject.toString();

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwtToken").value(jwtUtil.generateToken("admin")))
        ;
    }

/*

    @Test
    public void testPerformRegistrationSuccess() throws Exception {
        Mockito.when(jwtUtil.generateToken(personDTO.getUsername())).thenReturn("jwtToken");
        System.out.println(personDTO.getUsername() + "!!!");
        String token = jwtUtil.generateToken(personDTO.getUsername());
        System.out.println(token);
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(personDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.jwtToken", Matchers.is(jwtUtil.generateToken(personDTO.getUsername()))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeStamp", Matchers.notNullValue()));

        Mockito.verify(registrationService, Mockito.times(1)).register(Mockito.any(Person.class));
    }
*/

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
