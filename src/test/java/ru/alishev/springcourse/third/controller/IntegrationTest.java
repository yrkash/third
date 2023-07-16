package ru.alishev.springcourse.third.controller;

import com.jayway.jsonpath.JsonPath;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.alishev.springcourse.third.dto.MeasurementDTO;
import ru.alishev.springcourse.third.dto.SensorDTO;
import ru.alishev.springcourse.third.model.Measurement;
import ru.alishev.springcourse.third.service.MeasurementService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MeasurementService measurementService;

    private MockMvc mockMvc;

    private String json;

    public IntegrationTest() {
    }
    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonSensor = new JSONObject();
        jsonObject.put("value", 20.03);
        jsonObject.put("raining", true);
        jsonSensor.put("name", "nnov_1");
        jsonObject.put("sensor", jsonSensor);
        json = jsonObject.toString();
    }

    @Test
    public void getRainyDaysCount_afterAddNewMeasurementWithRaining_afterGetRainyDaysCount_shouldReturnOkStatus() throws Exception {
        MvcResult result = mockMvc.perform(get("/measurements/rainyDaysCount"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNumber())
                .andReturn();
        String responseJson = result.getResponse().getContentAsString();
        Integer rainyDaysCount = JsonPath.parse(responseJson).read("$");
        System.out.println("Rainy days count: " + rainyDaysCount);
//        Add new Measurement
        mockMvc.perform(MockMvcRequestBuilders.post("/measurements/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
//        Check that rainyDaysCont incremented
        mockMvc.perform(get("/measurements/rainyDaysCount"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(rainyDaysCount + 1))
                .andReturn();
//      Delete tested measurement
        List<Measurement> list = measurementService.findMeasurementsByValue(20.03);
        Measurement measurementToDelete = list.get(0);
        measurementService.delete(measurementToDelete.getId());
    }


}
