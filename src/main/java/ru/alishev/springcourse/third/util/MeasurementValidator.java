package ru.alishev.springcourse.third.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.alishev.springcourse.third.model.Measurement;
import ru.alishev.springcourse.third.model.Sensor;
import ru.alishev.springcourse.third.service.MeasurementService;

import java.util.Optional;

@Component
public class MeasurementValidator implements Validator {

    private final MeasurementService measurementService;

    @Autowired
    public MeasurementValidator(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Measurement.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Measurement measurement = (Measurement) target;
        Optional<Sensor> optionalSensor = measurementService.findBySensor(measurement.getSensor().getName());
        if (optionalSensor.isEmpty()) errors.rejectValue("sensor", "This sensor is not registered");

    }
}
