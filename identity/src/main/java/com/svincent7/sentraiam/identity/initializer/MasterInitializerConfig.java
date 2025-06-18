package com.svincent7.sentraiam.identity.initializer;

import com.svincent7.sentraiam.common.config.ConfigProperties;
import com.svincent7.sentraiam.common.initializer.Initializer;
import com.svincent7.sentraiam.common.initializer.MasterInitializer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;

@Configuration
@RequiredArgsConstructor
public class MasterInitializerConfig {
    private final ConfigProperties config;
    private final Collection<Initializer> initializers;

    public static final int INIT_PERMISSION_ORDER = 0;
    public static final int INIT_IDENTITY_ORDER = INIT_PERMISSION_ORDER + 1;

    @Bean
    public MasterInitializer getMasterInitializer() {
        return new MasterInitializer(initializers, config);
    }
}
