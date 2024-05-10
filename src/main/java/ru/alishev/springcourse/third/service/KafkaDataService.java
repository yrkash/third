package ru.alishev.springcourse.third.service;

import ru.alishev.springcourse.third.model.Measurement;

public interface KafkaDataService {

    void send(Measurement measurement);



}
