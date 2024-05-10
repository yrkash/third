package ru.alishev.springcourse.third.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.alishev.springcourse.third.dto.MeasurementDTO;
import ru.alishev.springcourse.third.dto.SensorDTO;
import ru.alishev.springcourse.third.model.Measurement;
import ru.alishev.springcourse.third.model.Sensor;
import ru.alishev.springcourse.third.service.KafkaDataService;
import ru.alishev.springcourse.third.service.MeasurementService;
import ru.alishev.springcourse.third.service.SensorService;
import ru.alishev.springcourse.third.util.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/measurements")
@RequiredArgsConstructor
@Tag(name = "Измерения", description = "Контроллер для работы с измерениями. Требуется JWT-аутентификация")
public class MeasurementController {

    private final KafkaDataService kafkaDataService;

    private final MeasurementService measurementService;
    private final SensorService sensorService;

    private final MeasurementValidator measurementValidator;
    private final ModelMapper modelMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(SensorController.class);

    @GetMapping()
    public List<MeasurementDTO> getMeasurementList() {
        LOGGER.info("Get All measurement");
        return measurementService.findAll().stream()
                .map(this::convertToMeasurementDTO)
                .collect(Collectors.toList()); // Jackson конвертирует эти объекты в JSON
    }

    @GetMapping("/name/{name}")
    public List<MeasurementDTO> getMeasurementListForSensor(@PathVariable("name") String name) {
        LOGGER.info("Get All measurement for a sensor");
        return measurementService.findAll().stream()
                .filter(m-> m.getSensor().getName().equals(name))
                .map(this::convertToMeasurementDTO)
                .collect(Collectors.toList()); // Jackson конвертирует эти объекты в JSON
    }

    @GetMapping("/rainyDaysCount")
    public Long getCountOfRaining() {
        LOGGER.info("Get count of rainy days");
        return measurementService.findAll().stream()
                .filter(Measurement::isRaining)
                .count();
    }


    @GetMapping("/{id}")
    public MeasurementDTO getMeasurement(@PathVariable("id") int id) {
        // Статус - 200
        return convertToMeasurementDTO(measurementService.findOne(id));
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(CustomException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // BAD_REQUEST - 400 статус
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(MeasurementNotFoundException e) {
        ErrorResponse response = new ErrorResponse(
                "Measurement with this id not found", System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // NOT_FOUND - 404 статус
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> add(@RequestBody @Valid MeasurementDTO measurementDTO, BindingResult bindingResult) {

        Measurement measurementToAdd = convertToMeasurement(measurementDTO);
        measurementValidator.validate(measurementToAdd,bindingResult);
        if (bindingResult.hasErrors()) {
//            Throw new MeasurementException() with message
            ErrorMessage.makeErrorMessage(bindingResult);
        }
        measurementService.addMeasurement(measurementToAdd);
        kafkaDataService.send(measurementToAdd);
        return ResponseEntity.ok(HttpStatus.OK);
    }

//    Only for MeasurementControllerTests
    @DeleteMapping("/delete")
    public ResponseEntity<HttpStatus> delete(Integer id) {

        try {
            measurementService.findOne(id);
            measurementService.delete(id);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (MeasurementNotFoundException e) {
            ErrorResponse response = new ErrorResponse(
                    "Sensor with this name not found!", System.currentTimeMillis());
            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
        }
    }

    private Measurement convertToMeasurement(MeasurementDTO measurementDTO) {
        return modelMapper.map(measurementDTO, Measurement.class);
    }

    private MeasurementDTO convertToMeasurementDTO(Measurement measurement) {
        return modelMapper.map(measurement, MeasurementDTO.class);
    }

}
