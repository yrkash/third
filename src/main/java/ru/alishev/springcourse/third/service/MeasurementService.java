package ru.alishev.springcourse.third.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alishev.springcourse.third.model.Measurement;
import ru.alishev.springcourse.third.model.Sensor;
import ru.alishev.springcourse.third.repository.MeasurementRepository;
import ru.alishev.springcourse.third.util.MeasurementNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MeasurementService {

    private final MeasurementRepository measurementRepository;
    private final SensorService sensorService;

    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository, SensorService sensorService) {
        this.measurementRepository = measurementRepository;
        this.sensorService = sensorService;
    }

    public Measurement findOne(int id) {
        Optional<Measurement> foundMeasurement = measurementRepository.findById(id);
        return foundMeasurement.orElseThrow(MeasurementNotFoundException::new);
    }

    public Optional<Sensor> findBySensor(String sensorName) {
        return sensorService.findByName(sensorName);
    }

    public List<Measurement> findAll() {
        return measurementRepository.findAll();}


    @Transactional
    public void addMeasurement(Measurement measurement) {
        enrichMeasurement(measurement);
        measurementRepository.save(measurement);
    }


    public void enrichMeasurement(Measurement measurement) {
        // мы должны сами найти сенсор из БД по имени и вставить объект из Hibernate persistence context'а
        measurement.setSensor(sensorService.findByName(measurement.getSensor().getName()).get());

        measurement.setCreatedAt(LocalDateTime.now());
    }

}
