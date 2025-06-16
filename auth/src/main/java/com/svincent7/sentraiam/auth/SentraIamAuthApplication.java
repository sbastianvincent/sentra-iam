package com.svincent7.sentraiam.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SentraIamAuthApplication {

    public static void main(final String[] args) {
        SpringApplication.run(SentraIamAuthApplication.class, args);
    }

}
