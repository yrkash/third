package ru.alishev.springcourse.third.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alishev.springcourse.third.model.Measurement;

public interface MeasurementRepository extends JpaRepository<Measurement, Integer> {
}
