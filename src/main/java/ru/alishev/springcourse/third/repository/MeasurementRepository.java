package ru.alishev.springcourse.third.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alishev.springcourse.third.model.Measurement;
import ru.alishev.springcourse.third.model.Sensor;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Integer> {
    Optional<Measurement> findBySensor(String sensorName);

    List<Measurement> findAllBySensor(Sensor sensor);

}
