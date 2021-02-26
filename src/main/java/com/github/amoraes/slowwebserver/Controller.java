package com.github.amoraes.slowwebserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class Controller {

    private static final Logger log = LoggerFactory.getLogger(Controller.class);

    private IntRandomNumberGenerator intRandomNumberGenerator =
            new IntRandomNumberGenerator(1, 10);

    @RequestMapping("/health")
    public Mono<String> health() {
        return Mono.just("OK");
    }

    @RequestMapping("/**")
    public Mono<ResponseEntity<String>> hello(ServerHttpRequest request) {
        int delay = intRandomNumberGenerator.nextInt();
        log.info("{} {} received (reponse will take {} seconds)", request.getMethodValue(), request.getPath(), delay);
        return Mono.delay(Duration.ofSeconds(delay))
                .thenReturn("error!")
                .map(value -> {
                    return ResponseEntity.status(500).build();
                });
    }

}
