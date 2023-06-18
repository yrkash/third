package ru.alishev.springcourse.third.model;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Cascade;
import org.hibernate.validator.constraints.Email;

import java.time.LocalDateTime;


@Entity
@Table(name = "measurement")
public class Measurement {

    @Id
    @Column (name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @JsonIgnore
    private Integer id;

    @Column(name = "value")
    @Min(value = -100, message = "Temperature should be greater than -100")
    @Max(value = 100, message = "Temperature should be less than 100")
    private Double value;

    @Column(name = "raining")
    @NotNull
    private Boolean raining;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @NotNull
    @ManyToOne
//    @JsonManagedReference
//    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    @JoinColumn(name = "sensor", referencedColumnName = "name") // Класс Sensor реализует интерфейс Serializable
//    @JoinColumn(name = "sensor_id", referencedColumnName = "id") Не работает
    private Sensor sensor;



    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Boolean isRaining() {
        return raining;
    }

    public void setRaining(boolean raining) {
        this.raining = raining;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }
}
