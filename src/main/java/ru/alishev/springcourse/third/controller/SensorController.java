package ru.alishev.springcourse.third.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.alishev.springcourse.third.dto.SensorDTO;
import ru.alishev.springcourse.third.model.Sensor;
import ru.alishev.springcourse.third.service.SensorService;
import ru.alishev.springcourse.third.util.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sensors")
public class SensorController {

    private final SensorService sensorService;
    private final ModelMapper modelMapper;
    private final SensorValidator sensorValidator;

    @Autowired
    public SensorController(SensorService sensorService, ModelMapper modelMapper, SensorValidator sensorValidator) {
        this.sensorService = sensorService;
        this.modelMapper = modelMapper;
        this.sensorValidator = sensorValidator;
    }

    @GetMapping()
    public List<SensorDTO> getSensorList() {
        return sensorService.findAll().stream()
                .map(this::convertToSensorDTO)
                .collect(Collectors.toList()); // Jackson конвертирует эти объекты в JSON
    }

    @GetMapping("/{id}")
    public SensorDTO getSensor(@PathVariable("id") int id) {
        // Статус - 200
        return convertToSensorDTO(sensorService.findOne(id));
    }

    @GetMapping("/name/{name}")
    public SensorDTO getSensorForName(@PathVariable("name") String name) {
        Optional<Sensor> optionalSensor = sensorService.findByName(name);
        if (optionalSensor.isPresent()) {
            return convertToSensorDTO(optionalSensor.get());
        } else {
            throw new SensorNotFoundException();
        }
    }
    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid SensorDTO sensorDTO, BindingResult bindingResult) {

        Sensor sensorToAdd = convertToSensor(sensorDTO);
        sensorValidator.validate(sensorToAdd, bindingResult);

        if (bindingResult.hasErrors()) {
//            Throw new MeasurementException() with message
            ErrorMessage.makeErrorMessage(bindingResult);
        }
        sensorService.save(convertToSensor(sensorDTO));
        // отправляем HTTP ответ с пустым телом и со статус 200
        return ResponseEntity.ok(HttpStatus.OK);
    }


    private Sensor convertToSensor(SensorDTO sensorDTO) {
        return modelMapper.map(sensorDTO, Sensor.class);
    }

    private SensorDTO convertToSensorDTO(Sensor sensor) {
        return modelMapper.map(sensor, SensorDTO.class);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(MeasurementException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(), System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // BAD_REQUEST - 400 статус
    }
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(SensorNotFoundException e) {
        ErrorResponse response = new ErrorResponse(
                "Sensor with this id not found!", System.currentTimeMillis());

        //в HTTP ответе тело ответа (response) и статус в заголовке
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // NOT_FOUND - 404 статус
    }


}
