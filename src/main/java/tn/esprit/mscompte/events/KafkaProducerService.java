package tn.esprit.mscompte.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import tn.esprit.mscompte.dto.CompteDto;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {
    private final KafkaTemplate<String, CompteDto> kafkaTemplate;
    private static final String TOPIC = "compte-events";

    public void sendCompteEvent(String eventType, CompteDto compteDto) {
        kafkaTemplate.send(TOPIC, eventType, compteDto);
        log.info("Sent event [{}] to Kafka with data: {}", eventType, compteDto);
    }
}
