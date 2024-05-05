package com.txt.eda.rest.config;

import org.flowable.eventregistry.impl.EventRegistryEngine;
import org.flowable.eventregistry.impl.EventRegistryEngines;
import org.flowable.eventregistry.spring.SpringEventRegistryEngineConfiguration;

public class CustomSpringEventRegistryEngineConfiguration extends SpringEventRegistryEngineConfiguration {
    @Override
    public void start() {
        synchronized (lifeCycleMonitor) {
            if (!isRunning()) {
                enginesBuild.forEach(name -> {
                    EventRegistryEngine eventRegistryEngine = EventRegistryEngines.getEventRegistryEngine(name);
//                    eventRegistryEngine.handleDeployedChannelDefinitions();
                    createAndInitEventRegistryChangeDetectionExecutor();

                    autoDeployResources(eventRegistryEngine);
                });
                running = true;
            }
        }
    }
}
