package ru.alishev.springcourse.third.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.alishev.springcourse.third.model.Sensor;
import ru.alishev.springcourse.third.service.SensorService;

import java.util.Optional;

@Component
public class SensorValidator implements Validator {

    private final SensorService sensorService;

    @Autowired
    public SensorValidator(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Sensor.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Sensor sensor = (Sensor) target;
        Optional<Sensor> optionalSensor = sensorService.findByName(sensor.getName());
        if (optionalSensor.isPresent()) errors.rejectValue("name", "This name is already taken");
    }
}
