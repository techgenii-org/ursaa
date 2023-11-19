package com.example.ursaa.controller;

import com.example.ursaa.dto.response.SampleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;
import java.util.UUID;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/v1/load-test")
public class ApiLoadTestController {

    private String uuid = UUID.randomUUID().toString();

    @Autowired
    private KafkaService kafkaService;

    @GetMapping("/sample-response")
    public Mono<SampleResponse> getSampleResponse() {
//        return Mono.just(SampleResponse.builder().uuid(uuid).build())
        return Mono.fromRunnable(() -> kafkaService.sendMessage());
    }

//    @PostConstruct
//    public void init() {
//        kafkaService.sendMessage("message");
//    }
}
