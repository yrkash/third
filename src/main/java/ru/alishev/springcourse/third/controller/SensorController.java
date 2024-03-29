package ru.alishev.springcourse.third.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.alishev.springcourse.third.dto.SensorDTO;
import ru.alishev.springcourse.third.model.Sensor;
import ru.alishev.springcourse.third.security.JWTUtil;
import ru.alishev.springcourse.third.service.SensorService;
import ru.alishev.springcourse.third.util.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sensors")
@RequiredArgsConstructor
@Secured("ROLE_ADMIN")
@Tag(name = "Сенсоры", description = "Контроллер для работы с сенсорами. Требуется JWT-аутентификация (только ROLE.ADMIN)")
public class SensorController {

    private final SensorService sensorService;
    private final ModelMapper modelMapper;
    private final SensorValidator sensorValidator;
    private final JWTUtil jwtUtil;

    private static final Logger LOGGER = LoggerFactory.getLogger(SensorController.class);

    @GetMapping()
    public List<SensorDTO> getSensorList() {
        LOGGER.info("Get All sensors");
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
//            Throw new CustomException() with message
            ErrorMessage.makeErrorMessage(bindingResult);
        }
        sensorService.save(sensorToAdd);
        // отправляем HTTP ответ с пустым телом и со статус 200
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<HttpStatus> delete(@RequestBody @Valid SensorDTO sensorDTO, BindingResult bindingResult) {

        Sensor sensorToDelete = convertToSensor(sensorDTO);
        Optional<Sensor> optionalSensor = sensorService.findByName(sensorToDelete.getName());
        if (optionalSensor.isPresent()) {
            sensorService.delete(sensorToDelete.getName());
            return ResponseEntity.ok(HttpStatus.OK);
        } else {
            ErrorResponse response = new ErrorResponse(
                    "Sensor with this name not found!", System.currentTimeMillis());
            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
        }
    }


    private Sensor convertToSensor(SensorDTO sensorDTO) {
        return modelMapper.map(sensorDTO, Sensor.class);
    }

    private SensorDTO convertToSensorDTO(Sensor sensor) {
        return modelMapper.map(sensor, SensorDTO.class);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(CustomException e) {
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
