package com.resilience4j.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ResilienceController {

    private static final String KAFKA_MESSAGE_SERVICE = "kafkaMessageService";
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/resilience")
    @CircuitBreaker(name=KAFKA_MESSAGE_SERVICE, fallbackMethod = "orderFallback")
    public ResponseEntity<String> callAPI() {
        String response = restTemplate.getForObject("http://localhost:8080/kafka/publish", String.class);
        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    public ResponseEntity<String> orderFallback(Exception exception) {
        return new ResponseEntity<String>("Kafka Message Service is down.", HttpStatus.OK);
    }
}
