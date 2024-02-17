package ru.alishev.springcourse.third.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class PersonDTO {
    @NotEmpty(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 100, message = "Имя должно быть от 2 до 100 символов длиной")
    private String username;

    @Min(value = 1900, message = "Год рождения должен быть больше, чем 1900")
    private int yearOfBirth;

    private String password;


}
