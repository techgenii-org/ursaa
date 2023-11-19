package com.example.ursaa.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.influxdb.client.InfluxDBClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.Random;

@Service
@Slf4j
public class KafkaService {

    @Value("${spring.kafka.topic.name}")
    private String topicName;

    @Autowired
    private ReactiveKafkaConsumerTemplate<String, Temperature> reactiveKafkaConsumerTemplate;

    @Autowired
    private ReactiveKafkaProducerTemplate<String,Temperature> reactiveKafkaProducerTemplate;

    @Autowired
    private InfluxDbService influxDbService;

    @Autowired
    private ObjectMapper objectMapper;


    public void sendMessage() {
        Temperature temperature = new Temperature();
                    temperature.setLocation(getLocation());
                    temperature.setValue(getValue());
                    temperature.setTime(Instant.now());

        reactiveKafkaProducerTemplate.send(topicName, temperature)
                .doOnSuccess(senderResult -> log.info("sent {} offset : {}", temperature, senderResult.recordMetadata().offset()))
                .subscribe();
        //        kafkaTemplate.send(topicName, message);
    }

    private static Double getValue() {
        Random r = new Random();

        return -20 + 70 * r.nextDouble();
    }

    private static String getLocation() {
        return "Prague";
    }

//    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "your_group_id")
//    public void listen(String message) {
//        System.out.println("Received message: " + message);
//    }


    @EventListener(ApplicationStartedEvent.class)
    public Flux<Temperature> startKafkaConsumer() {
        return reactiveKafkaConsumerTemplate
                .receiveAutoAck()
                // .delayElements(Duration.ofSeconds(2L)) // BACKPRESSURE
                .doOnNext(consumerRecord -> log.info("received key={}, value={} from topic={}, offset={}",
                        consumerRecord.key(),
                        consumerRecord.value(),
                        consumerRecord.topic(),
                        consumerRecord.offset())
                )
                .map(ConsumerRecord::value)
                .doOnNext(temperature -> {
                    log.info("successfully consumed {}={}", Temperature.class.getCanonicalName(), temperature);
                    influxDbService.writeData(temperature);
                })
                .doOnError(throwable -> log.error("something bad happened while consuming : {}", throwable.getMessage()));
    }
}
