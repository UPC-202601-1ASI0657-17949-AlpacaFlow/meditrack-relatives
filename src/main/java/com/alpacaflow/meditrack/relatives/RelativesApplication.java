package com.alpacaflow.meditrack.relatives;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class RelativesApplication {

    public static void main(String[] args) {
        SpringApplication.run(RelativesApplication.class, args);
    }
}
