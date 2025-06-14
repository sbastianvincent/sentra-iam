package com.svincent7.sentraiam.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class SentraIamDiscoveryApplication {
    public static void main(final String[] args) {
        SpringApplication.run(SentraIamDiscoveryApplication.class, args);
    }
}
