package com.wakanda.waste;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class WasteApplication {

    public static void main(String[] args) {
        // Esta línea es la que arranca el servidor Tomcat y tu código
        SpringApplication.run(WasteApplication.class, args);
    }
}