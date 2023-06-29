package ru.alishev.springcourse.third.controller;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.alishev.springcourse.third.dto.MeasurementDTO;
import ru.alishev.springcourse.third.dto.SensorDTO;
import ru.alishev.springcourse.third.model.Sensor;
import ru.alishev.springcourse.third.service.MeasurementService;
import ru.alishev.springcourse.third.service.SensorService;

import java.util.ArrayList;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MeasurementControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MeasurementService measurementService;

    private MockMvc mockMvc;

    private MeasurementDTO measurementDTO;
    private SensorDTO sensorDTO;

    public MeasurementControllerTest() {
    }

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        measurementDTO = new MeasurementDTO();
        sensorDTO = new SensorDTO();
        sensorDTO.setName("testSensor");
        measurementDTO.setValue(17.8);
        measurementDTO.setRaining(true);
        measurementDTO.setSensor(sensorDTO);
    }

    @Test
    public void getMeasurementList_shouldReturnOkStatus() throws Exception {
        // Arrange
        Sensor sensor1 = new Sensor();
        sensor1.setName("nnov_1");
        Sensor sensor2 = new Sensor();
        sensor2.setName("mos_77");
        Sensor sensor3 = new Sensor();
        sensor3.setName("vsk_10");
        ArrayList<String> sensorList = new ArrayList<>();
        sensorList.add(sensor1.getName());
        sensorList.add(sensor2.getName());
        sensorList.add(sensor3.getName());

        mockMvc.perform(get("/measurements"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].value").isNumber())
                .andExpect(jsonPath("$[0].raining").isBoolean())
//                .andExpect(jsonPath("$[0].sensor.name").value(Matchers.contains(sensorList)))
                .andExpect(jsonPath("$[0].sensor.name").value(sensor3.getName()))
                .andReturn();
    }

}
