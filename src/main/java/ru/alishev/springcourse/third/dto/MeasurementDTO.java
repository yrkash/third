package ru.alishev.springcourse.third.dto;

import lombok.Data;
import ru.alishev.springcourse.third.model.Sensor;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class MeasurementDTO {

    @Column(name = "value")
    @Min(value = -100, message = "Temperature should be greater than -100")
    @Max(value = 100, message = "Temperature should be less than 100")
    private double value;

    @Column(name = "raining")
    @NotNull
    private boolean raining;

    @JoinColumn(name = "sensor_id", referencedColumnName = "id")
    private SensorDTO sensor;


}
