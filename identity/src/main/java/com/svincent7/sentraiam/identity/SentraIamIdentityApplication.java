package com.svincent7.sentraiam.identity;

import com.svincent7.sentraiam.common.initializer.CsrBootstrapInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SentraIamIdentityApplication {

    public static void main(final String[] args) {
        SpringApplication app = new SpringApplication(SentraIamIdentityApplication.class);
        app.addInitializers(new CsrBootstrapInitializer());
        app.run(args);
    }

}
