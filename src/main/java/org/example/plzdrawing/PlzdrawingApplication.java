package org.example.plzdrawing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PlzdrawingApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlzdrawingApplication.class, args);
    }

}
