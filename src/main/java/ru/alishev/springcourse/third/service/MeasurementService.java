package ru.alishev.springcourse.third.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alishev.springcourse.third.model.Measurement;
import ru.alishev.springcourse.third.model.Sensor;
import ru.alishev.springcourse.third.repository.MeasurementRepository;
import ru.alishev.springcourse.third.util.MeasurementNotFoundException;
import ru.alishev.springcourse.third.util.SensorNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MeasurementService {

    private final MeasurementRepository measurementRepository;

    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }


    public List<Measurement> findAll() {return measurementRepository.findAll();}

    public Measurement findOne(int id) {
        Optional<Measurement> foundMeasurement = measurementRepository.findById(id);
        return foundMeasurement.orElseThrow(MeasurementNotFoundException::new);
    }

    @Transactional
    public void save(Measurement measurement) {
        enrichMeasurement(measurement);
        measurementRepository.save(measurement);
    }

    private void enrichMeasurement(Measurement measurement) {
        measurement.setCreatedAt(LocalDateTime.now());
    }

}
