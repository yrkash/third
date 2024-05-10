package ru.alishev.springcourse.third.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;
import ru.alishev.springcourse.third.model.Measurement;

@Service
@RequiredArgsConstructor
public class KafkaDataServiceImpl implements KafkaDataService{

    private final KafkaSender<String, Object> sender;
    @Override
    public void send(Measurement data) {
        String topic = "data-temperature";
        sender.send(
                Mono.just(
                        SenderRecord.create(
                                topic,
                                0,
                                System.currentTimeMillis(),
                                String.valueOf(data.hashCode()),
                                data,
                                null
                        )
                )
        )
        .subscribe();
    }
}
