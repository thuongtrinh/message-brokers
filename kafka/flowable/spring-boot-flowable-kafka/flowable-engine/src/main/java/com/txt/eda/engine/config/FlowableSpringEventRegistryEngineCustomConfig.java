package com.txt.eda.engine.config;


import com.txt.eda.engine.config.autodeloy.DefaultEventRegistryAutoDeploymentWithTenantIdStrategy;

import lombok.extern.log4j.Log4j2;
import org.flowable.eventregistry.api.ChannelModelProcessor;
import org.flowable.eventregistry.spring.SpringEventRegistryEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.flowable.spring.boot.eventregistry.EventRegistryServicesAutoConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;


@AutoConfigureBefore({
        EventRegistryServicesAutoConfiguration.class
})
@Configuration
@Log4j2
public class FlowableSpringEventRegistryEngineCustomConfig implements EngineConfigurationConfigurer<SpringEventRegistryEngineConfiguration> {

    @Value("${syspro-tenant-id}")
    private String tenantId;

    @Value("${syspro-deployment-mode}")
    private String deploymenMode;

    @Override
    public void configure(SpringEventRegistryEngineConfiguration config) {
        log.info("Start Event Registry Engine with tenantId {} and deployment mode {}", tenantId, deploymenMode);
        config.setEnableEventRegistryChangeDetectionAfterEngineCreate(false);
        config.setEnableEventRegistryChangeDetection(false);
        config.setDefaultTenantValue(tenantId);
        config.setDeploymentMode(deploymenMode);
        config.getDeploymentStrategies().add(new DefaultEventRegistryAutoDeploymentWithTenantIdStrategy(tenantId));
        config.setAlwaysLookupLatestDefinitionVersion(true);
        config.setFallbackToDefaultTenant(true);
        if (config.getChannelModelProcessors() != null) {
            Optional<ChannelModelProcessor> opt = config.getChannelModelProcessors().stream().findFirst();
            CustomKafkaChannelDefinitionConfiguation.CustomKafkaDefinitionProcessor customKafkaDefinitionProcessor = (CustomKafkaChannelDefinitionConfiguation.CustomKafkaDefinitionProcessor) opt.get();
            config.setOutboundEventProcessor(new CustomDefaultOutboundEventProcessor(config.getEventRepositoryService(), config.isFallbackToDefaultTenant(), customKafkaDefinitionProcessor));
        }
    }

}
