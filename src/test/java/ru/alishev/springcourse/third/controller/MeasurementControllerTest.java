package ru.alishev.springcourse.third.controller;

import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.alishev.springcourse.third.dto.MeasurementDTO;
import ru.alishev.springcourse.third.dto.SensorDTO;
import ru.alishev.springcourse.third.model.Measurement;
import ru.alishev.springcourse.third.model.Sensor;
import ru.alishev.springcourse.third.service.MeasurementService;
import ru.alishev.springcourse.third.service.SensorService;
import ru.alishev.springcourse.third.utils.IsInSensorList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        ArrayList<Sensor> sensorList = new ArrayList<>();
        sensorList.add(sensor1);
        sensorList.add(sensor2);
        sensorList.add(sensor3);

        mockMvc.perform(get("/measurements"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].value").isNumber())
                .andExpect(jsonPath("$[0].raining").isBoolean())
                .andExpect(jsonPath("$[0].sensor.name", new IsInSensorList(sensorList))) //IsInSensorList - custom Matcher
                .andReturn();
    }

    @Test
    public void getMeasurementListForSensorName_shouldReturnOkStatus() throws Exception {
        Sensor sensor1 = new Sensor();
        sensor1.setName("nnov_1");
        mockMvc.perform(get("/measurements/name/" + sensor1.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].value").isNumber())
                .andExpect(jsonPath("$[0].raining").isBoolean())
                .andExpect(jsonPath("$[0].sensor.name").value(sensor1.getName()))
                .andReturn();
    }
    @Test
    public void getRainyDaysCount_shouldReturnOkStatus() throws Exception {
        mockMvc.perform(get("/measurements/rainyDaysCount"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNumber())
                .andReturn();
    }

    @Test
    public void addMeasurementSuccess() throws Exception {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonSensor = new JSONObject();
        jsonObject.put("value", 20.03);
        jsonObject.put("raining", true);
        jsonSensor.put("name", "nnov_1");
        jsonObject.put("sensor", jsonSensor);
        String json = jsonObject.toString();

        mockMvc.perform(MockMvcRequestBuilders.post("/measurements/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
        List<Measurement> list = measurementService.findMeasurementsByValue(20.03);
        Measurement measurementToDelete = list.get(0);
        measurementService.delete(measurementToDelete.getId());
    }

    @Test
    public void addMeasurementNotSuccess() throws Exception {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonSensor = new JSONObject();
        jsonObject.put("value", 20.03);
        jsonObject.put("raining", true);
        jsonSensor.put("name", "nnov_123");
        jsonObject.put("sensor", jsonSensor);
        String json = jsonObject.toString();

        mockMvc.perform(MockMvcRequestBuilders.post("/measurements/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }



}
