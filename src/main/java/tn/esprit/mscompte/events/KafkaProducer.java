package tn.esprit.mscompte.events;

import jdk.jfr.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {
    private final KafkaTemplate<String, Event> kafkaTemplate;
    private static final String TOPIC = "compte-events";

    public void produceEvent(Event compteEvent) {
        kafkaTemplate.send(TOPIC, compteEvent.type().toString(), compteEvent);
    }
}
