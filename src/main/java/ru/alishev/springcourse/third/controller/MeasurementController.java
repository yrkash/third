package ru.alishev.springcourse.third.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.alishev.springcourse.third.dto.MeasurementDTO;
import ru.alishev.springcourse.third.model.Measurement;
import ru.alishev.springcourse.third.service.MeasurementService;
import ru.alishev.springcourse.third.service.SensorService;
import ru.alishev.springcourse.third.util.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/measurements")
public class MeasurementController {

    private final MeasurementService measurementService;
    private final SensorService sensorService;

    private final MeasurementValidator measurementValidator;
    private final ModelMapper modelMapper;
    @Autowired
    public MeasurementController(MeasurementService measurementService, SensorService sensorService, MeasurementValidator measurementValidator, ModelMapper modelMapper) {
        this.measurementService = measurementService;
        this.sensorService = sensorService;
        this.measurementValidator = measurementValidator;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public List<MeasurementDTO> getMeasurementList() {
        return measurementService.findAll().stream()
                .map(this::convertToMeasurementDTO)
                .collect(Collectors.toList()); // Jackson конвертирует эти объекты в JSON
    }

    @GetMapping("/name/{name}")
    public List<MeasurementDTO> getMeasurementListForSensor(@PathVariable("name") String name) {
        return measurementService.findAll().stream()
                .filter(m-> m.getSensor().getName().equals(name))
                .map(this::convertToMeasurementDTO)
                .collect(Collectors.toList()); // Jackson конвертирует эти объекты в JSON
    }

    @GetMapping("/rainyDaysCount")
    public Long getCountOfRaining() {
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
    private ResponseEntity<ErrorResponse> handleException(MeasurementException e) {
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
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private Measurement convertToMeasurement(MeasurementDTO measurementDTO) {
        return modelMapper.map(measurementDTO, Measurement.class);
    }

    private MeasurementDTO convertToMeasurementDTO(Measurement measurement) {
        return modelMapper.map(measurement, MeasurementDTO.class);
    }

}
