package ru.alishev.springcourse.third.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alishev.springcourse.third.model.Sensor;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Integer> {
}
