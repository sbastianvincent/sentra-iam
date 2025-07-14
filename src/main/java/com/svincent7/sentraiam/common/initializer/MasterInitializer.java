package com.svincent7.sentraiam.common.initializer;

import com.svincent7.sentraiam.identity.config.ConfigProperties;
import jakarta.annotation.PostConstruct;

import java.util.Collection;
import java.util.Comparator;

public class MasterInitializer {
    private final Collection<Initializer> initializers;
    private final ConfigProperties configProperties;

    public MasterInitializer(final Collection<Initializer> initializerCollection, final ConfigProperties config) {
        this.initializers = initializerCollection;
        this.configProperties = config;
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
