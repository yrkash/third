package ru.alishev.springcourse.third.controller;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.alishev.springcourse.third.dto.PersonDTO;
import ru.alishev.springcourse.third.dto.SensorDTO;
import ru.alishev.springcourse.third.model.Sensor;
import ru.alishev.springcourse.third.service.SensorService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ActiveProfiles("test")
public class SensorControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private SensorService sensorService;

    private MockMvc mockMvc;

    private SensorDTO sensorDTO;

    public SensorControllerTest() {
    }

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        sensorDTO = new SensorDTO();
        sensorDTO.setName("testSensor");
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

        mockMvc.perform(get("/sensors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(sensor1.getName()))
                .andExpect(jsonPath("$[1].name").value(sensor2.getName()))
                .andExpect(jsonPath("$[2].name").value(sensor3.getName()))
                .andReturn();
    }
    @Test
    public void getAbsentSensor() throws Exception {
        Sensor absentSensor = new Sensor();
        absentSensor.setName("absent");

        mockMvc.perform(get("/sensors/name/" + absentSensor.getName()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Sensor with this id not found!"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    @Test
    public void testRegistrationSuccess() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", sensorDTO.getName());
        String json = jsonObject.toString();

        mockMvc.perform(MockMvcRequestBuilders.post("/sensors/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void createExistingSensor() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "nnov_1");
        String json = jsonObject.toString();

        mockMvc.perform(MockMvcRequestBuilders.post("/sensors/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("name - This name is already taken;"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }
    @After
    public void reset() {
        sensorService.delete(sensorDTO.getName());
    }
}
