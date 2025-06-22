package com.svincent7.sentraiam.auth;

import com.svincent7.sentraiam.common.initializer.CsrBootstrapInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SentraIamAuthApplication {

    public static void main(final String[] args) {
        SpringApplication app = new SpringApplication(SentraIamAuthApplication.class);
        app.addInitializers(new CsrBootstrapInitializer());
        app.run(args);
    }

}
