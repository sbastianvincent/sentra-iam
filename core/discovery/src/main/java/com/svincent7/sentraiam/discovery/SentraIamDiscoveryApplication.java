package com.svincent7.sentraiam.discovery;

import com.svincent7.sentraiam.common.initializer.CsrBootstrapInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class SentraIamDiscoveryApplication {
    public static void main(final String[] args) {
        SpringApplication app = new SpringApplication(SentraIamDiscoveryApplication.class);
        app.addInitializers(new CsrBootstrapInitializer());
        app.run(args);
    }
}
