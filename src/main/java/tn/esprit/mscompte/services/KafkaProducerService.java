package tn.esprit.mscompte.services;

import jdk.jfr.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<String, Event<?>> kafkaTemplate;
    private static final String TOPIC = "compte-events";

    public void produceEvent(Event<?> event) {
        log.info("Envoi de l'événement Kafka : {}", event);
        kafkaTemplate.send(TOPIC, event.type().toString(), event);
    }
}