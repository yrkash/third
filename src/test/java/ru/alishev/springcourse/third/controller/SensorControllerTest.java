package ru.alishev.springcourse.third.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.alishev.springcourse.third.model.Sensor;
import ru.alishev.springcourse.third.service.SensorService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SensorControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private SensorService sensorService;

    private MockMvc mockMvc;

    public SensorControllerTest() {
    }

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void getSensorList_shouldReturnOkStatus() throws Exception {
        // Arrange
        Sensor sensor1 = new Sensor();
        sensor1.setName("nnov_1");
        Sensor sensor2 = new Sensor();
        sensor2.setName("mos_77");
        Sensor sensor3 = new Sensor();
        sensor3.setName("vsk_10");

//        sensorService.save(sensor1);
//        sensorService.save(sensor2);

        // Act & Assert
//        MvcResult mvcResult = this.mockMvc.perform(get(""));

        mockMvc.perform(get("/sensors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(sensor1.getName()))
                .andExpect(jsonPath("$[1].name").value(sensor2.getName()))
                .andExpect(jsonPath("$[2].name").value(sensor3.getName()))
                .andReturn();
    }

    /*@Test
    public void create_shouldReturnOkStatus() throws Exception {
        // Arrange
        SensorDTO sensorDTO = new SensorDTO("Sensor 1");

        // Act & Assert
        mockMvc.perform(post("/sensors/registration")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(new ObjectMapper().writeValueAsString(sensorDTO)))
                .andExpect(status().isOk());
    }*/
}
