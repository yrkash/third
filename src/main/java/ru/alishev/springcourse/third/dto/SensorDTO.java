package ru.alishev.springcourse.third.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class SensorDTO {

    @Column(name = "name")
    @NotEmpty(message = "Название не должно быть пустым!")
    @Size(min = 3, max = 30, message = "Название сенсора должно быть от 3 до 30 символов!")
    private String name;


}
