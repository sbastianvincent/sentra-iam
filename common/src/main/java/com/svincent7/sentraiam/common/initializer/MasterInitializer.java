package com.svincent7.sentraiam.common.initializer;

import com.svincent7.sentraiam.common.config.ConfigProperties;
import jakarta.annotation.PostConstruct;

import java.util.Collection;
import java.util.Comparator;

public class MasterInitializer {
    private final Collection<Initializer> initializers;
    private final ConfigProperties configProperties;

    public MasterInitializer(final Collection<Initializer> initializers, final ConfigProperties configProperties) {
        this.initializers = initializers;
        this.configProperties = configProperties;
    }

    @PostConstruct
    public void init() {
        if (!configProperties.isShouldRunInitializer()) {
            return;
        }

        initializers.stream().sorted(Comparator.comparingInt(Initializer::getOrder))
                .forEach(Initializer::initialize);
    }
}
